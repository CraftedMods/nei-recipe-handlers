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

import codechicken.nei.recipe.GuiRecipe;
import craftedMods.recipes.api.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public abstract class AbstractRecipeViewer<T extends Recipe, U extends RecipeHandler<T>>
    implements RecipeHandlerRecipeViewer<T>
{

    protected final U handler;
    public static final Collection<Class<? extends GuiContainer>> RECIPE_HANDLER_GUIS = Arrays.asList (GuiRecipe.class);

    public AbstractRecipeViewer (U handler)
    {
        this.handler = handler;
    }

    @Override
    public Collection<Class<? extends GuiContainer>> getSupportedGUIClasses ()
    {
        return AbstractRecipeViewer.RECIPE_HANDLER_GUIS;
    }

    @Override
    public boolean isGuiContainerSupported (GuiContainer container)
    {
        return true;
    }

    @Override
    public int getOffsetX (Class<? extends GuiContainer> guiClass)
    {
        return 0;
    }

    @Override
    public int getOffsetY (Class<? extends GuiContainer> guiClass)
    {
        return 0;
    }

    @Override
    public ItemStack getButtonIcon (Class<? extends GuiContainer> guiClass)
    {
        return new ItemStack (Items.writable_book);
    }

    @Override
    public String getButtonTooltip (Class<? extends GuiContainer> guiClass)
    {
        return StatCollector.translateToLocal ("neiRecipeHandlers.recipeViewer.defaultTooltip");
    }

}
