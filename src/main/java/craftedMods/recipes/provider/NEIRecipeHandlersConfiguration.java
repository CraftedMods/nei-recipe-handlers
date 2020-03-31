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
package craftedMods.recipes.provider;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class NEIRecipeHandlersConfiguration
{
    private Configuration configFile;

    private boolean hideTechnicalBlocks = true;
    private boolean useVersionChecker = true;
    private boolean brewingRecipeHandlerDisabled = false;
    private boolean complicatedStaticRecipeLoadingCacheEnabled = true;
    private int classDiscovererThreadTimeout = 10000;
    private boolean disabled = false;

    public NEIRecipeHandlersConfiguration (File configFile)
    {
        this.configFile = new Configuration (configFile);
        update ();
    }

    public void update ()
    {
        hideTechnicalBlocks = configFile.getBoolean ("Enable item hiding handlers",
            Configuration.CATEGORY_GENERAL, hideTechnicalBlocks,
            "Item hiding handlers hide items - typically technical block items - in NEI");
        useVersionChecker = configFile.getBoolean ("Use version checker", Configuration.CATEGORY_GENERAL,
            useVersionChecker,
            "Enables/disables the version checker of the mod. If it is disabled, you won't be notified about new available versions for the mod itself and any assigned handlers.");
        brewingRecipeHandlerDisabled = configFile.getBoolean ("Disable vanilla brewing recipe handler",
            Configuration.CATEGORY_GENERAL,
            brewingRecipeHandlerDisabled,
            "If set to true, the vanilla brewing recipe handler will be disabled. Set this to true if you don't use the vanilla brewing system.");
        complicatedStaticRecipeLoadingCacheEnabled = configFile.getBoolean ("Enable recipe caching",
            Configuration.CATEGORY_GENERAL,
            complicatedStaticRecipeLoadingCacheEnabled,
            "If set to true, some recipes that are expensive to compute will be saved on the filesystem, so they don't have to be computed every time the mod starts. The cached recipes will only be used if the set of registered items doesn't change, so if you add/remove mods that add items, the recipes have to be computed again. If you experience crashes on startup or the recipe handlers behave weirdly, try to disable this feature.");
        classDiscovererThreadTimeout = configFile.getInt ("Class discoverer thread timeout",
            Configuration.CATEGORY_GENERAL,
            classDiscovererThreadTimeout, 0, Integer.MAX_VALUE,
            "The maximum time the mod waits for the class discoverer thread to find the recipe handlers on the classpath");
        disabled = configFile.getBoolean ("Disable NEI integration", Configuration.CATEGORY_GENERAL,
            disabled,
            "If you want to disable NEI integration without removing the mod from your mods folder, you can do it by setting this property to true. This does not disable the version checker.");

        if (configFile.hasChanged ())
        {
            configFile.save ();
        }
    }

    public Configuration getConfigFile ()
    {
        return configFile;
    }

    public boolean isHideTechnicalBlocks ()
    {
        return hideTechnicalBlocks;
    }

    public boolean isUseVersionChecker ()
    {
        return useVersionChecker;
    }

    public boolean isBrewingRecipeHandlerDisabled ()
    {
        return brewingRecipeHandlerDisabled;
    }

    public boolean isComplicatedStaticRecipeLoadingCacheEnabled ()
    {
        return complicatedStaticRecipeLoadingCacheEnabled;
    }

    public int getClassDiscovererThreadTimeout ()
    {
        return classDiscovererThreadTimeout;
    }

    public boolean isDisabled ()
    {
        return disabled;
    }

}
