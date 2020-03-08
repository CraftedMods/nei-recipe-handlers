/*******************************************************************************
 * Copyright (C) 2019 CraftedMods (see https://github.com/CraftedMods)
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

import craftedMods.recipes.api.*;
import net.minecraft.item.crafting.*;
import net.minecraftforge.oredict.*;

/**
 * A basic recipe handler for 3x3 crafting grids.
 * 
 * @author CraftedMods
 */
public abstract class CraftingGridRecipeHandler extends AbstractMTRecipeHandler<AbstractRecipe>
{

    public static final int[][] DEFAULT_SHAPELESS_STACKORDER = new int[][]
    {
        {0, 0},
        {1, 0},
        {0, 1},
        {1, 1},
        {0, 2},
        {1, 2},
        {2, 0},
        {2, 1},
        {2, 2}};

    protected boolean logUndefinedRecipeTypes = true;

    protected CraftingGridRecipeHandler (String unlocalizedName)
    {
        super (unlocalizedName);

    }

    protected CraftingGridRecipeHandler (String unlocalizedName, Supplier<Collection<IRecipe>> recipesListGetter)
    {
        super (unlocalizedName, recipesListGetter);
    }

    protected Collection<AbstractRecipe> loadRecipes ()
    {
        List<AbstractRecipe> ret = new ArrayList<> ();

        for (IRecipe recipe : this.recipesListGetter.get ())
        {
            boolean nullResult = recipe.getRecipeOutput () == null || recipe.getRecipeOutput ().getItem () == null;
            boolean undefinedRecipeType = false;

            if (recipe instanceof ShapedOreRecipe)
            {
                if (nullResult)
                    continue;
                ShapedOreRecipe shapedOreRecipe = (ShapedOreRecipe) recipe;
                for (Object ingred : shapedOreRecipe.getInput ())
                    if (ingred instanceof List<?> && ((List<?>) ingred).isEmpty ())
                    {
                        continue;
                    }
                try
                {
                    ret.add (new ShapedRecipe (shapedOreRecipe));
                }
                catch (Exception e)
                {
                    this.logger.error ("Couldn't load shaped ore recipe: ", e);
                }
            }
            else if (recipe instanceof ShapedRecipes)
            {
                if (nullResult)
                    continue;
                ret.add (new ShapedRecipe ((ShapedRecipes) recipe));
            }
            else if (recipe instanceof ShapelessOreRecipe)
            {
                if (nullResult)
                    continue;
                ShapelessOreRecipe shapelessOreRecipe = (ShapelessOreRecipe) recipe;
                for (Object ingred : shapelessOreRecipe.getInput ())
                    if (ingred instanceof List<?> && ((List<?>) ingred).isEmpty ())
                    {
                        continue;
                    }
                ret.add (new ShapelessRecipe (shapelessOreRecipe));
            }
            else if (recipe instanceof ShapelessRecipes)
            {
                if (nullResult)
                    continue;
                ShapelessRecipes shapelessRecipe = (ShapelessRecipes) recipe;
                if (shapelessRecipe.recipeItems != null)
                {
                    ret.add (new ShapelessRecipe (shapelessRecipe));
                }
            }
            else
            {
                this.undefinedRecipeTypeFound (recipe, ret);
                undefinedRecipeType = true;
            }

            if (!undefinedRecipeType && nullResult)
                recipeWithNullResultFound (recipe);
        }
        this.logUndefinedRecipeTypes = false; // After loading the recipes one time, don't log those again
        return ret;
    }

    /**
     * Invoked if recipes were found which have null as result item stack (of if
     * stack.getItem() for that stack is null). Will only be invoked for recipes
     * which would have been processed otherwise. Can be overridden in child classes
     * to alternate the behaviour - by default a warning message will be logged.
     * 
     * @param recipe
     *            The recipe instance with the null result
     */
    protected void recipeWithNullResultFound (IRecipe recipe)
    {
        this.logger
            .warn ("The recipe handler \"" + this.getUnlocalizedName () + "\" got a recipe (\"" + recipe.getClass ()
                + "\") which has null as a result - it'll be ignored");
    }

    /**
     * Invoked if a recipe type was found which couldn'd be processed.</br>
     * The recipe can be processed in child classes which can add it to the
     * "container".
     * 
     * @param recipe
     *            The "undefined" recipe instance
     * @param container
     *            The list to which the processed recipe could be added
     */
    protected void undefinedRecipeTypeFound (IRecipe recipe, Collection<AbstractRecipe> container)
    {
        if (this.logUndefinedRecipeTypes)
        {
            this.logger
                .warn ("The recipe handler \"" + this.getUnlocalizedName () + "\" got a recipe (\"" + recipe.getClass ()
                    + "\") which couldn't be processed");
        }
    }

   

    @Override
    public List<RecipeItemSlot> getSlotsForRecipeItems (AbstractRecipe recipe, EnumRecipeItemRole role)
    {
        return this.getSlotsForRecipeItems (recipe, role, CraftingGridRecipeHandler.DEFAULT_SHAPELESS_STACKORDER);
    }

    @SuppressWarnings("incomplete-switch")
    protected List<RecipeItemSlot> getSlotsForRecipeItems (AbstractRecipe recipe, EnumRecipeItemRole role,
        int[][] shapelessStackorder)
    {
        List<RecipeItemSlot> ret = new ArrayList<> ();
        switch (role)
        {
            case INGREDIENT:
                if (recipe instanceof ShapedRecipe)
                {
                    ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
                    for (int x = 0; x < shapedRecipe.getWidth (); x++)
                    {
                        for (int y = 0; y < shapedRecipe.getHeight (); y++)
                        {
                            ret.add (this.createRecipeItemSlot (25 + x * 18, 6 + y * 18));
                        }
                    }
                }
                else if (recipe instanceof ShapelessRecipe)
                {
                    ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;
                    for (int i = 0; i < shapelessRecipe.getRecipeItems (EnumRecipeItemRole.INGREDIENT).size ()
                        && i < 9; i++)
                    {
                        ret.add (this.createRecipeItemSlot (25 + shapelessStackorder[i][0] * 18,
                            6 + shapelessStackorder[i][1] * 18));
                    }
                }
                break;
            case RESULT:
                ret.add (this.createRecipeItemSlot (119, 24));
                break;
        }
        return ret;
    }
}
