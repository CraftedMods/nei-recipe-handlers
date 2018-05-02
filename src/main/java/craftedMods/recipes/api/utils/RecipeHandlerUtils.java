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
package craftedMods.recipes.api.utils;

import java.util.*;

import craftedMods.recipes.api.RecipeItemSlot;
import craftedMods.recipes.utils.RecipeHandlerUtilsImpl;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * A collection of useful methods for recipe handlers
 * 
 * @author CraftedMods
 */
public interface RecipeHandlerUtils {

	public static final RecipeHandlerUtils instance = new RecipeHandlerUtilsImpl();

	/**
	 * @return An instance of the utility class
	 */
	public static RecipeHandlerUtils getInstance() {
		return RecipeHandlerUtils.instance;
	}

	/**
	 * Returns a set containing all permutations of all provided stacks.</br>
	 * Permutations are all stacks, which are equal for crafting.
	 * 
	 * @param stacks The initial stack list
	 * @return A set containing all permutations
	 */
	public ItemStackSet generatePermutations(ItemStack... stacks);

	/**
	 * Returns a set containing all permutations of all provided stacks.</br>
	 * Permutations of a stack are all stacks, which are equal for crafting.
	 * 
	 * @param stacks The initial stack list
	 * @return A set containing all permutations
	 */
	public ItemStackSet generatePermutations(Collection<ItemStack> stacks);

	/**
	 * Returns if the stacks are of the same type.</br>
	 * This doesn't mean that these stacks would match in a crafting recipe.
	 * 
	 * @param stack1 The first stack
	 * @param stack2 The second stack
	 * @return Whether the stacks match
	 */
	public boolean areStacksSameType(ItemStack stack1, ItemStack stack2);

	/**
	 * Returns whether the stacks are the same type for crafting.</br>
	 * This means that if one stack would be specified as a recipe ingredient, the other stack could be used as an ingredient for this recipe.
	 * 
	 * @param stack1 The first stack
	 * @param stack2 The second stack
	 * @return Whether the stacks match
	 */
	public boolean areStacksSameTypeForCrafting(ItemStack stack1, ItemStack stack2);

	/**
	 * Returns all item stacks that the provided object contains.
	 * 
	 * @param container The object from which the stacks will be extracted
	 * @return The extracted stacks
	 */
	public ItemStack[] extractRecipeItems(Object container);

	/**
	 * Returns a list containing all registered items.</br>
	 * Only call this of the list was initialized.
	 * 
	 * @return All registered items
	 */
	public List<ItemStack> getItemList();

	/**
	 * Reads an item stack list from a tag of the specified compound with the specified name.
	 * 
	 * @param compound The tag compound
	 * @param tagName The name of the tag containing the list
	 * @return A collection containing the stacks
	 */
	public Collection<ItemStack> readItemStackListFromNBT(NBTTagCompound compound, String tagName);

	/**
	 * Writes the provided item stack list to a tag in the specified compound with the specified name
	 * 
	 * @param compound The tag compound
	 * @param tagName The tag under which the list will be stored
	 * @param stacks The stack list
	 */
	public void writeItemStackListToNBT(NBTTagCompound compound, String tagName, Collection<? extends ItemStack> stacks);

	/**
	 * Offsets the recipe item slots in the list by the provided offset
	 * 
	 * @param slotsList The original slot lost
	 * @param xOffset The x offset
	 * @param yOffset The y offset
	 * @return The moved item stack slots
	 */
	public List<RecipeItemSlot> offset(List<RecipeItemSlot> slotsList, int xOffset, int yOffset);

	/**
	 * Forces a refresh of the recipe cache
	 */
	public void forceRecipeCacheRefresh();

	/**
	 * @return The resource domain of the provider (for custom resource locations)
	 */
	public String getResourceDomain();

	/**
	 * @return A set containing all registered fuel items
	 */
	public ItemStackSet getFuels();

	/**
	 * Allows you to remove "native" recipe handlers from NEI.
	 * 
	 * @param recipeHandlerClass The full class name of the recipe handler to remove
	 * @throws ClassNotFoundException If the provided class wasn't found
	 */
	public void removeNativeRecipeHandler(String recipeHandlerClass) throws ClassNotFoundException;

	/**
	 * Writes an item stack to the specified compound.</br>
	 * Instead of the item name it's registry name will be saved - this allows more compatibility.
	 * 
	 * @param stack The item stack to save
	 * @param compound The compound
	 */
	public void writeItemStackToNBT(ItemStack stack, NBTTagCompound compound);

	/**
	 * Reads an item stack from the specified compound.</br>
	 * The stack has to be saved with {@link craftedMods.recipes.api.utils.RecipeHandlerUtils#writeItemStackToNBT(ItemStack, NBTTagCompound)}.</br>
	 * Returns null of no stack could be loaded.
	 * 
	 * @param compound The compound which contains the saved stack data
	 * @return The loaded item stack or null
	 */
	public ItemStack readItemStackFromNBT(NBTTagCompound compound);

}
