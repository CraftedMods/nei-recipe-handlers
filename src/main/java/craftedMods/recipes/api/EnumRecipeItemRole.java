package craftedMods.recipes.api;

import craftedMods.recipes.utils.NEIRecipeHandlersUtils;

public enum EnumRecipeItemRole {

	RESULT, INGREDIENT, OTHER;

	public static EnumRecipeItemRole createRecipeItemRole(String name) {
		return NEIRecipeHandlersUtils.createRecipeItemRole(name);
	}

}
