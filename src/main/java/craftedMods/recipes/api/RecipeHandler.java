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

import java.util.*;

import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemStack;

/**
 * A recipe handler is the core part of the API. It loads, registers and manages the recipes that
 * will be displayed with NEI (and it also manages the way these actions will be done).</br>
 * To be loaded, the handler needs to be annotated with
 * {@link craftedMods.recipes.api.RegisteredHandler}
 * 
 * @author CraftedMods
 * @param <T>
 *            The recipe type the handler processes
 * @param The
 *            type of cached recipes the recipe handler produces/accepts
 */
public interface RecipeHandler<T extends Recipe> {

	/**
	 * The unlocalized name of the recipe handler. It must be unique, and will be used like an ID.
	 * Use dots in the name to group the recipe handlers hierarchically.
	 *
	 * @return The unlocalized name
	 */
	public String getUnlocalizedName();

	/**
	 * The localized display name of the recipe handler. Usually it's the name of the device where
	 * the recipe will be processed (Crafting Table, Alloy Forge, ...).
	 *
	 * @return The localized display name
	 */
	public String getDisplayName();

	/**
	 * @return Whether all static recipes for this handler were loaded
	 */
	public boolean areStaticRecipesLoaded();

	/**
	 * Returns a set of all pre-computed ("static") recipes of this handler. The set returned may be
	 * null.
	 *
	 * @return A set of static recipes
	 */
	public Collection<T> getStaticRecipes();

	/**
	 * Called every time the player queries the crafting recipes (recipes where the item is a
	 * result) for an item in NEI. The set returned may be null. (Simple)Static recipe loading
	 * should be preferred if possible.
	 *
	 * @param result
	 *            The result item
	 * @return The crafting recipes with the result item as result
	 */
	public Collection<T> getDynamicCraftingRecipes(ItemStack result);

	/**
	 * Called every time the player queries the usage recipes (recipes where the item is an
	 * ingredient) for an item in NEI. The Set returned may be null. (Simple) Static recipe loading
	 * should be preferred if possible.
	 *
	 * @param ingredient
	 *            The ingredient item
	 * @return The crafting recipes with the ingredient item as ingredient
	 */
	public Collection<T> getDynamicUsageRecipes(ItemStack ingredient);

	/**
	 * Called if the recipe handler is loaded, but before the static recipes are loaded. All
	 * initializations should be done here, not in a constructor.
	 *
	 * @param config
	 *            An object that allows the recipe handler to access it's own configuration file
	 *            section
	 * @param logger
	 *            The logger assigned to this recipe handler
	 */
	public void onPreLoad(RecipeHandlerConfiguration config, Logger logger);

	/**
	 * Called when the recipe handler is loaded; at this time all static recipes were loaded.
	 *
	 * @param staticRecipes
	 *            The static recipes that were loaded for this handler. They've to be returned by
	 *            getStaticRecipes()
	 */
	public void onPostLoad(Collection<T> staticRecipes);

	/**
	 * Loads all static recipes that don't need any external dependencies, to example a list of all
	 * items. The result may be null.
	 *
	 * @return a set of simple static recipes
	 */
	public Collection<T> loadSimpleStaticRecipes();

	/**
	 * The count of item stacks in a combination for loadComplicatedStaticRecipe(). If less equal
	 * zero, loadComplicatedStaticRecipe() won't be called.
	 *
	 * @return the depth of complicated static recipes
	 */
	public int getComplicatedStaticRecipeDepth();

	/**
	 * Loads a static recipe that needs a combination of item stacks, to example an alloy forge
	 * recipe. Something like this is necessary if a mod doesn't provide an IRecipe instance but a
	 * function like canSmelt(ItemStack ingredient, ItemStack alloyItem) that determines the recipe
	 * dynamically. A collection of all possible item combinations has to be created which will be
	 * sent to this function, which returns a cached recipe if the input matches with a "recipe".
	 * The count of item stacks in a combination (to example two for the method above) is specified
	 * in getComplicatedStaticRecipeDepth(). Please note that itemcount^staticRecipeDepth iterations
	 * through all items are necessary to test all possible combinations, so this can be very
	 * expensive. If possible, avoid this method of loading static recipes. This method can be
	 * called concurrently from multiple threads. You can use caching so these computations don't
	 * have to be done every time the game is loaded.
	 *
	 * @param stacks
	 *            An array containing a combination of getComplicatedStaticRecipeDepth() item stacks
	 * @return a set of one complicated static recipe or null if no recipe matches
	 */
	public T loadComplicatedStaticRecipe(ItemStack... stacks);

	/**
	 * Returns the slots (positions on the screen) by their role where the items assigned to a
	 * specific recipe will be displayed.
	 *
	 * @param recipe
	 *            The recipe
	 * @param role
	 *            Whether the slots for ingredient, result, or other items are required
	 * @return A list containing the slots
	 */
	public List<RecipeItemSlot> getSlotsForRecipeItems(T recipe, EnumRecipeItemRole role);
	// TODO: Why a List and why can the slots be null?

	/**
	 * Returns the renderer of the recipe handler. The renderer draws the GUI of the recipe handler.
	 * If you need special features - text, custom textures or things like that, you'll need a
	 * renderer. It can be null - then a default texture will be displayed.
	 * 
	 * @return A renderer instance
	 */
	public <V extends RecipeHandlerRenderer<W, T>, W extends RecipeHandler<T>> V getRenderer();

	/**
	 * Returns the count of recipes displayed in the GUI per page.
	 *
	 * @return The count of recipes per page
	 */
	public int getRecipesPerPage();

	/**
	 * This function will be called every tick the recipe handler is updated.
	 * 
	 * @param cycleticks
	 *            The time in ticks the recipe handler is/was active
	 */
	public void onUpdate(int cycleticks);

	/**
	 * Returns the cache manager. If you don't want to do expensive computations (to example
	 * complicated static recipe loading) every time the game starts if nothing did change, you can
	 * use the cache manager to store some computed recipes. As long as the cache is valid, the
	 * saved recipes will be used and the expensive computation can be skipped. </br>
	 * If null, no caching will be used.
	 * 
	 * @return A cache manager instance
	 */
	public RecipeHandlerCacheManager<T> getCacheManager();

	/**
	 * Returns the crafting helper. NEI has a feature which allows the user to automatically move
	 * the ingredients from his inventory to the GUI of the current (supported) device (to example a
	 * Crafting Table) where they'll be placed in the appropriate slots (based on the data of the
	 * current recipe). Furthermore it allows the user to render a recipe overlay in the device's
	 * GUI. To coordinate this, a crafting helper is needed. </br>
	 * If null, this feature won't be supported.
	 * 
	 * @return A crafting helper instance
	 */
	public RecipeHandlerCraftingHelper<T> getCraftingHelper();

}
