package craftedMods.recipes.api;

import java.util.Collection;

import net.minecraft.item.ItemStack;

/**
 * A handler which allows you to hide items in NEI.</br>
 * Typically items of technical blocks will be hidden. </br>
 * To be loaded, the handler needs to be annotated with
 * {@link craftedMods.recipes.api.RegisteredHandler}
 * 
 * @author CraftedMods
 */
public interface ItemHidingHandler {

	/**
	 * A collection of item stack which will be hidden in NEI.
	 * 
	 * @return The items stacks
	 */
	public Collection<ItemStack> getHiddenStacks();

}
