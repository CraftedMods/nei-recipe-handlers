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

import craftedMods.recipes.api.RecipeItemSlot;

/**
 * A default implementation for {@link craftedMods.recipes.api.RecipeItemSlot}.
 * Use it unless you need a custom one.
 *
 * @author CraftedMods
 */
public class RecipeItemSlotImpl implements RecipeItemSlot
{

    private final int x;
    private final int y;

    public RecipeItemSlotImpl (int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX ()
    {
        return x;
    }

    @Override
    public int getY ()
    {
        return y;
    }

}
