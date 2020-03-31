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
package craftedMods.recipes.base;

import java.util.*;
import java.util.function.Supplier;

import org.apache.logging.log4j.Logger;

import craftedMods.recipes.api.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

/**
 * A recipe handler which contains support for MineTweaker/CraftTweaker. That
 * especially means that static recipe loading won't function here - because of
 * the dynamic nature of MineTweaker, the recipe loading will have to be made
 * dynamic.
 *
 * @author CraftedMods
 *
 * @param <T>
 *            The recipe type
 */
public abstract class AbstractMTRecipeHandler<T extends Recipe> extends AbstractRecipeHandler<T>
{

    /**
     * A getter for the original list of recipes for the supported device. The
     * getter is important when something like MineTweaker is used, because the list
     * can change then.
     */
    protected final Supplier<Collection<IRecipe>> recipesListGetter;

    protected AbstractMTRecipeHandler (String unlocalizedName)
    {
        super (unlocalizedName);
        this.recipesListGetter = () -> new ArrayList<> (); // Default getter

    }

    protected AbstractMTRecipeHandler (String unlocalizedName, Supplier<Collection<IRecipe>> recipesListGetter)
    {
        super (unlocalizedName);
        this.recipesListGetter = recipesListGetter;
    }

    @Override
    public void onPreLoad (RecipeHandlerConfiguration config, Logger logger)
    {
        super.onPreLoad (config, logger);
        if (isMineTweakerSupportEnabled ())
        {
            this.logger.debug (
                "The MineTweaker API (and eventually additional MineTweaker plugins) were detected - dynamic recipe loading will be enabled");
        }
    }

    /**
     * A general function for loading recipes. The recipe loading for recipes that
     * can be loaded statically and dynamically should be done here (normally that
     * should be most of the recipes
     * {@link AbstractMTRecipeHandler#recipesListGetter} returns). Depending on
     * whether MT is present or not (determined by
     * {@link AbstractMTRecipeHandler#isMineTweakerSupportEnabled}) this function
     * will be called once during the static recipe loading phase or ingame via the
     * dynamic recipe loading functions.
     *
     * @return The computed recipes
     */
    protected abstract Collection<T> loadRecipes ();

    @Override
    public Collection<T> loadSimpleStaticRecipes ()
    {// No static recipe loading with MT
        return isMineTweakerSupportEnabled () ? null : this.loadRecipes ();
    }

    @Override
    public Collection<T> getDynamicCraftingRecipes (ItemStack result)
    {
        Collection<T> ret = new ArrayList<> ();

        // Use dynamic recipe loading with MineTweaker present
        if (isMineTweakerSupportEnabled ())
        {
            Collection<T> recipes = this.loadRecipes ();
            for (T recipe : recipes)
                if (recipe.produces (result))
                {
                    ret.add (recipe);
                }
        }

        return ret;
    }

    @Override
    public Collection<T> getDynamicUsageRecipes (ItemStack ingredient)
    {
        Collection<T> ret = new ArrayList<> ();

        // Use dynamic recipe loading with MineTweaker present
        if (isMineTweakerSupportEnabled ())
        {
            Collection<T> recipes = this.loadRecipes ();
            for (T recipe : recipes)
                if (recipe.consumes (ingredient))
                {
                    ret.add (recipe);
                }
        }
        return ret;
    }

    /**
     * Returns true whether MineTweaker support should be enabled for this handler.
     * The actual return value can depend on multiple variables, for example whether
     * the MineTweaker API is present, whether additional MineTweaker plugins are
     * present and so on. By default the support is disabled.
     *
     * @return If MT support is enabled
     */
    protected boolean isMineTweakerSupportEnabled ()
    {
        return false;
    }

}
