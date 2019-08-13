/*******************************************************************************
 * Copyright (C) 2019 CraftedMods (see https://github.com/CraftedMods)
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

import java.awt.Rectangle;
import java.util.Collection;

import codechicken.nei.recipe.GuiRecipe;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

/**
 * This handler includes logic which allows the provider to render a button into the recipe handler and optionally the GUIs of supported devices,
 * which shows, when clicked, all recipes that device supports.
 * 
 * @author CraftedMods
 * @param <T> The type of recipe the supported handler handles
 */
public interface RecipeHandlerRecipeViewer<T extends Recipe> {

	/**
	 * The default rectangle in which the button is defined.
	 */
	public static final Rectangle VIEW_ALL_RECIPES_RECTANGLE = new Rectangle(3, 24, 16, 16);// 7, 26, 12, 12

	/**
	 * Returns a collection containing all recipes of the supported handler. Return an empty collection or null to disable the button.
	 * 
	 * @return A collection with all supported recipes of the handler
	 */
	public Collection<T> getAllRecipes();

	/**
	 * Returns a set of GUI classes which are supported for the button. These GUI classes are the GUI classes of the crafting/etc. devices (to example
	 * GUICraftingTable). Return null or an empty collection to support no GUI classes. Use {@link GuiRecipe} to support the button in the recipe
	 * handler GUIs.
	 * 
	 * @param recipe The current displayed recipe
	 * @return A collection of supported GUI classes
	 */
	public Collection<Class<? extends GuiContainer>> getSupportedGUIClasses();

	/**
	 * Returns whether the specified {@link GuiContainer} instance is supported. This is necessary if several devices share the same GUI class, so
	 * only the class doesn't suffice to distinguish them. This method will only be invoked for GUI container instances which class is supported, see
	 * {@link RecipeHandlerRecipeViewer#getSupportedGUIClasses()}.
	 * 
	 * @param container The instance to check
	 * @return Whether it's supported
	 */
	public boolean isGuiContainerSupported(GuiContainer container);

	/**
	 * Returns the offset added to the x component of {@link RecipeHandlerRecipeViewer#VIEW_ALL_RECIPES_RECTANGLE} based on the current GUI class.
	 * 
	 * @param guiClass The class of the current GUI screen
	 * @return The x offset
	 */
	public int getOffsetX(Class<? extends GuiContainer> guiClass);

	/**
	 * Returns the offset added to the y component of {@link RecipeHandlerRecipeViewer#VIEW_ALL_RECIPES_RECTANGLE} based on the current GUI class.
	 * 
	 * @param guiClass The class of the current GUI screen
	 * @return The y offset
	 */
	public int getOffsetY(Class<? extends GuiContainer> guiClass);

	/**
	 * Returns the item which is rendered as the icon of the button. It mustn't be null!
	 * 
	 * @param guiClass The class of the GUI the button is rendered into
	 * @return The button icon item
	 */
	public ItemStack getButtonIcon(Class<? extends GuiContainer> guiClass);

	/**
	 * Returns the tooltip rendered when the user hovers with the mouse above the button. Mustn't be null!
	 * 
	 * @param guiClass The current GUI class
	 * @return The final tooltip text
	 */
	public String getButtonTooltip(Class<? extends GuiContainer> guiClass);

}
