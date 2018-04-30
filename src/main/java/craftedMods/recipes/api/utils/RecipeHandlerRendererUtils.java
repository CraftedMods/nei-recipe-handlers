package craftedMods.recipes.api.utils;

import craftedMods.recipes.utils.RecipeHandlerRendererUtilsImpl;

/**
 * A collection of utilities for rendering recipe handler GUIs.
 * 
 * @author CraftedMods
 */
public interface RecipeHandlerRendererUtils {

	public static final RecipeHandlerRendererUtils instance = new RecipeHandlerRendererUtilsImpl();

	/**
	 * @return An instance of the utility class
	 */
	public static RecipeHandlerRendererUtils getInstance() {
		return RecipeHandlerRendererUtils.instance;
	}

	/**
	 * Binds a texture
	 * 
	 * @param texture
	 *            The texture to bind
	 */
	public void bindTexture(String texture);

	/**
	 * Draws a rectangle from P(x|y) to Q(x+width|y+height) with a color
	 * 
	 * @param x
	 *            The x origin
	 * @param y
	 *            The y origin
	 * @param width
	 *            The width
	 * @param height
	 *            The height
	 * @param color
	 *            The color
	 */
	public void drawRectangle(int x, int y, int width, int height, int color);

	/**
	 * Draws a rectangle from P(x|y) to Q(x+width|y+height) with a gradient color (from color1 to
	 * color2)
	 * 
	 * @param x
	 *            The x origin
	 * @param y
	 *            The y origin
	 * @param width
	 *            The width
	 * @param height
	 *            The height
	 * @param color1
	 *            The left color bound
	 * @param color2
	 *            The right color bound
	 */
	public void drawGradientRectangle(int x, int y, int width, int height, int color1, int color2);

	/**
	 * Draws a rectangle from P(x|y) to Q(x+width|y+height) with a part of the current bound texture
	 * from R(textureX|textureY) to S(textureX+width|textureHeight+height)
	 * 
	 * @param x
	 *            The x origin
	 * @param y
	 *            The y origin
	 * @param textureX
	 *            The x (u) coordinate in the texture
	 * @param textureY
	 *            The y (v) coordinate inF the texture
	 * @param width
	 *            The width
	 * @param height
	 *            The height r
	 */
	public void drawTexturedRectangle(int x, int y, int textureX, int textureY, int width, int height);

	/**
	 * Draws text at P(x|y) with the specified color and optionally a text shadow.
	 * 
	 * @param text
	 *            The text
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 * @param color
	 *            The text color
	 * @param shadow
	 *            Whether a text shadow should be drawn
	 */
	public void drawText(String text, int x, int y, int color, boolean shadow);

	/**
	 * Draws text at P(x|y) with the specified color
	 * 
	 * @param text
	 *            The text
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 * @param color
	 *            The text color
	 */
	public void drawText(String text, int x, int y, int color);

	/**
	 * Draws text centered around P(x|y) with the specified color and optionally a text shadow.
	 * 
	 * @param text
	 *            The text
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 * @param color
	 *            The text color
	 * @param shadow
	 *            Whether a text shadow should be drawn
	 */
	public void drawTextCentered(String text, int x, int y, int color, boolean shadow);

	/**
	 * Draws text centered around P(x|y) with the specified color
	 * 
	 * @param text
	 *            The text
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 * @param color
	 *            The text color
	 */
	public void drawTextCentered(String text, int x, int y, int color);

	/**
	 * Returns the width of the provided text
	 * 
	 * @param text
	 *            The text
	 * @return The width of the text
	 */
	public int getStringWidth(String text);

	/**
	 * Draws a progress bar from P(x|y) to Q(x+width|y+height) with a part of the current bound
	 * texture from R(textureX|textureY) to S(textureX+width|textureHeight+height) by the provided
	 * completion (100% for the whole bar) from the provided direction.
	 * 
	 * @param x
	 *            The x origin
	 * @param y
	 *            The x origin
	 * @param textureX
	 *            The x (u) coordinate in the texture
	 * @param textureY
	 *            The y (v) coordinate in the texture
	 * @param width
	 *            The width
	 * @param height
	 *            The height
	 * @param completion
	 *            The completion factor (1.0: 100%)
	 * @param direction
	 *            The direction from/to which the progress bar grows/shrinks
	 */
	public void drawProgressBar(int x, int y, int textureX, int textureY, int width, int height, float completion, EnumProgressBarDirection direction);

	/**
	 * A set of directions to/from which the progress bar grows/shrinks
	 * 
	 * @author CraftedMods
	 */
	public enum EnumProgressBarDirection {
		INCREASE_RIGHT, INCREASE_DOWN, INCREASE_LEFT, INCREASE_UP, DECREASE_LEFT, DECREASE_UP, DECREASE_RIGHT, DECREASE_DOWN;
	}

}
