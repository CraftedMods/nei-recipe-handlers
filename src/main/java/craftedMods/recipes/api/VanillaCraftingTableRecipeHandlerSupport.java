package craftedMods.recipes.api;

import org.apache.commons.lang3.tuple.Pair;

import craftedMods.recipes.base.AbstractRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

/**
 * An support handler for the vanilla CT handler. The handlers extend the functionality of the vanilla CT handler.<br>
 * To be loaded, the handler needs to be annotated with {@link craftedMods.recipes.api.RegisteredHandler}
 * 
 * @author CraftedMods
 */
public interface VanillaCraftingTableRecipeHandlerSupport {

	/**
	 * Invoked if a recipe type was found which couldn'd be processed. Either return the processed recipe instance or null, which means that the
	 * recipe will be sent to the other handlers, which try to process it. The second argument of the pair is whether the recipe should be ignored,
	 * meaning that it won't be sent to other handlers, but also that no message will be logged. The returned recipe instance of not relevant then.
	 * Usually used if the recipe is computed some other way. If null is returned, the same thing happens as if the processed instance is null and the
	 * second argument is false.
	 * 
	 * @param recipe The "undefined" recipe instance
	 * @return The processed recipe instance or null and whether the recipe should be ignored
	 */
	public Pair<AbstractRecipe, Boolean> undefinedRecipeTypeFound(IRecipe recipe);

	/**
	 * Returns the complicated static recipe depth for this support handler. The maximum of the depths of all support handlers for the vanilla
	 * crafting table recipe handler will be used as the complicated static recipe loading depth of the vanilla CT handler.
	 * 
	 * @return The complicated static recipe depth for this support handler
	 */
	public default int getComplicatedStaticRecipeDepth() {
		return 0;
	}

	/**
	 * A delegate for {@link RecipeHandler#loadComplicatedStaticRecipe(ItemStack...)} for the vanilla CT handler.
	 * 
	 * @param stacks The stacks to process
	 * @return The returned recipe or null
	 */
	public default AbstractRecipe loadComplicatedStaticRecipe(ItemStack... stacks) {
		return null;
	}

	/**
	 * Used to extend {@link RecipeHandlerCraftingHelper#matches(ItemStack, ItemStack)} of the crafting helper of the vanilla CT handler. The function
	 * will be invoked for every support handler, and it has to be true for every one called so that the function in the crafting helper returns true.
	 * 
	 * @param stack1 The first stack
	 * @param stack2 The second stack
	 * @return Whether the stacks do match
	 */
	public default boolean matches(ItemStack stack1, ItemStack stack2) {
		return true;
	}

}