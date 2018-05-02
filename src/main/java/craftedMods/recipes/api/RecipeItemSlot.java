package craftedMods.recipes.api;

/**
 * A position on the recipe handler GUI where an item stack is rendered.
 * 
 * @author CraftedMods
 */
public interface RecipeItemSlot {

	/**
	 * @return The x coordinate of the position
	 */
	public int getX();

	/**
	 * @return The y coordinate of the position
	 */
	public int getY();

}
