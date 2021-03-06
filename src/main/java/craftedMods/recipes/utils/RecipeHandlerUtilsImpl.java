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

import java.util.*;

import codechicken.nei.NEIClientConfig;
import codechicken.nei.guihook.GuiContainerManager;
import craftedMods.recipes.NEIRecipeHandlers;
import craftedMods.recipes.api.RecipeItemSlot;
import craftedMods.recipes.api.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;

public class RecipeHandlerUtilsImpl implements RecipeHandlerUtils
{

    private List<ItemStack> fuels;
    private Boolean hasMineTweaker = null;

    @Override
    public ItemStackSet generatePermutations (ItemStack... stacks)
    {
        return NEIRecipeHandlersUtils.generatePermutations (Arrays.asList (stacks));
    }

    @Override
    public ItemStackSet generatePermutations (Collection<ItemStack> stacks)
    {
        return NEIRecipeHandlersUtils.generatePermutations (stacks);
    }

    @Override
    public boolean areStacksSameType (ItemStack stack1, ItemStack stack2)
    {
        return NEIRecipeHandlersUtils.areStacksSameType (stack1, stack2);
    }

    @Override
    public boolean areStacksSameTypeForCrafting (ItemStack stack1, ItemStack stack2)
    {
        return NEIRecipeHandlersUtils.areStacksSameTypeForCrafting (stack1, stack2);
    }

    @Override
    public ItemStack[] extractRecipeItems (Object container)
    {
        return NEIRecipeHandlersUtils.extractRecipeItems (container);
    }

    @Override
    public List<ItemStack> getItemList ()
    {
        return NEIRecipeHandlersUtils.getItemList ();
    }

    @Override
    public Collection<ItemStack> readItemStackListFromNBT (NBTTagCompound compound, String tagName)
    {
        return NEIRecipeHandlersUtils.readItemStackListFromNBT (compound, tagName);
    }

    @Override
    public void writeItemStackListToNBT (NBTTagCompound compound, String tagName,
        Collection<? extends ItemStack> stacks)
    {
        NEIRecipeHandlersUtils.writeItemStackListToNBT (compound, tagName, stacks);
    }

    @Override
    public List<RecipeItemSlot> offset (List<RecipeItemSlot> slotsList, int xOffset, int yOffset)
    {
        return NEIRecipeHandlersUtils.offset (slotsList, xOffset, yOffset);
    }

    @Override
    public void forceRecipeCacheRefresh ()
    {
        NEIRecipeHandlers.mod.getNEIIntegrationManager ().refreshCache ();
    }

    @Override
    public String getResourceDomain ()
    {
        return NEIRecipeHandlers.MODID;
    }

    @Override
    public ItemStackSet getFuels ()
    {
        if (fuels == null)
        {
            Set<Item> excludedfuels = new HashSet<> ();
            excludedfuels.add (Item.getItemFromBlock (Blocks.brown_mushroom));
            excludedfuels.add (Item.getItemFromBlock (Blocks.red_mushroom));
            excludedfuels.add (Item.getItemFromBlock (Blocks.standing_sign));
            excludedfuels.add (Item.getItemFromBlock (Blocks.wall_sign));
            excludedfuels.add (Item.getItemFromBlock (Blocks.wooden_door));
            excludedfuels.add (Item.getItemFromBlock (Blocks.trapped_chest));
            fuels = new ArrayList<> ();
            for (ItemStack item : RecipeHandlerUtils.getInstance ().getItemList ())
                if (!excludedfuels.contains (item.getItem ()))
                {
                    int burnTime = net.minecraft.tileentity.TileEntityFurnace.getItemBurnTime (item);
                    if (burnTime > 0)
                    {
                        fuels.add (item.copy ());
                    }
                }
        }
        return ItemStackSet.create (fuels);
    }

    @Override
    public void removeNativeRecipeHandler (String recipeHandlerClass) throws ClassNotFoundException
    {
        NEIRecipeHandlers.mod.getNEIIntegrationManager ().removeRecipeHandler (recipeHandlerClass);
    }

    @Override
    public void writeItemStackToNBT (ItemStack stack, NBTTagCompound compound)
    {
        NEIRecipeHandlersUtils.writeItemStackToNBT (stack, compound);
    }

    @Override
    public ItemStack readItemStackFromNBT (NBTTagCompound compound)
    {
        return NEIRecipeHandlersUtils.readItemStackFromNBT (compound);
    }

    @Override
    public boolean isNEIGuiOpen ()
    {
        return NEIClientConfig.isEnabled () && !NEIClientConfig.isHidden () && GuiContainerManager.getManager () != null
            && (Minecraft.getMinecraft ().currentScreen instanceof GuiContainerCreative
                ? ((GuiContainerCreative) Minecraft.getMinecraft ().currentScreen)
                    .func_147056_g () == CreativeTabs.tabInventory.getTabIndex ()
                : true);
    }

    @Override
    public boolean hasMineTweaker ()
    {
        if (hasMineTweaker == null)
        {
            try
            {
                Class.forName ("minetweaker.MineTweakerAPI");
                hasMineTweaker = Boolean.TRUE;
            }
            catch (Exception e)
            {
                hasMineTweaker = Boolean.FALSE;
            }
        }
        return hasMineTweaker;
    }

}
