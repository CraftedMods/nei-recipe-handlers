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

import craftedMods.recipes.api.*;
import net.minecraft.nbt.NBTTagCompound;

/**
 * A base class for a cache manager. Usually you should extend this class if you
 * want to create a cache manager because it already fulfills a part of the
 * implementation contract.
 *
 * @author CraftedMods
 * @param <T>
 *            The recipe type processed by this handler
 */
public abstract class AbstractRecipeHandlerCacheManager<T extends Recipe> implements RecipeHandlerCacheManager<T>
{

    public static final String RECIPE_HANDLER_VERSION_KEY = "handlerVersion";

    protected final RecipeHandler<T> handler;

    private boolean isCacheValid = true;

    protected AbstractRecipeHandlerCacheManager (RecipeHandler<T> handler)
    {
        this.handler = handler;
    }

    @Override
    public boolean isCacheEnabled ()
    {
        return true;
    }

    @Override
    public void invalidateCache ()
    {
        this.isCacheValid = false;
    }

    @Override
    public void validateCache ()
    {
        this.isCacheValid = true;
    }

    @Override
    public boolean isCacheValid (NBTTagCompound cacheHeaderTag)
    {
        return this.isCacheValid && cacheHeaderTag
            .getString (AbstractRecipeHandlerCacheManager.RECIPE_HANDLER_VERSION_KEY).equals (this.getVersion ());
    }

    @Override
    public void writeRecipesToCache (NBTTagCompound cacheHeaderTag, NBTTagCompound cacheContentTag)
    {
        cacheHeaderTag.setString (AbstractRecipeHandlerCacheManager.RECIPE_HANDLER_VERSION_KEY, this.getVersion ());
    }

    @Override
    public String getVersion ()
    {
        return "1.0";
    }

}
