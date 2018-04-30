package craftedMods.recipes.api;

import craftedMods.recipes.utils.NEIRecipeHandlersUtils;

/**
 * A recipe item role is the "type" of a recipe item. </br>
 * To example a default recipe has ingredients ({@link EnumRecipeItemRole#INGREDIENT}) and results
 * ({@link EnumRecipeItemRole#RESULT}).
 * 
 * @author CraftedMods
 */
public enum EnumRecipeItemRole {

	RESULT, INGREDIENT, OTHER;

	/**
	 * Allows you to create custom recipe item roles.
	 * 
	 * @param name
	 *            The name of the role
	 * @return The role instance
	 */
	public static EnumRecipeItemRole createRecipeItemRole(String name) {
		return NEIRecipeHandlersUtils.createRecipeItemRole(name);
	}

}
