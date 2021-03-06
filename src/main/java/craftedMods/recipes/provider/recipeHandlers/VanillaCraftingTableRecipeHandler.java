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
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;

import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.RecipeHandlerUtils;
import craftedMods.recipes.base.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.StatCollector;

public class VanillaCraftingTableRecipeHandler extends CraftingGridRecipeHandler
{

    private final Collection<VanillaCraftingTableRecipeHandlerSupport> supportHandlers = new ArrayList<> ();
    private final VanillaCraftingTableRecipeHandlerCraftingHelper craftingHelper = new VanillaCraftingTableRecipeHandlerCraftingHelper ();
    private final VanillaCraftingTableRecipeViewer recipeViewer = new VanillaCraftingTableRecipeViewer (this);

    public VanillaCraftingTableRecipeHandler (Collection<VanillaCraftingTableRecipeHandlerSupport> supportHandlers)
    {
        super ("vanilla.craftingTable", CraftingManager.getInstance ()::getRecipeList);
        this.supportHandlers.addAll (supportHandlers);
    }

    @Override
    public String getDisplayName ()
    {
        return StatCollector.translateToLocal ("neiRecipeHandlers.handler.vanilla.craftingTable.name");
    }

    @Override
    public void onPreLoad (RecipeHandlerConfiguration config, Logger logger)
    {
        super.onPreLoad (config, logger);
        removeRecipeHandler ("codechicken.nei.recipe.ShapedRecipeHandler");
        removeRecipeHandler ("codechicken.nei.recipe.ShapelessRecipeHandler");
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
    protected void undefinedRecipeTypeFound (IRecipe recipe, Collection<AbstractRecipe> container)
    {
        for (VanillaCraftingTableRecipeHandlerSupport supportHandler : supportHandlers)
        {
            Pair<Collection<AbstractRecipe>, Boolean> result = supportHandler.undefinedRecipeTypeFound (recipe);
            if (result != null)
            {
                if (result.getRight ())
                    return;
                if (result.getLeft () != null && !result.getLeft ().isEmpty ())
                {
                    container.addAll (result.getLeft ());
                    return;
                }
            }
        }
        super.undefinedRecipeTypeFound (recipe, container);
    }

    @Override
    public Collection<AbstractRecipe> getDynamicCraftingRecipes (ItemStack result)
    {
        Collection<AbstractRecipe> ret = super.getDynamicCraftingRecipes (result);

        // Let the support handlers add additional recipes
        supportHandlers.forEach (handler -> ret.addAll (handler.getDynamicCraftingRecipes (result)));

        return ret;
    }

    @Override
    public Collection<AbstractRecipe> getDynamicUsageRecipes (ItemStack ingredient)
    {
        Collection<AbstractRecipe> ret = super.getDynamicUsageRecipes (ingredient);

        // Let the support handlers add additional recipes
        supportHandlers.forEach (handler -> ret.addAll (handler.getDynamicUsageRecipes (ingredient)));

        return ret;
    }

    @Override
    public RecipeHandlerCraftingHelper<AbstractRecipe> getCraftingHelper ()
    {
        return craftingHelper;
    }

    @Override
    public AbstractRecipe loadComplicatedStaticRecipe (ItemStack... stacks)
    {
        // TODO Currently no MT support
        for (VanillaCraftingTableRecipeHandlerSupport supportHandler : supportHandlers)
        {
            AbstractRecipe recipe = supportHandler.loadComplicatedStaticRecipe (stacks);
            if (recipe != null)
                return recipe;
        }
        return null;
    }

    @Override
    public int getComplicatedStaticRecipeDepth ()
    {
        return supportHandlers.parallelStream ().map (handler -> handler.getComplicatedStaticRecipeDepth ())
            .collect (Collectors.maxBy (Comparator.naturalOrder ())).orElse (0);
    }

    @Override
    protected boolean isMineTweakerSupportEnabled ()
    {
        return RecipeHandlerUtils.getInstance ().hasMineTweaker ();
    }

    @Override
    public int getDefaultOrder ()
    {
        return 0;
    }

    @Override
    public RecipeHandlerRecipeViewer<AbstractRecipe> getRecipeViewer ()
    {
        return recipeViewer;
    }

    public class VanillaCraftingTableRecipeHandlerCraftingHelper extends AbstractCraftingHelper<AbstractRecipe>
    {

        @Override
        public Collection<Class<? extends GuiContainer>> getSupportedGUIClasses (AbstractRecipe recipe)
        {
            return isRecipe2x2 (recipe) ? Arrays.asList (GuiInventory.class, GuiCrafting.class)
                : Arrays.asList (GuiCrafting.class);
        }

        @Override
        public int getOffsetX (Class<? extends GuiContainer> guiClass, AbstractRecipe recipe)
        {
            return guiClass == GuiInventory.class ? 63 : 5;
        }

        @Override
        public int getOffsetY (Class<? extends GuiContainer> guiClass, AbstractRecipe recipe)
        {
            return guiClass == GuiInventory.class ? 20 : 11;
        }

        @Override
        public boolean matches (ItemStack stack1, ItemStack stack2)
        {
            return super.matches (stack1, stack2)
                && supportHandlers.parallelStream ()
                    .map (handler -> handler.matches (stack1, stack2)).reduce (Boolean::logicalAnd).orElse (true);
        }

        private boolean isRecipe2x2 (AbstractRecipe recipe)
        {
            boolean ret = recipe.getRecipeItems (EnumRecipeItemRole.INGREDIENT).size () <= 4;
            if (recipe instanceof ShapedRecipe)
            {
                ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
                ret = shapedRecipe.getWidth () <= 2 && shapedRecipe.getHeight () <= 2;
            }
            return ret;
        }
    }

    public class VanillaCraftingTableRecipeViewer
        extends AbstractRecipeViewer<AbstractRecipe, VanillaCraftingTableRecipeHandler>
    {

        private final Collection<Class<? extends GuiContainer>> supportedGuiClasses;

        public VanillaCraftingTableRecipeViewer (VanillaCraftingTableRecipeHandler handler)
        {
            super (handler);
            supportedGuiClasses = new ArrayList<> (AbstractRecipeViewer.RECIPE_HANDLER_GUIS);
            supportedGuiClasses.add (GuiCrafting.class);
        }

        @Override
        public Collection<AbstractRecipe> getAllRecipes ()
        {
            return handler.isMineTweakerSupportEnabled () ? handler.loadRecipes ()
                : handler.getStaticRecipes ();
        }

        @Override
        public Collection<Class<? extends GuiContainer>> getSupportedGUIClasses ()
        {
            return supportedGuiClasses;
        }

    }
}
