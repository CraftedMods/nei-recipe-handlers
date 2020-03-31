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
package craftedMods.utils;

import java.util.Objects;
import java.util.regex.Pattern;

public class SemanticVersion implements Comparable<SemanticVersion>
{

    private final EnumVersionState versionState;
    private final int majorVersion;
    private final int minorVersion;
    private final int patchVersion;
    private final int preReleaseVersion;

    private final String versionString;// Since this object is immutable, the version string doesn't
                                       // has to be computed every call of toString

    public SemanticVersion (EnumVersionState versionState, int majorVersion, int minorVersion, int patchVersion,
        int preReleaseVersion)
    {
        Objects.requireNonNull (versionState);
        if (majorVersion < 0)
            throw new IllegalArgumentException ("Major version is less then zero");
        if (minorVersion < 0)
            throw new IllegalArgumentException ("Minor version is less then zero");
        if (patchVersion < 0)
            throw new IllegalArgumentException ("Patch version is less then zero");
        if (preReleaseVersion < -1)
        {
            preReleaseVersion = -1;
        }
        this.versionState = versionState;
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.patchVersion = patchVersion;
        this.preReleaseVersion = preReleaseVersion;
        versionString = buildVersionString ();
    }

    public SemanticVersion (EnumVersionState versionState, int majorVersion, int minorVersion, int patchVersion)
    {
        this (versionState, majorVersion, minorVersion, patchVersion, -1);
    }

    public EnumVersionState getVersionState ()
    {
        return versionState;
    }

    public int getMajorVersion ()
    {
        return majorVersion;
    }

    public int getMinorVersion ()
    {
        return minorVersion;
    }

    public int getPatchVersion ()
    {
        return patchVersion;
    }

    public int getPreReleaseVersion ()
    {
        return preReleaseVersion;
    }

    public boolean isPreRelease ()
    {
        return preReleaseVersion >= 0;
    }

    @Override
    public int compareTo (SemanticVersion comparable)
    {
        int stateComp = versionState.compareTo (comparable.versionState);
        int majorComp = Integer.compare (majorVersion, comparable.majorVersion);
        int minorComp = Integer.compare (minorVersion, comparable.minorVersion);
        int patchComp = Integer.compare (patchVersion, comparable.patchVersion);
        int preReleaseComp = Integer.compare (preReleaseVersion, comparable.preReleaseVersion);
        if (preReleaseVersion < 0 || comparable.preReleaseVersion < 0)
        {
            preReleaseComp = -preReleaseComp;
        }
        return stateComp == 0
            ? majorComp == 0 ? minorComp == 0 ? patchComp == 0 ? preReleaseComp : patchComp : minorComp : majorComp
            : stateComp;
    }

    @Override
    public int hashCode ()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + majorVersion;
        result = prime * result + minorVersion;
        result = prime * result + patchVersion;
        result = prime * result + preReleaseVersion;
        result = prime * result + (versionState == null ? 0 : versionState.hashCode ());
        return result;
    }

    @Override
    public boolean equals (Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass () != obj.getClass ())
            return false;
        SemanticVersion other = (SemanticVersion) obj;
        if (majorVersion != other.majorVersion)
            return false;
        if (minorVersion != other.minorVersion)
            return false;
        if (patchVersion != other.patchVersion)
            return false;
        if (preReleaseVersion != other.preReleaseVersion)
            return false;
        if (versionState != other.versionState)
            return false;
        return true;
    }

    private String buildVersionString ()
    {
        return String.format ("%d.%d.%d%s%s", majorVersion, minorVersion, patchVersion,
            versionState == EnumVersionState.FULL ? "" : String.format ("-%s", versionState.toString ()),
            preReleaseVersion >= 0
                ? String.format ("%s.%d", versionState == EnumVersionState.FULL ? "-PRE" : "",
                    preReleaseVersion)
                : "");
    }

    @Override
    public String toString ()
    {
        return versionString;
    }

    public static SemanticVersion of (String versionString)
    {
        Objects.requireNonNull (versionString);
        String trimmedVersion = versionString.trim ();
        if (!Pattern.matches ("^[0-9]+\\.[0-9]+\\.[0-9]+($|(-PRE)|(-ALPHA)|(-BETA))($|(\\.[0-9]+))$", trimmedVersion))
            throw new IllegalArgumentException (
                String.format ("The version string \"%s\" doesn't match the specification", trimmedVersion));
        String[] parts = trimmedVersion.split ("-");
        String[] mainVersion = parts[0].split ("\\.");
        Integer majorVersion = Integer.parseInt (mainVersion[0]);
        Integer minorVersion = Integer.parseInt (mainVersion[1]);
        Integer patchVersion = Integer.parseInt (mainVersion[2]);
        EnumVersionState versionState = EnumVersionState.FULL;
        Integer preReleaseVersion = -1;
        if (parts.length > 1)
        {
            String[] parts2 = parts[1].split ("\\.");
            String state = parts2[0].replaceAll ("-", "");
            if (!state.equals ("PRE"))
            {
                versionState = EnumVersionState.valueOf (state);
            }
            if (parts2.length > 1)
            {
                preReleaseVersion = Integer.parseInt (parts2[1]);
            }
        }
        return new SemanticVersion (versionState, majorVersion, minorVersion, patchVersion, preReleaseVersion);
    }

}
