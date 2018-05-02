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

import java.lang.annotation.*;

/**
 * An annotation marking several handlers of the API as registered. Registered handlers will be - if
 * they're enabled - automatically loaded by the provider. </br>
 * <b>Important:</b> Every registered handler needs a zero argument constructor to be instantiated.
 * 
 * @author CraftedMods
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RegisteredHandler {

	/**
	 * If true, a handler instance will be created and the handler will be loaded, if false, not
	 * 
	 * @return Whether the handler is enabled
	 */
	boolean isEnabled() default true;

}
