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
package craftedMods.recipes.base;

import java.io.InputStream;
import java.util.*;
import java.util.function.Supplier;

import net.minecraft.util.ResourceLocation;

/**
 * A resource loader implementation which loads the resources from the classpath
 *
 * @author CraftedMods
 */
public class ClasspathResourceLoader implements RecipeHandlerResourceLoader
{

    private Set<ResourceLocation> resourceLocations = new HashSet<> ();
    private String prefix = "";

    /**
     * Gets the loading path prefix - a prefix that will be appended between the
     * root path and the resource path specified in the resource location. This can
     * be used for example if the actual path and the path specified via the
     * resource location don't match.
     *
     * @return The loading path prefix
     */
    public String getPrefix ()
    {
        return prefix;
    }

    public void setPrefix (String prefix)
    {
        this.prefix = prefix;
    }

    @Override
    public boolean registerResource (ResourceLocation location)
    {
        return resourceLocations.add (location);
    }

    @Override
    public Map<ResourceLocation, Supplier<InputStream>> loadResources ()
    {
        Map<ResourceLocation, Supplier<InputStream>> ret = new HashMap<> ();
        resourceLocations.forEach (location ->
        {
            if (this.getClass ().getResource ("/" + prefix + location.getResourcePath ()) != null)
            {
                ret.put (location, () ->
                {
                    return this.getClass ().getResourceAsStream ("/" + prefix + location.getResourcePath ());
                });
            }
        });
        return ret;
    }

}
