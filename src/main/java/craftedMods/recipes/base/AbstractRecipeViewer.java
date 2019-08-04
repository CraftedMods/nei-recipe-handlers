package craftedMods.recipes.base;

import java.util.*;

import codechicken.nei.recipe.*;
import craftedMods.recipes.api.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public abstract class AbstractRecipeViewer<T extends Recipe, U extends RecipeHandler<T>> implements RecipeHandlerRecipeViewer<T> {

	protected final U handler;
	public static final Collection<Class<? extends GuiContainer>> RECIPE_HANDLER_GUIS = Arrays.asList(GuiRecipe.class);

	public AbstractRecipeViewer(U handler) {
		this.handler = handler;
	}

	@Override
	public Collection<Class<? extends GuiContainer>> getSupportedGUIClasses() {
		return RECIPE_HANDLER_GUIS;
	}

	@Override
	public boolean isGuiContainerSupported(GuiContainer container) {
		return true;
	}

	@Override
	public int getOffsetX(Class<? extends GuiContainer> guiClass) {
		return 0;
	}

	@Override
	public int getOffsetY(Class<? extends GuiContainer> guiClass) {
		return 0;
	}

	@Override
	public ItemStack getButtonIcon(Class<? extends GuiContainer> guiClass) {
		return new ItemStack(Items.writable_book);
	}

	@Override
	public String getButtonTooltip(Class<? extends GuiContainer> guiClass) {
		return StatCollector.translateToLocal("neiRecipeHandlers.recipeViewer.defaultTooltip");
	}

}
