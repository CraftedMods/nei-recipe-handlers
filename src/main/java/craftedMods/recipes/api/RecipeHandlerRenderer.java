/*******************************************************************************
 * Copyright (C) 2018 CraftedMods (see https://github.com/CraftedMods)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
	 * The default GUI texture. If a recipe handler has no renderer, this texture will be rendered
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
