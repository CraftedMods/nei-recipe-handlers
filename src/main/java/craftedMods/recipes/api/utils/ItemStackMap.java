package craftedMods.recipes.api.utils;

import java.util.Map;

import craftedMods.recipes.utils.ItemStackMapImpl;
import net.minecraft.item.ItemStack;

/**
 * ItemStack hasn't an equals and hashCode implementation which means that it won't work well with
 * maps. A ItemStackMap is a map where ItemStacks can be used as keys - just like a normal map. But
 * instead of normal maps this will work well with ItemStacks.
 * 
 * @author CraftedMods
 * @param <V>
 *            The value type of the map
 */
public interface ItemStackMap<V> extends Map<ItemStack, V> {

	/**
	 * Creates a NBT insensitive item stack map
	 * 
	 * @return The item stack map
	 */
	public static <T> ItemStackMap<T> create() {
		return new ItemStackMapImpl<>();
	}

	/**
	 * Creates an item stack map which can be configured to be NBT sensitive
	 * 
	 * @param isNBTSensitive
	 *            If true, the returned map will be NBT sensitive, if false, not
	 * @return The item stack map
	 */
	public static <T> ItemStackMap<T> create(boolean isNBTSensitive) {
		return new ItemStackMapImpl<>(isNBTSensitive);
	}

}
