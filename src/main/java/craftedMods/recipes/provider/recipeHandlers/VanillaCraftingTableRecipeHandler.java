package craftedMods.recipes.provider.recipeHandlers;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;

import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.RecipeHandlerUtils;
import craftedMods.recipes.base.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.StatCollector;

public class VanillaCraftingTableRecipeHandler extends CraftingGridRecipeHandler {

	private final Collection<VanillaCraftingTableRecipeHandlerSupport> supportHandlers = new ArrayList<>();
	private final VanillaCraftingTableRecipeHandlerCraftingHelper craftingHelper = new VanillaCraftingTableRecipeHandlerCraftingHelper();

	public VanillaCraftingTableRecipeHandler(Collection<VanillaCraftingTableRecipeHandlerSupport> supportHandlers) {
		super("vanillaCrafting");
		this.supportHandlers.addAll(supportHandlers);
	}

	@Override
	public String getDisplayName() {
		return StatCollector.translateToLocal("neiRecipeHandlers.handler.vanillaCrafting.name");
	}

	@Override
	public void onPreLoad(RecipeHandlerConfiguration config, Logger logger) {
		super.onPreLoad(config, logger);
		if (RecipeHandlerUtils.getInstance().hasMineTweaker()) {
			this.logger.info("The Minetweaker API was detected - dynamic recipe loading will be enabled");
		}
		this.removeRecipeHandler("codechicken.nei.recipe.ShapedRecipeHandler");
		this.removeRecipeHandler("codechicken.nei.recipe.ShapelessRecipeHandler");
	}

	private void removeRecipeHandler(String recipeHandlerClass) {
		try {
			RecipeHandlerUtils.getInstance().removeNativeRecipeHandler(recipeHandlerClass);
		} catch (Exception e) {
			this.logger.error(String.format("Couldn't remove the native recipe handler \"%s\"", recipeHandlerClass));
		}
	}

	@Override
	public Collection<AbstractRecipe> loadSimpleStaticRecipes() {
		return RecipeHandlerUtils.getInstance().hasMineTweaker() ? null : super.loadSimpleStaticRecipes();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Collection<AbstractRecipe> loadRecipes() {
		this.recipes.clear();
		this.recipes.addAll(CraftingManager.getInstance().getRecipeList());
		Collection<AbstractRecipe> ret = super.loadRecipes();
		this.logUndefinedRecipeTypes = false;
		return ret;
	}

	@Override
	protected void undefinedRecipeTypeFound(IRecipe recipe, Collection<AbstractRecipe> container) {
		for (VanillaCraftingTableRecipeHandlerSupport supportHandler : supportHandlers) {
			Pair<AbstractRecipe, Boolean> result = supportHandler.undefinedRecipeTypeFound(recipe);
			if (result != null) {
				if (result.getRight()) return;
				if (result.getLeft() != null) {
					container.add(result.getLeft());
					return;
				}
			}
		}
		super.undefinedRecipeTypeFound(recipe, container);
	}

	@Override
	public Collection<AbstractRecipe> getDynamicCraftingRecipes(ItemStack result) {
		Collection<AbstractRecipe> ret = new ArrayList<>();
		if (RecipeHandlerUtils.getInstance().hasMineTweaker()) {
			Collection<AbstractRecipe> recipes = this.loadRecipes();
			for (AbstractRecipe recipe : recipes)
				if (recipe.produces(result)) {
					ret.add(recipe);
				}
		}
		return ret;
	}

	@Override
	public Collection<AbstractRecipe> getDynamicUsageRecipes(ItemStack ingredient) {
		Collection<AbstractRecipe> ret = new ArrayList<>();
		if (RecipeHandlerUtils.getInstance().hasMineTweaker()) {
			Collection<AbstractRecipe> recipes = this.loadRecipes();
			for (AbstractRecipe recipe : recipes)
				if (recipe.consumes(ingredient)) {
					ret.add(recipe);
				}
		}
		return ret;
	}

	@Override
	public RecipeHandlerCraftingHelper<AbstractRecipe> getCraftingHelper() {
		return this.craftingHelper;
	}

	@Override
	public AbstractRecipe loadComplicatedStaticRecipe(ItemStack... stacks) {
		for (VanillaCraftingTableRecipeHandlerSupport supportHandler : supportHandlers) {
			AbstractRecipe recipe = supportHandler.loadComplicatedStaticRecipe(stacks);
			if (recipe != null) return recipe;
		}
		return null;
	}

	@Override
	public int getComplicatedStaticRecipeDepth() {
		return supportHandlers.parallelStream().map(handler -> handler.getComplicatedStaticRecipeDepth()).collect(Collectors.maxBy(Comparator.naturalOrder()))
				.orElse(0);
	}

	public class VanillaCraftingTableRecipeHandlerCraftingHelper extends AbstractCraftingHelper<AbstractRecipe> {

		@Override
		public Collection<Class<? extends GuiContainer>> getSupportedGUIClasses(AbstractRecipe recipe) {
			return this.isRecipe2x2(recipe) ? Arrays.asList(GuiInventory.class, GuiCrafting.class) : Arrays.asList(GuiCrafting.class);
		}

		@Override
		public int getOffsetX(Class<? extends GuiContainer> guiClass, AbstractRecipe recipe) {
			return guiClass == GuiInventory.class ? 63 : 5;
		}

		@Override
		public int getOffsetY(Class<? extends GuiContainer> guiClass, AbstractRecipe recipe) {
			return guiClass == GuiInventory.class ? 20 : 11;
		}

		@Override
		public boolean matches(ItemStack stack1, ItemStack stack2) {
			return super.matches(stack1, stack2)
					&& supportHandlers.parallelStream().map(handler -> handler.matches(stack1, stack2)).reduce(Boolean::logicalAnd).orElse(true);
		}

		private boolean isRecipe2x2(AbstractRecipe recipe) {
			boolean ret = recipe.getRecipeItems(EnumRecipeItemRole.INGREDIENT).size() <= 4;
			if (recipe instanceof ShapedRecipe) {
				ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
				ret = shapedRecipe.getWidth() <= 2 && shapedRecipe.getHeight() <= 2;
			}
			return ret;
		}
	}
}
