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
package craftedMods.recipes.provider;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Triple;
import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.guihook.*;
import codechicken.nei.recipe.*;
import craftedMods.recipes.api.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class NEIRecipeHandlersTransferRectManager implements IContainerInputHandler, IContainerTooltipHandler, IContainerDrawHandler {

	static {
		GuiContainerManager.addInputHandler(new NEIRecipeHandlersTransferRectManager());
		GuiContainerManager.addTooltipHandler(new NEIRecipeHandlersTransferRectManager());
		GuiContainerManager.addDrawHandler(new NEIRecipeHandlersTransferRectManager());
	}

	private static HashMap<Class<? extends GuiContainer>, HashMap<RecipeHandler<?>, TransferRect>> guiMap = new HashMap<>();

	public static void registerHandler(RecipeHandler<?> handler) {
		if (handler.getRecipeViewer() != null) {
			RecipeHandlerRecipeViewer<?> recipeViewer = handler.getRecipeViewer();
			if (recipeViewer.getSupportedGUIClasses() != null) {
				Collection<Class<? extends GuiContainer>> guiClasses = new ArrayList<>(recipeViewer.getSupportedGUIClasses());

				if (guiClasses.contains(GuiRecipe.class)) {
					guiClasses.remove(GuiRecipe.class);
					guiClasses.add(GuiCraftingRecipe.class);
					guiClasses.add(GuiUsageRecipe.class);
				}

				for (Class<? extends GuiContainer> guiClass : guiClasses) {
					Rectangle area = new Rectangle(RecipeHandlerRecipeViewer.VIEW_ALL_RECIPES_RECTANGLE);
					area.translate(recipeViewer.getOffsetX(guiClass), recipeViewer.getOffsetY(guiClass));

					if (!NEIRecipeHandlersTransferRectManager.guiMap.containsKey(guiClass)) {
						NEIRecipeHandlersTransferRectManager.guiMap.put(guiClass, new HashMap<>());
					}

					NEIRecipeHandlersTransferRectManager.guiMap.get(guiClass).put(handler,
							new TransferRect(area, NEIRecipeHandlersTransferRectManager.getViewAllRecipedIdentifier(handler), new Object[0]));
				}
			}
		}

	}

	public static String getViewAllRecipedIdentifier(RecipeHandler<?> handler) {
		return "all_" + handler.getUnlocalizedName();
	}

	public boolean canHandle(GuiContainer gui) {
		return NEIRecipeHandlersTransferRectManager.guiMap.containsKey(gui.getClass()) && NEIRecipeHandlersTransferRectManager.guiMap.get(gui.getClass())
				.keySet().stream().anyMatch(handler -> handler.getRecipeViewer().isGuiContainerSupported(gui));
	}

	@Override
	public boolean lastKeyTyped(GuiContainer gui, char keyChar, int keyCode) {
		if (!this.canHandle(gui)) return false;

		if (gui instanceof GuiRecipe) {
			GuiRecipe guiRecipe = (GuiRecipe) gui;

			Boolean res = this.executeForGuiRecipe(guiRecipe, null, (triple) -> {
				Point offset = guiRecipe.getRecipePosition(triple.getLeft());

				boolean result = false;

				if (keyCode == NEIClientConfig.getKeyBinding("gui.recipe")) {
					result = this.transferRect(gui, triple.getRight(), offset.x, offset.y, false);
				}
				if (keyCode == NEIClientConfig.getKeyBinding("gui.usage")) {
					result = this.transferRect(gui, triple.getRight(), offset.x, offset.y, true);
				}

				if (result) return true;
				return null;
			}, false);

			return res == Boolean.TRUE ? true : false;

		} else {
			int[] offset = RecipeInfo.getGuiOffset(gui);
			if (keyCode == NEIClientConfig.getKeyBinding("gui.recipe")) return this.transferRect(gui, null, offset[0], offset[1], false);
			if (keyCode == NEIClientConfig.getKeyBinding("gui.usage")) return this.transferRect(gui, null, offset[0], offset[1], true);
		}

		return false;
	}

	@Override
	public boolean mouseClicked(GuiContainer gui, int mousex, int mousey, int button) {
		if (!this.canHandle(gui)) return false;

		if (gui instanceof GuiRecipe) {
			GuiRecipe guiRecipe = (GuiRecipe) gui;

			Boolean res = this.executeForGuiRecipe(guiRecipe, null, (triple) -> {
				Point offset = guiRecipe.getRecipePosition(triple.getLeft());

				boolean result = false;

				if (button == 0) {
					result = this.transferRect(gui, triple.getRight(), offset.x, offset.y, false);
				}
				if (button == 1) {
					result = this.transferRect(gui, triple.getRight(), offset.x, offset.y, true);
				}

				if (result) return true;

				return null;
			}, false);

			return res == Boolean.TRUE ? true : false;

		} else {
			int[] offset = RecipeInfo.getGuiOffset(gui);
			if (button == 0) return this.transferRect(gui, null, offset[0], offset[1], false);
			if (button == 1) return this.transferRect(gui, null, offset[0], offset[1], true);
		}

		return false;
	}

	private boolean transferRect(GuiContainer gui, RecipeHandler<?> currentHandler, int offsetx, int offsety, boolean usage) {
		Map<RecipeHandler<?>, TransferRect> transferRects = NEIRecipeHandlersTransferRectManager.guiMap.get(gui.getClass());
		Point pos = GuiDraw.getMousePosition();
		Point relMouse = new Point(pos.x - gui.guiLeft - offsetx, pos.y - gui.guiTop - offsety);
		for (Map.Entry<RecipeHandler<?>, TransferRect> entry : transferRects.entrySet()) {
			if (entry.getKey().getRecipeViewer().isGuiContainerSupported(gui) && (currentHandler != null ? currentHandler == entry.getKey() : true)) {
				TransferRect rect = entry.getValue();
				if (!rect.rect.contains(relMouse) || !(usage ? GuiUsageRecipe.openRecipeGui(rect.outputId, rect.results)
						: GuiCraftingRecipe.openRecipeGui(rect.outputId, rect.results))) {
					continue;
				}
				return true;
			}
		}
		return false;
	}

	private List<String> transferRectTooltip(GuiContainer gui, RecipeHandler<?> currentHandler, Map<RecipeHandler<?>, TransferRect> transferRects, int offsetx,
			int offsety, List<String> currenttip) {
		Point pos = GuiDraw.getMousePosition();
		Point relMouse = new Point(pos.x - gui.guiLeft - offsetx, pos.y - gui.guiTop - offsety);
		for (Map.Entry<RecipeHandler<?>, TransferRect> entry : transferRects.entrySet()) {
			if (entry.getKey().getRecipeViewer().isGuiContainerSupported(gui) && (currentHandler != null ? currentHandler == entry.getKey() : true)) {
				TransferRect rect = entry.getValue();
				if (!rect.rect.contains(relMouse)) {
					continue;
				}
				currenttip.add(entry.getKey().getRecipeViewer().getButtonTooltip(gui.getClass()));
				break;
			}
		}
		return currenttip;
	}

	@Override
	public void onKeyTyped(GuiContainer gui, char keyChar, int keyID) {}

	@Override
	public void onMouseClicked(GuiContainer gui, int mousex, int mousey, int button) {}

	@Override
	public void onMouseUp(GuiContainer gui, int mousex, int mousey, int button) {}

	@Override
	public boolean keyTyped(GuiContainer gui, char keyChar, int keyID) {
		return false;
	}

	@Override
	public boolean mouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {
		return false;
	}

	@Override
	public void onMouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {}

	@Override
	public List<String> handleTooltip(GuiContainer gui, int mousex, int mousey, final List<String> currenttip) {
		if (!this.canHandle(gui)) return currenttip;

		List<String> ret = currenttip;

		if (GuiContainerManager.shouldShowTooltip(gui) && currenttip.size() == 0) {

			if (gui instanceof GuiRecipe) {
				GuiRecipe guiRecipe = (GuiRecipe) gui;

				ret = this.executeForGuiRecipe(guiRecipe, null, (triple) -> {
					Point offset = guiRecipe.getRecipePosition(triple.getLeft());
					List<String> oldtip = currenttip;
					List<String> newTip = this.transferRectTooltip(gui, triple.getRight(), NEIRecipeHandlersTransferRectManager.guiMap.get(gui.getClass()),
							offset.x, offset.y, currenttip);
					if (!oldtip.equals(newTip)) return newTip;
					return null;
				}, false);

				if (ret == null) {
					ret = currenttip;
				}

			} else {
				int[] offset = RecipeInfo.getGuiOffset(gui);
				ret = this.transferRectTooltip(gui, null, NEIRecipeHandlersTransferRectManager.guiMap.get(gui.getClass()), offset[0], offset[1], currenttip);
			}
		}
		return ret;
	}

	@Override
	public List<String> handleItemDisplayName(GuiContainer gui, ItemStack itemstack, List<String> currenttip) {
		return currenttip;
	}

	@Override
	public List<String> handleItemTooltip(GuiContainer gui, ItemStack itemstack, int mousex, int mousey, List<String> currenttip) {
		return currenttip;
	}

	@Override
	public void onMouseDragged(GuiContainer gui, int mousex, int mousey, int button, long heldTime) {}

	@Override
	public void onPreDraw(GuiContainer var1) {}

	@Override
	public void postRenderObjects(GuiContainer guiContainer, int mouseX, int mouseY) {
		if (this.canHandle(guiContainer)) {
			NEIRecipeHandlersTransferRectManager.guiMap.get(guiContainer.getClass()).forEach((handler, rect) -> {
				if (handler.getRecipeViewer().isGuiContainerSupported(guiContainer)) {

					if (guiContainer instanceof GuiRecipe) {
						GuiRecipe guiRecipe = (GuiRecipe) guiContainer;

						this.executeForGuiRecipe(guiRecipe, handler, (triple) -> {
							Point offset = guiRecipe.getRecipePosition(triple.getLeft());
							this.renderButtons(guiContainer, handler, offset.x, offset.y);
							return null;
						}, true);

					} else {
						int[] offset = RecipeInfo.getGuiOffset(guiContainer);
						this.renderButtons(guiContainer, handler, offset[0], offset[1]);
					}

				}
			});

		}

	}

	private <T> T executeForGuiRecipe(GuiRecipe guiContainer, RecipeHandler<?> handler,
			Function<Triple<Integer, RecipeHandler<?>, RecipeHandler<?>>, T> toExecute, boolean innerHandlerEqualToHandler) {
		GuiRecipe guiRecipe = guiContainer;
		IRecipeHandler currentHandler = guiRecipe.currenthandlers.get(guiRecipe.recipetype);

		if (currentHandler instanceof PluginRecipeHandler<?, ?>) {
			RecipeHandler<?> innerHandler = ((PluginRecipeHandler<?, ?>) currentHandler).getInnerHandler();

			if (innerHandlerEqualToHandler ? innerHandler == handler : true) {

				for (int currentRecipe = guiRecipe.page * currentHandler.recipiesPerPage(); currentRecipe < currentHandler.numRecipes()
						&& currentRecipe < (guiRecipe.page + 1) * currentHandler.recipiesPerPage(); ++currentRecipe) {
					T result = toExecute.apply(Triple.of(currentRecipe, handler, innerHandler));
					if (result != null) return result;
				}
			}
		}
		return null;
	}

	private void renderButtons(GuiContainer guiContainer, RecipeHandler<?> handler, int offsetX, int offsetY) {
		Point absMousePos = GuiDraw.getMousePosition();
		Point relMouse = new Point(absMousePos.x - guiContainer.guiLeft - offsetX, absMousePos.y - guiContainer.guiTop - offsetY);

		Rectangle location = new Rectangle(RecipeHandlerRecipeViewer.VIEW_ALL_RECIPES_RECTANGLE);
		location.translate(handler.getRecipeViewer().getOffsetX(guiContainer.getClass()), handler.getRecipeViewer().getOffsetY(guiContainer.getClass()));

		GL11.glPushMatrix();

		// Render the book item
		GL11.glTranslatef(guiContainer.guiLeft + location.x + offsetX, guiContainer.guiTop + location.y + offsetY, 0);
		GL11.glScalef(location.width / 16.0f, location.height / 16.0f, 0);
		// Render the book item
		RenderItem.getInstance().renderItemIntoGUI(GuiDraw.fontRenderer, GuiDraw.renderEngine, handler.getRecipeViewer().getButtonIcon(guiContainer.getClass()),
				0, 0);
		GL11.glScalef(16.0f / location.width, 16.0f / location.height, 0);

		// Render the overlay if the mouse hovers over the item
		if (location.contains(relMouse)) {
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glColorMask(true, true, true, false);
			GuiDraw.drawGradientRect(0, 0, location.width, location.height, -2130706433, -2130706433);
			GL11.glColorMask(true, true, true, true);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		}

		GL11.glPopMatrix();
	}

	@Override
	public void renderObjects(GuiContainer var1, int var2, int var3) {}

	@Override
	public void renderSlotOverlay(GuiContainer var1, Slot var2) {}

	@Override
	public void renderSlotUnderlay(GuiContainer var1, Slot var2) {}

	public static class TransferRect {
		private Rectangle rect;
		private String outputId;
		private Object[] results;

		public TransferRect(Rectangle rectangle, String outputId, Object... results) {
			this.rect = rectangle;
			this.outputId = outputId;
			this.results = results;
		}

		public Rectangle getRect() {
			return this.rect;
		}

		public String getOutputId() {
			return this.outputId;
		}

		public Object[] getResults() {
			return this.results;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof TransferRect)) return false;
			return this.rect.equals(((TransferRect) obj).rect);
		}

		@Override
		public int hashCode() {
			return this.rect.hashCode();
		}
	}

}
