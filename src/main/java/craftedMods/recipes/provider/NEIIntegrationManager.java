/*******************************************************************************
 * Copyright (C) 2018 CraftedMods (see https://github.com/CraftedMods)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package craftedMods.recipes.provider;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Supplier;

import org.apache.logging.log4j.Logger;

import codechicken.nei.api.API;
import codechicken.nei.recipe.*;
import cpw.mods.fml.common.*;
import craftedMods.recipes.NEIRecipeHandlers;
import craftedMods.recipes.api.*;
import craftedMods.recipes.provider.recipeHandlers.VanillaCraftingTableRecipeHandler;
import craftedMods.recipes.utils.*;
import craftedMods.recipes.utils.VersionChecker.EnumVersionComparison;
import craftedMods.utils.ClassDiscoverer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraftforge.common.MinecraftForge;

public class NEIIntegrationManager implements IResourceManagerReloadListener {

	private final NEIRecipeHandlersConfiguration config;
	private final Logger logger;
	private final ClassDiscoverer discoverer;

	private RecipeHandlerManager recipeHandlerManager;

	private Collection<ItemHidingHandler> itemHidingHandlers = new ArrayList<>();
	private Collection<ItemOverrideHandler> itemOverrideHandlers = new ArrayList<>();

	private Map<VersionCheckerHandler, VersionChecker> versionCheckers = new HashMap<>();

	private Collection<Class<?>> recipeHandlersToRemove = new HashSet<>();

	private ResourceHandlerResourcePack recipeHandlerResourcePack;

	private boolean wasLoaded = false;

	public NEIIntegrationManager(NEIRecipeHandlersConfiguration config, Logger logger) {
		this.config = config;
		this.logger = logger;
		this.discoverer = new ClassDiscoverer(logger);
	}

	public void preInit() {
		this.discoverer.registerClassToDiscover(RegisteredHandler.class, RecipeHandler.class);
		this.discoverer.registerClassToDiscover(RegisteredHandler.class, RecipeHandlerFactory.class);
		this.discoverer.registerClassToDiscover(RegisteredHandler.class, ItemHidingHandler.class);
		this.discoverer.registerClassToDiscover(RegisteredHandler.class, ItemOverrideHandler.class);
		this.discoverer.registerClassToDiscover(RegisteredHandler.class, ResourceHandler.class);
		this.discoverer.registerClassToDiscover(RegisteredHandler.class, VersionCheckerHandler.class);
		this.discoverer.registerClassToDiscover(RegisteredHandler.class, VanillaCraftingTableRecipeHandlerSupport.class);
		this.discoverer.discoverClassesAsync();
	}

	public void init(boolean useCachedRecipes) {
		try {
			long start = System.currentTimeMillis();

			Map<Class<? extends Annotation>, Map<Class<?>, Set<Class<?>>>> discoveredClasses = this.discoverer
					.getDiscoveredClasses(this.config.getClassDiscovererThreadTimeout());

			this.setupResourceHandlerHandlerResourcePack(discoveredClasses);

			if (Minecraft.getMinecraft().getResourceManager() instanceof IReloadableResourceManager) {
				IReloadableResourceManager resourceManager = (IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager();
				resourceManager.registerReloadListener(this);
			} else {
				this.logger.warn(String.format("The MC resource manager doesn't implement %s. Some features of %s are therefore not available."),
						IReloadableResourceManager.class.getName(), NEIRecipeHandlers.MODNAME);
			}

			this.recipeHandlerManager = new RecipeHandlerManager(this.config.getConfigFile(), discoveredClasses);

			VanillaCraftingTableRecipeHandler vanillaRecipeHandler = new VanillaCraftingTableRecipeHandler(
					NEIRecipeHandlersUtils.discoverRegisteredHandlers(discoveredClasses, VanillaCraftingTableRecipeHandlerSupport.class));

			this.recipeHandlerManager.init(useCachedRecipes, Arrays.asList(vanillaRecipeHandler));

			NEIRecipeHandlers.mod.getLogger().info("Enable item hiding handlers: " + this.config.isHideTechnicalBlocks());

			if (this.config.isHideTechnicalBlocks()) {
				this.itemHidingHandlers.addAll(NEIRecipeHandlersUtils.discoverRegisteredHandlers(discoveredClasses, ItemHidingHandler.class));
			}

			this.itemOverrideHandlers.addAll(NEIRecipeHandlersUtils.discoverRegisteredHandlers(discoveredClasses, ItemOverrideHandler.class));

			Collection<VersionCheckerHandler> versionCheckerHandlers = new ArrayList<>(
					NEIRecipeHandlersUtils.discoverRegisteredHandlers(discoveredClasses, VersionCheckerHandler.class));

			// Add the handler names and versions to the modlist description
			StringBuilder newModDescriptionBuilder = new StringBuilder(EnumChatFormatting.DARK_GREEN + "\n\nLoaded modules: ");
			ModMetadata neiRecipeHandlersMetadata = FMLCommonHandler.instance().findContainerFor(NEIRecipeHandlers.mod).getMetadata();
			if (versionCheckerHandlers.isEmpty()) newModDescriptionBuilder.append(EnumChatFormatting.RED + "None");
			newModDescriptionBuilder.append("\n");
			for (VersionCheckerHandler handler : versionCheckerHandlers) {
				newModDescriptionBuilder.append(
						String.format("%s\n - %s (%s)", EnumChatFormatting.GREEN, handler.getLocalizedHandlerName(), handler.getCurrentVersion().toString()));
			}
			neiRecipeHandlersMetadata.description = neiRecipeHandlersMetadata.description.concat(newModDescriptionBuilder.toString());

			// Do the version checks
			if (this.config.isUseVersionChecker()) {
				for (VersionCheckerHandler handler : versionCheckerHandlers)
					if (handler.getCurrentVersion() == null || handler.getVersionFileURL() == null || !handler.getVersionFileURL().trim().isEmpty()) {
						VersionChecker checker = new VersionChecker(handler.getVersionFileURL(), handler.getCurrentVersion());
						if (NEIRecipeHandlersUtils.doVersionCheck(handler.getLocalizedHandlerName(), checker, this.logger)) {
							this.versionCheckers.put(handler, checker);
							handler.onVersionCheck(checker.getRemoteVersion().getRemoteVersion());
						}
					}
			}

			this.logger.info("Initialized NEI configuration within " + (System.currentTimeMillis() - start) + " ms");
		} catch (Exception e) {
			this.logger.error("Couldn't initialize NEI configuration: ", e);
		}
	}

	private void setupResourceHandlerHandlerResourcePack(Map<Class<? extends Annotation>, Map<Class<?>, Set<Class<?>>>> discoveredClasses) {
		Collection<ResourceHandler> handlersToRegister = new ArrayList<>(
				NEIRecipeHandlersUtils.discoverRegisteredHandlers(discoveredClasses, ResourceHandler.class));
		Iterator<ResourceHandler> handlersToRegisterIterator = handlersToRegister.iterator();
		while (handlersToRegisterIterator.hasNext()) {
			ResourceHandler handler = handlersToRegisterIterator.next();
			Map<ResourceLocation, Supplier<InputStream>> resources = handler.getResources();
			int resourceCount = resources == null ? 0 : resources.size();
			this.logger.debug(String.format("The resource handler \"%s\" registered %d resources", handler.getClass().getName(), resourceCount));
			if (resourceCount <= 0) {
				handlersToRegisterIterator.remove();
			}
		}
		this.recipeHandlerResourcePack = new ResourceHandlerResourcePack(handlersToRegister);
		NEIRecipeHandlersUtils.registerDefaultResourcePack(this.recipeHandlerResourcePack);
		this.logger.info("Registered the resource handler resource pack");
	}

	public void removeRecipeHandler(String recipeHandlerClass) throws ClassNotFoundException {
		Class<?> loadedClass = Class.forName(recipeHandlerClass);
		if (!IUsageHandler.class.isAssignableFrom(loadedClass) && !ICraftingHandler.class.isAssignableFrom(loadedClass)) throw new IllegalArgumentException(
				String.format("The provided class \"%s\" is not an instance of ICraftingHandler or IUsageHandler", loadedClass.getName()));
		this.recipeHandlersToRemove.add(loadedClass);
	}

	@SuppressWarnings("unchecked")
	public void load() {
		if (!this.config.isDisabled()) {

			long start = System.currentTimeMillis();

			// Remove recipe handlers
			if (this.config.isBrewingRecipeHandlerDisabled()) {
				this.removeCraftingAndUsageHandler(BrewingRecipeHandler.class);
			}

			for (Class<?> recipeHandlerToRemove : this.recipeHandlersToRemove) {
				if (IUsageHandler.class.isAssignableFrom(recipeHandlerToRemove)) {
					this.removeUsageHandler((Class<? extends IUsageHandler>) recipeHandlerToRemove);
				}
				if (ICraftingHandler.class.isAssignableFrom(recipeHandlerToRemove)) {
					this.removeCraftingHandler((Class<? extends ICraftingHandler>) recipeHandlerToRemove);
				}
			}

			// Load registered handlers
			this.recipeHandlerManager.getRecipeHandlers().forEach((unlocalizedName, handler) -> this.loadHandler(new PluginRecipeHandler<>(handler)));

			// Item hiding
			if (this.config.isHideTechnicalBlocks()) {
				this.registerHiddenItems();
			}

			// Override names
			this.registerItemOverrides();

			wasLoaded = true;

			this.logger.info("Loaded NEI configuration for within " + (System.currentTimeMillis() - start) + " ms");
		}
	}

	public void onWorldLoad() {
		if (this.config.isUseVersionChecker()) {
			this.versionCheckers.forEach((handler, checker) -> {
				if (checker.compareRemoteVersion() == EnumVersionComparison.NEWER) {
					Minecraft.getMinecraft().thePlayer.addChatComponentMessage(
							NEIRecipeHandlersUtils.getVersionNotificationChatText(handler.getLocalizedHandlerName(), checker.getRemoteVersion()));
				}
			});
		}
	}

	public void refreshCache() {
		this.recipeHandlerManager.refreshCache();
	}

	@Override
	public void onResourceManagerReload(IResourceManager manager) {
		if (wasLoaded) {
			this.registerItemOverrides();
			this.logger.info("Reloaded the item override handlers");
		}
	}

	private void loadHandler(PluginRecipeHandler<?, ?> handler) {
		GuiCraftingRecipe.craftinghandlers.add(handler);
		GuiUsageRecipe.usagehandlers.add(handler);

		new RecipeHandlerEventHandler(handler.getInnerHandler()); // The handler registers itself to the event bus

		NEIRecipeHandlersTransferRectManager.registerHandler(handler.getInnerHandler());
	}

	private <T extends ICraftingHandler & IUsageHandler> void removeCraftingAndUsageHandler(Class<T> handlerClass) {
		this.removeCraftingHandler(handlerClass);
		this.removeUsageHandler(handlerClass);
	}

	private void removeCraftingHandler(Class<? extends ICraftingHandler> handlerClass) {
		this.removeHandler(handlerClass, GuiCraftingRecipe.craftinghandlers, false);
	}

	private void removeUsageHandler(Class<? extends IUsageHandler> handlerClass) {
		this.removeHandler(handlerClass, GuiUsageRecipe.usagehandlers, true);
	}

	private <T> void removeHandler(Class<? extends T> handlerClass, List<T> handlerList, boolean isUsageHandler) {
		for (int i = 0; i < handlerList.size(); i++) {
			T handler = handlerList.get(i);
			if (handler.getClass() == handlerClass) {
				handlerList.remove(i);
				this.logger.debug(String.format("Removed the %s recipe handler \"%s\" of class \"%s\"", isUsageHandler ? "usage" : "crafting", handler,
						handlerClass.getName()));
			}
		}
	}

	private void registerHiddenItems() {
		for (ItemHidingHandler handler : this.itemHidingHandlers) {
			Collection<ItemStack> hiddenStacks = handler.getHiddenStacks();
			if (hiddenStacks != null) {
				hiddenStacks.forEach(API::hideItem);
				NEIRecipeHandlers.mod.getLogger().debug(
						"The item hiding handler \"" + handler.getClass() + "\" has hidden " + (hiddenStacks == null ? 0 : hiddenStacks.size()) + " items");
			}
		}
	}

	private void registerItemOverrides() {
		for (ItemOverrideHandler handler : this.itemOverrideHandlers) {
			Map<ItemStack, String> overrides = handler.getItemOverrideNames();
			if (overrides != null) {
				overrides.forEach(API::setOverrideName);
				NEIRecipeHandlers.mod.getLogger().debug("The item override handler \"" + handler.getClass() + "\" overwrote the name of "
						+ (overrides == null ? 0 : overrides.size()) + " items");
			}
		}
	}

	public class RecipeHandlerEventHandler {

		// private final RecipeHandler<?> handler;

		public RecipeHandlerEventHandler(RecipeHandler<?> handler) {
			// this.handler = handler;
			MinecraftForge.EVENT_BUS.register(this);
		}

	}

}
