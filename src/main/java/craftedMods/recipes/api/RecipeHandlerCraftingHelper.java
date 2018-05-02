package craftedMods.recipes.api;

import java.util.Collection;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

/**
 * The crafting helper coordinates the recipe overlay and the automatic placement of recipe
 * ingredients in the devices GUI.
 * 
 * @author CraftedMods
 * @param <T>
 *            The recipe type the crafting helper handles
 */
public interface RecipeHandlerCraftingHelper<T extends Recipe> {

	/**
	 * Returns a set of GUI classes which are supported for the overlay rendering or item stack
	 * placing. These GUI classes are the GUI classes of the crafting/etc. devices (to example
	 * GUICraftingTable).
	 * 
	 * @param recipe
	 *            The current displayed recipe
	 * @return A collection of supported GUI classes
	 */
	public Collection<Class<? extends GuiContainer>> getSupportedGUIClasses(T recipe);

	/**
	 * Returns the x offset for the recipe item slots.</br>
	 * The recipe handler displays a set of recipe item slots in NEI. To choose the right slots of
	 * the device's GUI, their position has to match the position of the slots in this GUI. Usually
	 * they've to be moved for this.
	 * 
	 * @param guiClass
	 *            The GUI class of the current device
	 * @param recipe
	 *            The current displayed recipe
	 * @return The x offset
	 */
	public int getOffsetX(Class<? extends GuiContainer> guiClass, T recipe);

	/**
	 * Returns the y offset for the recipe item slots.</br>
	 * The recipe handler displays a set of recipe item slots in NEI. To choose the right slots of
	 * the device's GUI, their position has to match the position of the slots in this GUI. Usually
	 * they've to be moved for this.
	 * 
	 * @param guiClass
	 *            The GUI class of the current device
	 * @param recipe
	 *            The current displayed recipe
	 * @return The y offset
	 */
	public int getOffsetY(Class<? extends GuiContainer> guiClass, T recipe);

	/**
	 * Returns whether an item stack in the inventory of the player matches a recipe ingredient
	 * 
	 * @param stack1
	 *            Stack one
	 * @param stack2
	 *            Stack two
	 * @return Whether the two stacks do match
	 */
	public boolean matches(ItemStack stack1, ItemStack stack2);

}
