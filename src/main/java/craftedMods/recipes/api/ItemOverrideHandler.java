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
package craftedMods.recipes.api;

import java.util.Map;

import net.minecraft.item.ItemStack;

/**
 * A handler which overrides the display name of an item stack in NEI.</br>
 * This is useful for items like portal block items, which usually don't have a localized name, but are useful for users and thus won't be hidden.
 * With this handler, you can give them a human readable name. </br>
 * To be loaded, the handler needs to be annotated with {@link craftedMods.recipes.api.RegisteredHandler}
 * 
 * @author CraftedMods
 */
public interface ItemOverrideHandler {

	/**
	 * A map containing the override names of specific item stacks
	 * 
	 * @return The override name map
	 */
	public Map<ItemStack, String> getItemOverrideNames();

}
