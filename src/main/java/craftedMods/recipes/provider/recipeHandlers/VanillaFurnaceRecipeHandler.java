package craftedMods.recipes.provider.recipeHandlers;

import java.util.*;

import org.apache.logging.log4j.Logger;

import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.*;
import craftedMods.recipes.api.utils.RecipeHandlerRendererUtils.EnumProgressBarDirection;
import craftedMods.recipes.base.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

@RegisteredHandler
public class VanillaFurnaceRecipeHandler extends AbstractRecipeHandler<FurnanceRecipe> {

	private final VanillaFurnaceRecipeHandlerRenderer renderer = new VanillaFurnaceRecipeHandlerRenderer();
	private final VanillaFurnaceRecipeHandlerRecipeViewer recipeViewer = new VanillaFurnaceRecipeHandlerRecipeViewer(this);

	public VanillaFurnaceRecipeHandler() {
		super("vanilla.furnace");
	}

	@Override
	public String getDisplayName() {
		return Blocks.furnace.getLocalizedName();
	}

	@Override
	public void onPreLoad(RecipeHandlerConfiguration config, Logger logger) {
		super.onPreLoad(config, logger);
		if (RecipeHandlerUtils.getInstance().hasMineTweaker()) {
			this.logger.info("The Minetweaker API was detected - dynamic recipe loading will be enabled");
		}
		this.removeRecipeHandler("codechicken.nei.recipe.FurnaceRecipeHandler");
	}

	private void removeRecipeHandler(String recipeHandlerClass) {
		try {
			RecipeHandlerUtils.getInstance().removeNativeRecipeHandler(recipeHandlerClass);
		} catch (Exception e) {
			this.logger.error(String.format("Couldn't remove the native recipe handler \"%s\"", recipeHandlerClass));
		}
	}

	@Override
	public Collection<FurnanceRecipe> loadSimpleStaticRecipes() {
		Collection<FurnanceRecipe> ret = new ArrayList<>();
		if (!RecipeHandlerUtils.getInstance().hasMineTweaker()) {
			ret.addAll(this.loadRecipes());
		}
		return ret;
	}

	@Override
	public Collection<FurnanceRecipe> getDynamicCraftingRecipes(ItemStack result) {
		Collection<FurnanceRecipe> ret = new ArrayList<>();
		if (RecipeHandlerUtils.getInstance().hasMineTweaker()) {
			Collection<FurnanceRecipe> recipes = this.loadRecipes();
			for (FurnanceRecipe recipe : recipes)
				if (recipe.produces(result)) {
					ret.add(recipe);
				}
		}
		return ret;
	}

	@Override
	public Collection<FurnanceRecipe> getDynamicUsageRecipes(ItemStack ingredient) {
		Collection<FurnanceRecipe> ret = new ArrayList<>();
		if (RecipeHandlerUtils.getInstance().hasMineTweaker()) {
			Collection<FurnanceRecipe> recipes = this.loadRecipes();
			for (FurnanceRecipe recipe : recipes)
				if (recipe.consumes(ingredient)) {
					ret.add(recipe);
				}
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	protected Collection<FurnanceRecipe> loadRecipes() {
		Collection<FurnanceRecipe> ret = new ArrayList<>();
		((Map<ItemStack, ItemStack>) FurnaceRecipes.smelting().getSmeltingList()).forEach((ingredient, result) -> {
			ret.add(new FurnanceRecipe(ingredient, result));
		});
		return ret;
	}

	@Override
	public List<RecipeItemSlot> getSlotsForRecipeItems(FurnanceRecipe recipe, EnumRecipeItemRole role) {
		ArrayList<RecipeItemSlot> ret = new ArrayList<>();
		switch (role) {
			case INGREDIENT:
				ret.add(this.createRecipeItemSlot(51, 6));
				break;
			case OTHER:
				ret.add(this.createRecipeItemSlot(51, 42));
				break;
			case RESULT:
				ret.add(this.createRecipeItemSlot(111, 24));
				break;
		}
		return ret;
	}

	@Override
	@SuppressWarnings("unchecked")
	public VanillaFurnaceRecipeHandlerRenderer getRenderer() {
		return renderer;
	}

	@Override
	public RecipeHandlerRecipeViewer<FurnanceRecipe> getRecipeViewer() {
		return recipeViewer;
	}

	public class VanillaFurnaceRecipeHandlerRenderer implements RecipeHandlerRenderer<VanillaFurnaceRecipeHandler, FurnanceRecipe> {

		@Override
		public void renderBackground(VanillaFurnaceRecipeHandler handler, FurnanceRecipe recipe, int cycleticks) {
			RecipeHandlerRendererUtils.getInstance().bindTexture("textures/gui/container/furnace.png");
			RecipeHandlerRendererUtils.getInstance().drawTexturedRectangle(0, 0, 5, 11, 166, 65);
			RecipeHandlerRendererUtils.getInstance().drawRectangle(106, 19, 26, 26, 0xFFC6C6C6);
			RecipeHandlerRendererUtils.getInstance().drawTexturedRectangle(106, 13, 51, 6, 22, 28);
			RecipeHandlerRendererUtils.getInstance().drawProgressBar(74, 23, 176, 14, 24, 16, cycleticks % 48 / 48.0f, EnumProgressBarDirection.INCREASE_RIGHT);
			RecipeHandlerRendererUtils.getInstance().drawProgressBar(51, 25, 176, 0, 14, 14, cycleticks % 48 / 48.0f, EnumProgressBarDirection.DECREASE_DOWN);
		}

		@Override
		public void renderForeground(VanillaFurnaceRecipeHandler handler, FurnanceRecipe recipe, int cycleticks) {

		}

	}

	public class VanillaFurnaceRecipeHandlerRecipeViewer extends AbstractRecipeViewer<FurnanceRecipe, VanillaFurnaceRecipeHandler> {

		private final Collection<Class<? extends GuiContainer>> supportedGuiClasses = new ArrayList<>();

		public VanillaFurnaceRecipeHandlerRecipeViewer(VanillaFurnaceRecipeHandler handler) {
			super(handler);
			this.supportedGuiClasses.addAll(AbstractRecipeViewer.RECIPE_HANDLER_GUIS);
			this.supportedGuiClasses.add(GuiFurnace.class);
		}

		@Override
		public Collection<Class<? extends GuiContainer>> getSupportedGUIClasses() {
			return supportedGuiClasses;
		}

		@Override
		public Collection<FurnanceRecipe> getAllRecipes() {
			return RecipeHandlerUtils.getInstance().hasMineTweaker() ? this.handler.loadRecipes() : this.handler.getStaticRecipes();
		}

		@Override
		public int getOffsetX(Class<? extends GuiContainer> guiClass) {
			return guiClass == GuiFurnace.class ? 18 : 8;
		}

	}

}
