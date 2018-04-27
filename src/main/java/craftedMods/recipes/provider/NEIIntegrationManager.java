package craftedMods.recipes.provider;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Supplier;

import org.apache.logging.log4j.Logger;

import codechicken.nei.api.API;
import codechicken.nei.recipe.*;
import craftedMods.recipes.NEIRecipeHandlers;
import craftedMods.recipes.api.*;
import craftedMods.recipes.utils.NEIRecipeHandlersUtils;
import craftedMods.utils.ClassDiscoverer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class NEIIntegrationManager {

	private final NEIRecipeHandlersConfiguration config;
	private final Logger logger;
	private final ClassDiscoverer discoverer;

	private RecipeHandlerManager recipeHandlerManager;

	private Collection<ItemHidingHandler> itemHidingHandlers = new ArrayList<>();
	private Collection<ItemOverrideHandler> itemOverrideHandlers = new ArrayList<>();

	private Collection<Class<?>> recipeHandlersToRemove = new HashSet<>();

	private ResourceHandlerResourcePack recipeHandlerResourcePack;

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
		this.discoverer.discoverClassesAsync();
	}

	public void init(boolean useCachedRecipes) {
		try {
			long start = System.currentTimeMillis();

			Map<Class<? extends Annotation>, Map<Class<?>, Set<Class<?>>>> discoveredClasses = this.discoverer
					.getDiscoveredClasses(this.config.getClassDiscovererThreadTimeout());

			this.setupResourceHandlerHandlerResourcePack(discoveredClasses);

			this.recipeHandlerManager = new RecipeHandlerManager(this.config.getConfigFile(), discoveredClasses);

			this.recipeHandlerManager.init(useCachedRecipes);

			NEIRecipeHandlers.mod.getLogger().info("Enable item hiding handlers: " + this.config.isHideTechnicalBlocks());

			if (this.config.isHideTechnicalBlocks())
				NEIRecipeHandlersUtils.discoverRegisteredHandlers(discoveredClasses, ItemHidingHandler.class, this.itemHidingHandlers);

			NEIRecipeHandlersUtils.discoverRegisteredHandlers(discoveredClasses, ItemOverrideHandler.class, this.itemOverrideHandlers);

			this.logger.info("Initialized NEI configuration within " + (System.currentTimeMillis() - start) + " ms");
		} catch (Exception e) {
			this.logger.error("Couldn't initialize NEI configuration: ", e);
		}
	}

	private void setupResourceHandlerHandlerResourcePack(Map<Class<? extends Annotation>, Map<Class<?>, Set<Class<?>>>> discoveredClasses) {
		Collection<ResourceHandler> handlersToRegister = new ArrayList<>();
		NEIRecipeHandlersUtils.discoverRegisteredHandlers(discoveredClasses, ResourceHandler.class, handlersToRegister);
		Iterator<ResourceHandler> handlersToRegisterIterator = handlersToRegister.iterator();
		while (handlersToRegisterIterator.hasNext()) {
			ResourceHandler handler = handlersToRegisterIterator.next();
			Map<ResourceLocation, Supplier<InputStream>> resources = handler.getResources();
			int resourceCount = resources == null ? 0 : resources.size();
			this.logger.debug(String.format("The resource handler \"%s\" registered %d resources", handler.getClass().getName(), resourceCount));
			if (resourceCount <= 0) handlersToRegisterIterator.remove();
		}
		this.recipeHandlerResourcePack = new ResourceHandlerResourcePack(handlersToRegister);
		NEIRecipeHandlersUtils.registerDefaultResourcePack(recipeHandlerResourcePack);
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
			if (this.config.isBrewingRecipeHandlerDisabled()) this.removeCraftingAndUsageHandler(BrewingRecipeHandler.class);

			for (Class<?> recipeHandlerToRemove : this.recipeHandlersToRemove) {
				if (IUsageHandler.class.isAssignableFrom(recipeHandlerToRemove))
					this.removeUsageHandler((Class<? extends IUsageHandler>) recipeHandlerToRemove);
				if (ICraftingHandler.class.isAssignableFrom(recipeHandlerToRemove))
					this.removeCraftingHandler((Class<? extends ICraftingHandler>) recipeHandlerToRemove);
			}

			// Load registered handlers
			this.recipeHandlerManager.getRecipeHandlers().forEach((unlocalizedName, handler) -> this.loadHandler(new PluginRecipeHandler<>(handler)));

			// Item hiding
			if (this.config.isHideTechnicalBlocks()) this.registerHiddenItems();

			// Override names
			this.registerItemOverrides();

			this.logger.info("Loaded NEI configuration for within " + (System.currentTimeMillis() - start) + " ms");
		}
	}

	public void refreshCache() {
		this.recipeHandlerManager.refreshCache();
	}

	private void loadHandler(TemplateRecipeHandler handler) {
		GuiCraftingRecipe.craftinghandlers.add(handler);
		GuiUsageRecipe.usagehandlers.add(handler);
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

}
