package craftedMods.recipes.api;

/**
 * A recipe handler renderer draws the GUI of a recipe handler. It's needed if you need more than
 * the default texture - to example text, animated progress bars and so on. The rendering can be
 * done with the functions in {@link craftedMods.recipes.api.utils.RecipeHandlerRendererUtils}}
 * 
 * @author CraftedMods
 * @param <T>
 *            The supported recipe handler type
 * @param <U>
 *            The supported recipe type
 */
public interface RecipeHandlerRenderer<T extends RecipeHandler<U>, U extends Recipe> {

	/**
	 * The default GUI texture. If a recipe handler has no renderer, this texture will be renderer
	 * as it's background.
	 */
	public static final String DEFAULT_GUI_TEXTURE = "textures/gui/container/crafting_table.png";

	/**
	 * Called if background components of the GUI should be rendered.
	 * 
	 * @param handler
	 *            The recipe handler
	 * @param recipe
	 *            The current renderer recipe
	 * @param cycleticks
	 *            The time the recipe handler is running in ticks
	 */
	public void renderBackground(T handler, U recipe, int cycleticks);

	/**
	 * Called if foreground components of the GUI should be rendered.
	 * 
	 * @param handler
	 *            The recipe handler
	 * @param recipe
	 *            The current renderer recipe
	 * @param cycleticks
	 *            The time the recipe handler is running in ticks
	 */
	public void renderForeground(T handler, U recipe, int cycleticks);

}
