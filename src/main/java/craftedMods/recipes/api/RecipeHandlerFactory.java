package craftedMods.recipes.api;

import java.util.Set;

/**
 * A handler which manually registers recipe handlers.</br>
 * This is necessary of you want to use the same recipe handler class for multiple devices - for
 * example for a ton of Crafting Tables (like the LOTR Mod has) - or if you want to reuse your
 * handler class. So instead of letting the provider to create one instance per handler class you
 * can create as much instances as you need using a factory and register them by your own.</br>
 * To be loaded, the handler needs to be annotated with
 * {@link craftedMods.recipes.api.RegisteredHandler}
 * 
 * @author CraftedMods
 */
public interface RecipeHandlerFactory {

	/**
	 * A set of instances to register
	 * 
	 * @return The recipe handler instances
	 */
	public Set<RecipeHandler<?>> getRecipeHandlers();

}
