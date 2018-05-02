/*******************************************************************************
 * Copyright (C) 2018 CraftedMods (see https://github.com/CraftedMods)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package craftedMods.recipes.api;

import java.util.regex.Pattern;

/**
 * The recipe handler configuration allows a recipe handler to load and store configuration data in
 * the configuration file of the provider. Each recipe handler gets it's own entry in there.
 * 
 * @author CraftedMods
 */
public interface RecipeHandlerConfiguration {

	/**
	 * Reloads the configuration data from the file
	 */
	public void reload();

	/**
	 * If false, the recipe handler was disabled via the configuration file, if true, not
	 * 
	 * @return Whether the recipe handler is enabled
	 */
	public boolean isEnabled();

	public default String getString(String name, String defaultValue, String comment) {
		return this.getString(name, defaultValue, comment, name, null);
	}

	public default String getString(String name, String defaultValue, String comment, String langKey) {
		return this.getString(name, defaultValue, comment, langKey, null);
	}

	public default String getString(String name, String defaultValue, String comment, Pattern pattern) {
		return this.getString(name, defaultValue, comment, name, pattern);
	}

	public String getString(String name, String defaultValue, String comment, String langKey, Pattern pattern);

	public default String getString(String name, String defaultValue, String comment, String[] validValues) {
		return this.getString(name, defaultValue, comment, validValues, name);
	}

	public String getString(String name, String defaultValue, String comment, String[] validValues, String langKey);

	public default String[] getStringList(String name, String[] defaultValues, String comment) {
		return this.getStringList(name, defaultValues, comment, (String[]) null, name);
	}

	public default String[] getStringList(String name, String[] defaultValue, String comment, String[] validValues) {
		return this.getStringList(name, defaultValue, comment, validValues, name);
	}

	public String[] getStringList(String name, String[] defaultValue, String comment, String[] validValues, String langKey);

	public default boolean getBoolean(String name, boolean defaultValue, String comment) {
		return this.getBoolean(name, defaultValue, comment, name);
	}

	public boolean getBoolean(String name, boolean defaultValue, String comment, String langKey);

	public default int getInt(String name, int defaultValue, int minValue, int maxValue, String comment) {
		return this.getInt(name, defaultValue, minValue, maxValue, comment, name);
	}

	public int getInt(String name, int defaultValue, int minValue, int maxValue, String comment, String langKey);

	public default float getFloat(String name, float defaultValue, float minValue, float maxValue, String comment) {
		return this.getFloat(name, defaultValue, minValue, maxValue, comment, name);
	}

	public float getFloat(String name, float defaultValue, float minValue, float maxValue, String comment, String langKey);

}
