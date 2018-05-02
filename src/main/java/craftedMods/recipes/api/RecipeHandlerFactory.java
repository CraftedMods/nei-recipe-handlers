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
package craftedMods.recipes.api;

import java.util.Set;

/**
 * A handler which manually registers recipe handlers.</br>
 * This is necessary of you want to use the same recipe handler class for multiple devices - for example for a ton of Crafting Tables (like the LOTR
 * Mod has) - or if you want to reuse your handler class. So instead of letting the provider to create one instance per handler class you can create
 * as much instances as you need using a factory and register them by your own.</br>
 * To be loaded, the handler needs to be annotated with {@link craftedMods.recipes.api.RegisteredHandler}
 * 
 * @author CraftedMods
 */
public interface RecipeHandlerFactory {

	/**
	 * A set of instances to register
	 * 
	 * @return The recipe handler instances
	 */
	public Set<RecipeHandler<?>> getRecipeHandlers();

}
