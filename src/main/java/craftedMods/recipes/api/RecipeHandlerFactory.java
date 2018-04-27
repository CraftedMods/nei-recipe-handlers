package craftedMods.recipes.api;

import java.util.Set;

public interface RecipeHandlerFactory {

	public Set<RecipeHandler<?>> getRecipeHandlers();

}
