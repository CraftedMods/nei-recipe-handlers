package craftedMods.recipes.api.utils;

import java.util.*;

import craftedMods.recipes.utils.ItemStackSetImpl;
import net.minecraft.item.ItemStack;

/**
 * ItemStack hasn't an equals and hashCode implementation which means that it won't work well with
 * sets. A ItemStackSet is a set where ItemStacks can be used - just like with a normal set. But
 * instead of normal sets this will work well with ItemStacks.
 * 
 * @author CraftedMods
 * @param <V>
 *            The value type of the map
 */
public interface ItemStackSet extends Set<ItemStack> {

	/**
	 * Returns an NBT insensitive item stack set containing the provided stacks
	 * 
	 * @param stacks
	 *            The stacks to add
	 * @return The item stack set
	 */
	public static ItemStackSet create(ItemStack... stacks) {
		return new ItemStackSetImpl(stacks);
	}

	/**
	 * Returns an item stack set containing the provided stacks
	 * 
	 * @param isNBTSensitive
	 *            If true, the set will be NBT sensitive, if false, not
	 * @param stacks
	 *            The stacks to add
	 * @return The item stack set
	 */
	public static ItemStackSet create(boolean isNBTSensitive, ItemStack... stacks) {
		return new ItemStackSetImpl(isNBTSensitive, stacks);
	}

	/**
	 * Returns an NBT insensitive item stack set containing the provided stacks
	 * 
	 * @param stacks
	 *            The stacks to add
	 * @return The item stack set
	 */
	public static ItemStackSet create(Collection<? extends ItemStack> stacks) {
		return new ItemStackSetImpl(stacks);
	}

	/**
	 * Returns an item stack set containing the provided stacks
	 * 
	 * @param isNBTSensitive
	 *            If true, the set will be NBT sensitive, if false, not
	 * @param stacks
	 *            The stacks to add
	 * @return The item stack set
	 */
	public static ItemStackSet create(boolean isNBTSensitive, Collection<? extends ItemStack> stacks) {
		return new ItemStackSetImpl(isNBTSensitive, stacks);
	}
}
