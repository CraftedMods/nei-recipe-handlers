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

import java.util.*;

import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.*;
import net.minecraft.item.ItemStack;

/**
 * A base class for recipes. Usually {@link craftedMods.recipes.base.ShapedRecipe} or
 * {@link craftedMods.recipes.base.ShapelessRecipe} are better choices - unless you need custom behaviour.
 * @author CraftedMods
 *
 */
public abstract class AbstractRecipe implements Recipe {

	protected final List<ItemStackSet> ingredients = new ArrayList<>();
	protected final List<ItemStackSet> results = new ArrayList<>();
	protected final List<ItemStackSet> others = new ArrayList<>();

	private boolean werePermutationsGenerated = false;

	protected AbstractRecipe(Collection<ItemStack> ingredients, Collection<ItemStack> results, Collection<ItemStack> otherStacks) {
		this.addAll(ingredients, this.ingredients);
		this.addAll(results, this.results);
		this.addAll(otherStacks, this.others);
		this.generatePermutations();
	}

	protected AbstractRecipe() {}

	public boolean werePermutationsGenerated() {
		return this.werePermutationsGenerated;
	}

	@Override
	public List<ItemStackSet> getRecipeItems(EnumRecipeItemRole role) {
		switch (role) {
			case INGREDIENT:
				return this.ingredients;
			case OTHER:
				return this.others;
			case RESULT:
				return this.results;
			default:
				throw new IllegalArgumentException("Cannot handle the recipe item role \"" + role + "\"");
		}
	}

	protected void addAll(Collection<ItemStack> src, List<ItemStackSet> dest) {
		if (src != null) {
			for (ItemStack stack : src) {
				this.add(stack, dest);
			}
		}
	}

	protected void addAll(ItemStack[] src, List<ItemStackSet> dest) {
		if (src != null) {
			for (ItemStack stack : src) {
				this.add(stack, dest);
			}
		}
	}

	protected void add(ItemStack stack, List<ItemStackSet> dest) {
		dest.add(this.createItemStackSet(stack));
	}

	protected ItemStackSet createItemStackSet(ItemStack... stacks) {
		return ItemStackSet.create(stacks);
	}

	@Override
	public boolean produces(ItemStack result) {
		for (ItemStackSet permutations : this.results)
			if (permutations != null) {
				for (ItemStack permutation : permutations)
					if (RecipeHandlerUtils.getInstance().areStacksSameTypeForCrafting(permutation, result)) return true;
			}
		return false;
	}

	@Override
	public boolean consumes(ItemStack ingredient) {
		for (ItemStackSet permutations : this.ingredients)
			if (permutations != null) {
				for (ItemStack permutation : permutations)
					if (RecipeHandlerUtils.getInstance().areStacksSameTypeForCrafting(permutation, ingredient)) return true;
			}
		return false;
	}

	@Override
	public ItemStack getIngredientReplacement(ItemStack defaultReplacement) {
		return defaultReplacement;
	}

	@Override
	public ItemStack getResultReplacement(ItemStack defaultReplacement) {
		return null;
	}

	protected boolean generatePermutations() {
		if (!this.werePermutationsGenerated) {
			this.generatePermutationsForStacks(this.ingredients);
			this.generatePermutationsForStacks(this.results);
			this.generatePermutationsForStacks(this.others);
			return this.werePermutationsGenerated = true;
		}
		return false;
	}

	protected void generatePermutationsForStacks(List<ItemStackSet> stacks) {
		ListIterator<ItemStackSet> permutationsIterator = stacks.listIterator();
		while (permutationsIterator.hasNext()) {
			ItemStackSet permutations = permutationsIterator.next();
			if (permutations != null) {
				permutationsIterator.set(RecipeHandlerUtils.getInstance().generatePermutations(permutations));
			}
		}
	}

}
