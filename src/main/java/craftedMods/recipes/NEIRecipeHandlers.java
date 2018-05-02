/*******************************************************************************
 * Copyright (C) 2018 CraftedMods (see https://github.com/CraftedMods)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package craftedMods.recipes;

import java.io.*;

import org.apache.logging.log4j.Logger;

import codechicken.nei.*;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import craftedMods.recipes.provider.*;
import craftedMods.recipes.utils.NEIRecipeHandlersUtils;
import craftedMods.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

@Mod(modid = NEIRecipeHandlers.MODID, name = NEIRecipeHandlers.MODNAME, version = NEIRecipeHandlers.VERSION, acceptableRemoteVersions = "*", guiFactory = "craftedMods.recipes.ConfigurationGuiFactory")
public class NEIRecipeHandlers {

	@Instance(NEIRecipeHandlers.MODID)
	public static NEIRecipeHandlers mod = new NEIRecipeHandlers();

	private NEIRecipeHandlersConfiguration config;

	public static final String MODID = "neirecipehandlers";
	public static final String MODNAME = "NEI Recipe Handlers";
	public static final String VERSION = "2.0.0-ALPHA";

	public static final SemanticVersion SEMANTIC_VERSION = new SemanticVersion(EnumVersionState.ALPHA, 2, 0, 0);

	public static final String MOD_DIR_NAME = "neiRecipeHandlers";
	public static final String ITEM_CACHE_FILE_NAME = "itemCache.dat";
	public static final String RECIPE_CACHE_FILE_NAME = "recipeCache.dat";

	private Logger logger;

	private NEIIntegrationManager neiIntegrationManager;

	private File modDir;
	private File itemCache;
	private File recipeCache;

	private boolean worldLoaded = false;

	public NEIRecipeHandlersConfiguration getConfig() {
		return this.config;
	}

	public Logger getLogger() {
		return this.logger;
	}

	public NEIIntegrationManager getNEIIntegrationManager() {
		return this.neiIntegrationManager;
	}

	public File getRecipeCache() {
		return this.recipeCache;
	}

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event) throws IOException {
		this.logger = event.getModLog();
		this.config = new NEIRecipeHandlersConfiguration(event.getSuggestedConfigurationFile());

		this.logger.info("NEI Integration enabled: " + !this.config.isDisabled());

		try {
			this.modDir = new File(Minecraft.getMinecraft().mcDataDir.getPath() + File.separator + NEIRecipeHandlers.MOD_DIR_NAME);

			if (!this.modDir.exists()) {
				this.modDir.mkdir();
				this.logger.debug("Successfully created the data directory of the mod");
			}
		} catch (Exception e) {
			this.logger.error("Couldn't create the data directory: ");
			throw e;
		}

		try {
			this.itemCache = new File(this.modDir.getPath() + File.separator + NEIRecipeHandlers.ITEM_CACHE_FILE_NAME);

			if (!this.itemCache.exists()) {
				this.itemCache.createNewFile();
				this.logger.debug("Successfully created the item cache file");
			}
		} catch (Exception e) {
			this.logger.error("Couldn't create the item cache file: ");
			throw e;
		}

		try {
			this.recipeCache = new File(this.modDir.getPath() + File.separator + NEIRecipeHandlers.RECIPE_CACHE_FILE_NAME);

			if (!this.recipeCache.exists()) {
				this.recipeCache.createNewFile();
				this.logger.debug("Successfully created the recipe cache file");
			}
		} catch (Exception e) {
			this.logger.error("Couldn't create the recipe cache file: ");
			throw e;
		}

		this.logger.debug("Version checker enabled: " + this.config.isUseVersionChecker());

		if (!this.config.isDisabled()) {
			this.neiIntegrationManager = new NEIIntegrationManager(this.config, this.logger);
			this.neiIntegrationManager.preInit();
		}

		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}

	@EventHandler
	public void onInit(FMLInitializationEvent event) {}

	@EventHandler
	public void onPostInit(FMLPostInitializationEvent event) {
		if (!this.config.isDisabled()) {
			try {
				// Load the NEI item list and wait until it's loaded
				ItemList.loadItems.restart();
				Thread thread = ReflectionHelper.getPrivateValue(RestartableTask.class, ItemList.loadItems, "thread");
				if (thread != null) {
					thread.join();
				}
			} catch (Exception e) {
				this.logger.fatal("Couldn't load the NEI item list - the mod cannot be started", e);
			}
		}

		this.neiIntegrationManager.init(this.checkItemCache());
	}

	private boolean checkItemCache() {
		boolean canCacheBeUsed = false;
		this.logger.info("Recipe caching enabled: " + this.config.isComplicatedStaticRecipeLoadingCacheEnabled());
		if (this.config.isComplicatedStaticRecipeLoadingCacheEnabled()) {
			NBTTagCompound currentItemListTag = new NBTTagCompound();
			NEIRecipeHandlersUtils.writeItemStackListToNBT(currentItemListTag, "items", ItemList.items);
			boolean recreateItemList = true;
			try (FileInputStream in = new FileInputStream(this.itemCache)) {
				NBTTagCompound savedItemListTag = CompressedStreamTools.readCompressed(in);
				recreateItemList = !savedItemListTag.equals(currentItemListTag);
			} catch (Exception e) {
				this.logger.warn("The item cache couldn't be loaded: ", e);
			}
			canCacheBeUsed = !recreateItemList;
			if (recreateItemList) {
				this.logger.info("The item cache has to be (re)created");
			}
			if (recreateItemList) {
				try (FileOutputStream out = new FileOutputStream(this.itemCache)) {
					CompressedStreamTools.writeCompressed(currentItemListTag, out);
					this.logger.info("Wrote the item cache to the filesystem");
				} catch (Exception e) {
					this.logger.error("Couldn't write the item cache to the filesystem: ", e);
				}
			}
		}
		return canCacheBeUsed;
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equals(NEIRecipeHandlers.MODID)) {
			this.config.update();
		}
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		this.worldLoaded = true;
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (this.worldLoaded && Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null) {
			this.neiIntegrationManager.onWorlLoad();
			this.worldLoaded = false;
		}
	}

}
