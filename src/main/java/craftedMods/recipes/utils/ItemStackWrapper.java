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
package craftedMods.recipes.utils;

import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;

public class ItemStackWrapper
{

    private final boolean isNBTSensitive;
    private final ItemStack stack;
    private final Item item;
    private final int damage;
    private final NBTTagCompound compound;

    public ItemStackWrapper (ItemStack stack)
    {
        this (stack, false);
    }

    public ItemStackWrapper (ItemStack stack, boolean isNBTSensitive)
    {
        this.isNBTSensitive = isNBTSensitive;
        item = stack.getItem ();
        damage = stack.getItemDamage ();
        compound = stack.getTagCompound ();
        this.stack = stack;
    }

    public Item getItem ()
    {
        return item;
    }

    public int getDamage ()
    {
        return damage;
    }

    public NBTTagCompound getTagCompound ()
    {
        return compound;
    }

    public ItemStack toItemStack ()
    {
        return stack;
    }

    @Override
    public int hashCode ()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (item == null ? 0 : item.hashCode ());
        result = prime * result + damage;
        result = prime * result + (isNBTSensitive ? 1231 : 1237);
        if (isNBTSensitive)
        {
            result = prime * result + (compound == null ? 0 : compound.hashCode ());
        }
        return result;
    }

    @Override
    public boolean equals (Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass () != obj.getClass ())
            return false;
        ItemStackWrapper other = (ItemStackWrapper) obj;
        if (item == null)
        {
            if (other.item != null)
                return false;
        }
        else if (!item.equals (other.item))
            return false;
        if (damage != other.damage)
            return false;
        if (isNBTSensitive != other.isNBTSensitive)
            return false;
        if (isNBTSensitive)
            if (compound == null)
            {
                if (other.compound != null)
                    return false;
            }
            else if (!compound.equals (other.compound))
                return false;
        return true;
    }

}
