package craftedMods.recipes.utils;

import java.util.*;

import craftedMods.recipes.NEIRecipeHandlers;
import craftedMods.recipes.api.RecipeItemSlot;
import craftedMods.recipes.api.utils.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;

public class RecipeHandlerUtilsImpl implements RecipeHandlerUtils {

	private List<ItemStack> fuels;

	@Override
	public ItemStackSet generatePermutations(ItemStack... stacks) {
		return NEIRecipeHandlersUtils.generatePermutations(Arrays.asList(stacks));
	}

	@Override
	public ItemStackSet generatePermutations(Collection<ItemStack> stacks) {
		return NEIRecipeHandlersUtils.generatePermutations(stacks);
	}

	@Override
	public boolean areStacksSameType(ItemStack stack1, ItemStack stack2) {
		return NEIRecipeHandlersUtils.areStacksSameType(stack1, stack2);
	}

	@Override
	public boolean areStacksSameTypeForCrafting(ItemStack stack1, ItemStack stack2) {
		return NEIRecipeHandlersUtils.areStacksSameTypeForCrafting(stack1, stack2);
	}

	@Override
	public ItemStack[] extractRecipeItems(Object container) {
		return NEIRecipeHandlersUtils.extractRecipeItems(container);
	}

	@Override
	public List<ItemStack> getItemList() {
		return NEIRecipeHandlersUtils.getItemList();
	}

	@Override
	public Collection<ItemStack> readItemStackListFromNBT(NBTTagCompound compound, String tagName) {
		return NEIRecipeHandlersUtils.readItemStackListFromNBT(compound, tagName);
	}

	@Override
	public void writeItemStackListToNBT(NBTTagCompound compound, String tagName, Collection<? extends ItemStack> stacks) {
		NEIRecipeHandlersUtils.writeItemStackListToNBT(compound, tagName, stacks);
	}

	@Override
	public List<RecipeItemSlot> offset(List<RecipeItemSlot> slotsList, int xOffset, int yOffset) {
		return NEIRecipeHandlersUtils.offset(slotsList, xOffset, yOffset);
	}

	@Override
	public void forceRecipeCacheRefresh() {
		NEIRecipeHandlers.mod.getNEIIntegrationManager().refreshCache();
	}

	@Override
	public String getResourceDomain() {
		return NEIRecipeHandlers.MODID;
	}

	@Override
	public ItemStackSet getFuels() {
		if (this.fuels == null) {
			Set<Item> excludedfuels = new HashSet<>();
			excludedfuels.add(Item.getItemFromBlock(Blocks.brown_mushroom));
			excludedfuels.add(Item.getItemFromBlock(Blocks.red_mushroom));
			excludedfuels.add(Item.getItemFromBlock(Blocks.standing_sign));
			excludedfuels.add(Item.getItemFromBlock(Blocks.wall_sign));
			excludedfuels.add(Item.getItemFromBlock(Blocks.wooden_door));
			excludedfuels.add(Item.getItemFromBlock(Blocks.trapped_chest));
			this.fuels = new ArrayList<>();
			for (ItemStack item : RecipeHandlerUtils.getInstance().getItemList())
				if (!excludedfuels.contains(item.getItem())) {
					int burnTime = net.minecraft.tileentity.TileEntityFurnace.getItemBurnTime(item);
					if (burnTime > 0) this.fuels.add(item.copy());
				}
		}
		return ItemStackSet.create(this.fuels);
	}

	@Override
	public void removeNativeRecipeHandler(String recipeHandlerClass) throws ClassNotFoundException {
		NEIRecipeHandlers.mod.getNEIIntegrationManager().removeRecipeHandler(recipeHandlerClass);
	}

}
