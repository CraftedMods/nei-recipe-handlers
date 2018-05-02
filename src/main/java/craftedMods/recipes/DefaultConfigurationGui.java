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
package craftedMods.recipes;

import java.util.*;

import cpw.mods.fml.client.config.*;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.*;

public class DefaultConfigurationGui extends GuiConfig {

	public DefaultConfigurationGui(GuiScreen parent) {
		super(parent, DefaultConfigurationGui.getConfigElements(), NEIRecipeHandlers.MODID, true, false,
				StatCollector.translateToLocal("neiRecipeHandlers.config.gui.title"));
	}

	@SuppressWarnings("rawtypes")
	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> ret = new ArrayList<>();
		Configuration config = NEIRecipeHandlers.mod.getConfig().getConfigFile();

		Set<String> registeredCategories = new HashSet<>();
		for (String categoryName : config.getCategoryNames()) {
			boolean add = true;
			for (String cat : categoryName.split("\\."))
				if (registeredCategories.contains(cat)) {
					add = false;
					break;
				}
			if (add) {
				ConfigCategory category = config.getCategory(categoryName);
				registeredCategories.add(category.getName());
				DefaultConfigurationGui.addChildren(category, registeredCategories);
				ret.add(new ConfigElement(category));
			}
		}

		return ret;
	}

	private static void addChildren(ConfigCategory category, Set<String> toAdd) {
		for (ConfigCategory child : category.getChildren()) {
			toAdd.add(child.getName());
			DefaultConfigurationGui.addChildren(child, toAdd);
		}
	}
}
