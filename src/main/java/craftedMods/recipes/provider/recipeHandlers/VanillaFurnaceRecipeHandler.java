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
package craftedMods.recipes.provider.recipeHandlers;

import java.util.*;

import org.apache.logging.log4j.Logger;

import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.*;
import craftedMods.recipes.api.utils.RecipeHandlerRendererUtils.EnumProgressBarDirection;
import craftedMods.recipes.base.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

@RegisteredHandler
public class VanillaFurnaceRecipeHandler extends AbstractMTRecipeHandler<FurnaceRecipe>
{

    private final VanillaFurnaceRecipeHandlerRenderer renderer = new VanillaFurnaceRecipeHandlerRenderer ();
    private final VanillaFurnaceRecipeHandlerRecipeViewer recipeViewer = new VanillaFurnaceRecipeHandlerRecipeViewer (
        this);

    public VanillaFurnaceRecipeHandler ()
    {
        super ("vanilla.furnace");
    }

    @Override
    public String getDisplayName ()
    {
        return Blocks.furnace.getLocalizedName ();
    }

    @Override
    public void onPreLoad (RecipeHandlerConfiguration config, Logger logger)
    {
        super.onPreLoad (config, logger);
        removeRecipeHandler ("codechicken.nei.recipe.FurnaceRecipeHandler");
    }

    private void removeRecipeHandler (String recipeHandlerClass)
    {
        try
        {
            RecipeHandlerUtils.getInstance ().removeNativeRecipeHandler (recipeHandlerClass);
        }
        catch (Exception e)
        {
            logger.error (String.format ("Couldn't remove the native recipe handler \"%s\"", recipeHandlerClass));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Collection<FurnaceRecipe> loadRecipes ()
    {
        Collection<FurnaceRecipe> ret = new ArrayList<> ();
        ((Map<ItemStack, ItemStack>) FurnaceRecipes.smelting ().getSmeltingList ()).forEach ( (ingredient, result) ->
        {
            ret.add (new FurnaceRecipe (ingredient, result));
        });
        return ret;
    }

    @Override
    public List<RecipeItemSlot> getSlotsForRecipeItems (FurnaceRecipe recipe, EnumRecipeItemRole role)
    {
        ArrayList<RecipeItemSlot> ret = new ArrayList<> ();
        switch (role)
        {
            case INGREDIENT:
                ret.add (createRecipeItemSlot (51, 6));
                break;
            case OTHER:
                ret.add (createRecipeItemSlot (51, 42));
                break;
            case RESULT:
                ret.add (createRecipeItemSlot (111, 24));
                break;
        }
        return ret;
    }

    @Override
    @SuppressWarnings("unchecked")
    public VanillaFurnaceRecipeHandlerRenderer getRenderer ()
    {
        return renderer;
    }

    @Override
    public RecipeHandlerRecipeViewer<FurnaceRecipe> getRecipeViewer ()
    {
        return recipeViewer;
    }

    @Override
    protected boolean isMineTweakerSupportEnabled ()
    {
        return RecipeHandlerUtils.getInstance ().hasMineTweaker ();
    }

    @Override
    public int getDefaultOrder ()
    {
        return 1000;
    }

    public class VanillaFurnaceRecipeHandlerRenderer
        implements RecipeHandlerRenderer<VanillaFurnaceRecipeHandler, FurnaceRecipe>
    {

        @Override
        public void renderBackground (VanillaFurnaceRecipeHandler handler, FurnaceRecipe recipe, int cycleticks)
        {
            RecipeHandlerRendererUtils.getInstance ().bindTexture ("textures/gui/container/furnace.png");
            RecipeHandlerRendererUtils.getInstance ().drawTexturedRectangle (0, 0, 5, 11, 166, 65);
            RecipeHandlerRendererUtils.getInstance ().drawRectangle (106, 19, 26, 26, 0xFFC6C6C6);
            RecipeHandlerRendererUtils.getInstance ().drawTexturedRectangle (106, 13, 51, 6, 22, 28);
            RecipeHandlerRendererUtils.getInstance ().drawProgressBar (74, 23, 176, 14, 24, 16, cycleticks % 48 / 48.0f,
                EnumProgressBarDirection.INCREASE_RIGHT);
            RecipeHandlerRendererUtils.getInstance ().drawProgressBar (51, 25, 176, 0, 14, 14, cycleticks % 48 / 48.0f,
                EnumProgressBarDirection.DECREASE_DOWN);
        }

        @Override
        public void renderForeground (VanillaFurnaceRecipeHandler handler, FurnaceRecipe recipe, int cycleticks)
        {
        }

    }

    public class VanillaFurnaceRecipeHandlerRecipeViewer
        extends AbstractRecipeViewer<FurnaceRecipe, VanillaFurnaceRecipeHandler>
    {

        private final Collection<Class<? extends GuiContainer>> supportedGuiClasses = new ArrayList<> ();

        public VanillaFurnaceRecipeHandlerRecipeViewer (VanillaFurnaceRecipeHandler handler)
        {
            super (handler);
            supportedGuiClasses.addAll (AbstractRecipeViewer.RECIPE_HANDLER_GUIS);
            supportedGuiClasses.add (GuiFurnace.class);
        }

        @Override
        public Collection<Class<? extends GuiContainer>> getSupportedGUIClasses ()
        {
            return supportedGuiClasses;
        }

        @Override
        public Collection<FurnaceRecipe> getAllRecipes ()
        {
            return handler.isMineTweakerSupportEnabled () ? handler.loadRecipes ()
                : handler.getStaticRecipes ();
        }

        @Override
        public int getOffsetX (Class<? extends GuiContainer> guiClass)
        {
            return guiClass == GuiFurnace.class ? 18 : 8;
        }

    }

}
