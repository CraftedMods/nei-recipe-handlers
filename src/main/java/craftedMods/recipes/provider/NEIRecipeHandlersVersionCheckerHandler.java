/*******************************************************************************
 * Copyright (C) 2020 CraftedMods (see https://github.com/CraftedMods)
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

import craftedMods.recipes.NEIRecipeHandlers;
import craftedMods.recipes.api.*;
import craftedMods.utils.SemanticVersion;

@RegisteredHandler
public class NEIRecipeHandlersVersionCheckerHandler implements VersionCheckerHandler
{

    @Override
    public String getLocalizedHandlerName ()
    {
        return NEIRecipeHandlers.MODNAME;
    }

    @Override
    public String getVersionFileURL ()
    {
        return "https://raw.githubusercontent.com/CraftedMods/nei-recipe-handlers/master/version.txt";
    }

    @Override
    public SemanticVersion getCurrentVersion ()
    {
        return NEIRecipeHandlers.SEMANTIC_VERSION;
    }

}
