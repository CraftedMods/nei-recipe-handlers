/*******************************************************************************
 * Copyright (C) 2020 CraftedMods (see https://github.com/CraftedMods)
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
import craftedMods.utils.SemanticVersion;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

@Mod(modid = NEIRecipeHandlers.MODID, name = NEIRecipeHandlers.MODNAME, version = NEIRecipeHandlers.VERSION, acceptableRemoteVersions = "*", guiFactory = "craftedMods.recipes.ConfigurationGuiFactory")
public class NEIRecipeHandlers
{

    @Instance(NEIRecipeHandlers.MODID)
    public static NEIRecipeHandlers mod = new NEIRecipeHandlers ();

    private NEIRecipeHandlersConfiguration config;

    public static final String MODID = "neirecipehandlers";
    public static final String MODNAME = "NEI Recipe Handlers";
    public static final String VERSION = "1.1.0-BETA";

    public static final SemanticVersion SEMANTIC_VERSION = SemanticVersion.of (NEIRecipeHandlers.VERSION);

    public static final String MOD_DIR_NAME = "neiRecipeHandlers";
    public static final String ENVIRONMENT_CACHE_FILE_NAME = "environmentCache.dat";
    public static final String RECIPE_CACHE_FILE_NAME = "recipeCache.dat";

    private Logger logger;

    private NEIIntegrationManager neiIntegrationManager;

    private File modDir;
    private File environmentCache;
    private File recipeCache;

    private boolean worldLoaded = false;

    public NEIRecipeHandlersConfiguration getConfig ()
    {
        return config;
    }

    public Logger getLogger ()
    {
        return logger;
    }

    public NEIIntegrationManager getNEIIntegrationManager ()
    {
        return neiIntegrationManager;
    }

    public File getRecipeCache ()
    {
        return recipeCache;
    }

    @EventHandler
    public void onPreInit (FMLPreInitializationEvent event) throws IOException
    {
        logger = event.getModLog ();
        config = new NEIRecipeHandlersConfiguration (event.getSuggestedConfigurationFile ());

        logger.info ("NEI Integration enabled: " + !config.isDisabled ());

        try
        {
            modDir = new File (
                Minecraft.getMinecraft ().mcDataDir.getPath () + File.separator + NEIRecipeHandlers.MOD_DIR_NAME);

            if (!modDir.exists ())
            {
                modDir.mkdir ();
                logger.debug ("Successfully created the data directory of the mod");
            }
        }
        catch (Exception e)
        {
            logger.error ("Couldn't create the data directory: ");
            throw e;
        }

        try
        {
            environmentCache = new File (
                modDir.getPath () + File.separator + NEIRecipeHandlers.ENVIRONMENT_CACHE_FILE_NAME);

            if (!environmentCache.exists ())
            {
                environmentCache.createNewFile ();
                logger.debug ("Successfully created the environment cache file");
            }
        }
        catch (Exception e)
        {
            logger.error ("Couldn't create the environment cache file: ");
            throw e;
        }

        try
        {
            recipeCache = new File (
                modDir.getPath () + File.separator + NEIRecipeHandlers.RECIPE_CACHE_FILE_NAME);

            if (!recipeCache.exists ())
            {
                recipeCache.createNewFile ();
                logger.debug ("Successfully created the recipe cache file");
            }
        }
        catch (Exception e)
        {
            logger.error ("Couldn't create the recipe cache file: ");
            throw e;
        }

        logger.debug ("Version checker enabled: " + config.isUseVersionChecker ());

        if (!config.isDisabled ())
        {
            neiIntegrationManager = new NEIIntegrationManager (config, logger);
            neiIntegrationManager.preInit ();
        }

        MinecraftForge.EVENT_BUS.register (this);
        FMLCommonHandler.instance ().bus ().register (this);
    }

    @EventHandler
    public void onInit (FMLInitializationEvent event)
    {
    }

    @EventHandler
    public void onPostInit (FMLPostInitializationEvent event)
    {
        if (!config.isDisabled ())
        {
            try
            {
                // Load the NEI item list and wait until it's loaded
                ItemList.loadItems.restart ();
                Thread thread = ReflectionHelper.getPrivateValue (RestartableTask.class, ItemList.loadItems, "thread");
                if (thread != null)
                {
                    thread.join ();
                }
            }
            catch (Exception e)
            {
                logger.fatal ("Couldn't load the NEI item list - the mod cannot be started", e);
            }
        }

        neiIntegrationManager.init (checkEnvironmentCache ());
    }

    private boolean checkEnvironmentCache ()
    {
        boolean canCacheBeUsed = false;
        logger.info ("Recipe caching enabled: " + config.isComplicatedStaticRecipeLoadingCacheEnabled ());

        if (config.isComplicatedStaticRecipeLoadingCacheEnabled ())
        {
            NBTTagCompound currentEnvironmentListTag = new NBTTagCompound ();
            for (ModContainer mod : Loader.instance ().getModList ())
            {
                NBTTagCompound modTag = new NBTTagCompound ();
                modTag.setString ("Name", mod.getName ());
                modTag.setString ("Version", mod.getVersion ());
                currentEnvironmentListTag.setTag (mod.getModId (), modTag);
            }

            boolean recreateEnvironmentCache = true;
            try (FileInputStream in = new FileInputStream (environmentCache))
            {
                NBTTagCompound savedItemListTag = CompressedStreamTools.readCompressed (in);
                recreateEnvironmentCache = !savedItemListTag.equals (currentEnvironmentListTag);
            }
            catch (Exception e)
            {
                logger.warn ("The environment cache couldn't be loaded: ", e);
            }

            if (recreateEnvironmentCache)
            {
                logger.info ("The environment cache has to be (re)created");
                try (FileOutputStream out = new FileOutputStream (environmentCache))
                {
                    CompressedStreamTools.writeCompressed (currentEnvironmentListTag, out);
                    logger.info ("Wrote the environment cache to the filesystem");
                }
                catch (Exception e)
                {
                    logger.error ("Couldn't write the environment cache to the filesystem: ", e);
                }
            }

            canCacheBeUsed = !recreateEnvironmentCache;
        }
        return canCacheBeUsed;
    }

    @SubscribeEvent
    public void onConfigChanged (ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.modID.equals (NEIRecipeHandlers.MODID))
        {
            config.update ();
        }
    }

    @SubscribeEvent
    public void onWorldLoad (WorldEvent.Load event)
    {
        worldLoaded = true;
    }

    @SubscribeEvent
    public void onTick (TickEvent.ClientTickEvent event)
    {
        if (worldLoaded && Minecraft.getMinecraft ().theWorld != null
            && Minecraft.getMinecraft ().thePlayer != null)
        {
            neiIntegrationManager.onWorldLoad ();
            worldLoaded = false;
        }
    }

}
