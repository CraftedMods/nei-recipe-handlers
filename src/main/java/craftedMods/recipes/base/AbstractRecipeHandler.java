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
package craftedMods.recipes.base;

import java.io.InputStream;
import java.util.*;
import java.util.function.Supplier;

import org.apache.logging.log4j.Logger;

import craftedMods.recipes.api.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;

/**
 * A default implementation of some aspects of the RecipeHandler interface. Normally you should
 * extend this class if you want to create a new recipe handler instead of implementing the
 * interface directly.
 * 
 * @author CraftedMods
 * @param <T>
 *            The recipe type
 */
public abstract class AbstractRecipeHandler<T extends Recipe> implements RecipeHandler<T> {

	private final String unlocalizedName;
	protected final Collection<T> staticRecipes = new ArrayList<>();
	private boolean areStaticRecipesLoaded = false;
	protected Logger logger;
	protected RecipeHandlerConfiguration config;
	protected RecipeHandlerResourceLoader resourceLoader;
	protected Map<ResourceLocation, Supplier<InputStream>> loadedResources = new HashMap<>();

	protected AbstractRecipeHandler(String unlocalizedName) {
		this.unlocalizedName = unlocalizedName;
	}

	@Override
	public String getUnlocalizedName() {
		return this.unlocalizedName;
	}

	@Override
	public String getDisplayName() {
		return StatCollector.translateToLocal("neiRecipeHandlers.recipeHandler." + this.getUnlocalizedName() + ".name");
	}

	@Override
	public boolean areStaticRecipesLoaded() {
		return this.areStaticRecipesLoaded;
	}

	@Override
	public Collection<T> getStaticRecipes() {
		return this.staticRecipes;
	}

	@Override
	public Collection<T> getDynamicCraftingRecipes(ItemStack result) {
		return null;
	}

	@Override
	public Collection<T> getDynamicUsageRecipes(ItemStack ingredient) {
		return null;
	}

	@Override
	public void onPreLoad(RecipeHandlerConfiguration config, Logger logger) {
		this.config = config;
		this.logger = logger;
	}

	@Override
	public Collection<T> loadSimpleStaticRecipes() {
		return null;
	}

	@Override
	public T loadComplicatedStaticRecipe(ItemStack... stacks) {
		return null;
	}

	@Override
	public void onPostLoad(Collection<T> staticRecipes) {
		this.staticRecipes.addAll(staticRecipes);
		this.areStaticRecipesLoaded = true;
	}

	protected RecipeItemSlot createRecipeItemSlot(int x, int y) {
		return new RecipeItemSlotImpl(x, y);
	}

	@Override
	public void onUpdate(int cycleticks) {}

	@Override
	public RecipeHandlerCacheManager<T> getCacheManager() {
		return null;
	}

	@Override
	public int getRecipesPerPage() {
		return 2;
	}

	@Override
	public <V extends RecipeHandlerRenderer<W, T>, W extends RecipeHandler<T>> V getRenderer() {
		return null;
	}

	@Override
	public int getComplicatedStaticRecipeDepth() {
		return 0;
	}

	@Override
	public RecipeHandlerCraftingHelper<T> getCraftingHelper() {
		return null;
	}

}
