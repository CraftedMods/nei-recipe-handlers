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
package craftedMods.recipes.api;

import craftedMods.recipes.api.utils.RecipeHandlerUtils;
import net.minecraft.item.ItemStack;

/**
 * A handler that allows you to specify additional logic for
 * {@link RecipeHandlerUtils#areStacksSameType(ItemStack, ItemStack)} and
 * {@link RecipeHandlerUtils#areStacksSameTypeForCrafting(ItemStack, ItemStack)}.
 * The value determined by this handler will be priorized over the default
 * implementation of those functions, if the handler doesn't return a result,
 * the default implementation will be used. To be loaded, the handler needs to
 * be annotated with {@link craftedMods.recipes.api.RegisteredHandler}
 *
 * @author CraftedMods
 */
public interface ItemStackComparisonHandler
{

    /**
     * Returns whether this handler sees the supplied stacks are of the same type.
     * Used in the implementation of
     * {@link RecipeHandlerUtils#areStacksSameType(ItemStack, ItemStack)}. Return
     * true if the stacks are equal, false if not, and null if this handler doesn't
     * decide this.
     *
     * @param stack1
     *            The first stack
     * @param stack2
     *            The second stack
     * @return Whether the stacks are seen as being of the same type
     */
    public default Boolean areStacksOfSameType (ItemStack stack1, ItemStack stack2)
    {
        return null;
    }

    /**
     * Returns whether this handler sees the supplied stacks are of the same type
     * for crafting. Used in the implementation of
     * {@link RecipeHandlerUtils#areStacksSameTypeForCrafting(ItemStack, ItemStack)}.
     * Return true if the stacks are equal (for crafting), false if not, and null if
     * this handler doesn't decide this.
     *
     * @param stack1
     *            The first stack
     * @param stack2
     *            The second stack
     * @return Whether the stacks are seen as being of the same type for crafting
     */
    public default Boolean areStacksOfSameTypeForCrafting (ItemStack stack1, ItemStack stack2)
    {
        return null;
    }

}
