package craftedMods.recipes.api;

import java.util.Map;

import net.minecraft.item.ItemStack;

/**
 * A handler which overrides the display name of an item stack in NEI.</br>
 * This is useful for items like portal block items, which usually don't have a localized name, but
 * are useful for users and thus won't be hidden. With this handle, you can give them a human
 * readable name. </br>
 * To be loaded, the handler needs to be annotated with
 * {@link craftedMods.recipes.api.RegisteredHandler}
 * 
 * @author CraftedMods
 */
public interface ItemOverrideHandler {

	/**
	 * A map containing the override names of specific item stacks
	 * 
	 * @return The override name map
	 */
	public Map<ItemStack, String> getItemOverrideNames();

}
