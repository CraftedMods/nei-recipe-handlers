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
package craftedMods.utils;

import org.junit.jupiter.api.*;

public class SemanticVersionTest {

	private SemanticVersion alpha = new SemanticVersion(EnumVersionState.ALPHA, 1, 2, 3, -1);
	private SemanticVersion beta = new SemanticVersion(EnumVersionState.BETA, 1, 2, 3, -1);
	private SemanticVersion full = new SemanticVersion(EnumVersionState.FULL, 1, 2, 3, -1);

	@Test
	public void testConstructorNullVersionState() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			new SemanticVersion(null, 10, 20, 30, 40);
		});
	}

	@Test
	public void testConstructorMajorVersionLessThanZero() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new SemanticVersion(EnumVersionState.ALPHA, -10, 20, 30, 40);
		});
	}

	@Test
	public void testConstructorMinorVersionLessThanZero() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new SemanticVersion(EnumVersionState.ALPHA, 10, -20, 30, 40);
		});
	}

	@Test
	public void testConstructorPatchVersionLessThanZero() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new SemanticVersion(EnumVersionState.ALPHA, 10, 20, -30, 40);
		});
	}

	@Test
	public void testConstructorPreVersionLessThanZeroFix() {
		Assertions.assertEquals(-1, new SemanticVersion(EnumVersionState.ALPHA, 10, 20, 30, -40).getPreReleaseVersion());
	}

	@Test
	public void testIsPreRelease() {
		Assertions.assertFalse(this.alpha.isPreRelease());
		Assertions.assertTrue(new SemanticVersion(EnumVersionState.ALPHA, 10, 20, 30, 40).isPreRelease());
	}

	@Test
	public void testCompareToSelf() {
		Assertions.assertEquals(0, this.alpha.compareTo(this.alpha));
		Assertions.assertEquals(0, this.beta.compareTo(this.beta));
		Assertions.assertEquals(0, this.full.compareTo(this.full));
	}

	@Test
	public void testCompareToVersionState() {
		Assertions.assertTrue(this.alpha.compareTo(this.beta) < 0);
		Assertions.assertTrue(this.alpha.compareTo(this.full) < 0);

		Assertions.assertTrue(this.beta.compareTo(this.alpha) > 0);
		Assertions.assertTrue(this.beta.compareTo(this.full) < 0);

		Assertions.assertTrue(this.full.compareTo(this.alpha) > 0);
		Assertions.assertTrue(this.full.compareTo(this.beta) > 0);
	}

	@Test
	public void testCompareToMajorVersion() {
		Assertions.assertTrue(this.alpha.compareTo(new SemanticVersion(EnumVersionState.ALPHA, 2, 2, 3, -1)) < 0);
		Assertions.assertTrue(this.alpha.compareTo(new SemanticVersion(EnumVersionState.ALPHA, 0, 2, 3, -1)) > 0);
	}

	@Test
	public void testCompareToMinorVersion() {
		Assertions.assertTrue(this.alpha.compareTo(new SemanticVersion(EnumVersionState.ALPHA, 1, 3, 3, -1)) < 0);
		Assertions.assertTrue(this.alpha.compareTo(new SemanticVersion(EnumVersionState.ALPHA, 1, 1, 3, -1)) > 0);
	}

	@Test
	public void testCompareToPatchVersion() {
		Assertions.assertTrue(this.alpha.compareTo(new SemanticVersion(EnumVersionState.ALPHA, 1, 2, 4, -1)) < 0);
		Assertions.assertTrue(this.alpha.compareTo(new SemanticVersion(EnumVersionState.ALPHA, 1, 2, 2, -1)) > 0);
	}

	@Test
	public void testCompareToPreRelease() {
		Assertions.assertTrue(this.alpha.compareTo(new SemanticVersion(EnumVersionState.ALPHA, 1, 2, 3, 0)) > 0);
	}

	@Test
	public void testToString() {
		Assertions.assertEquals("1.2.3-ALPHA", this.alpha.toString());
		Assertions.assertEquals("1.2.3-BETA", this.beta.toString());
		Assertions.assertEquals("1.2.3", this.full.toString());

		Assertions.assertEquals("100.2.3-ALPHA.5", new SemanticVersion(EnumVersionState.ALPHA, 100, 2, 3, 5).toString());
		Assertions.assertEquals("1.200.3-BETA.3", new SemanticVersion(EnumVersionState.BETA, 1, 200, 3, 3).toString());
		Assertions.assertEquals("1.2.300-PRE.2", new SemanticVersion(EnumVersionState.FULL, 1, 2, 300, 2).toString());
	}

	@Test
	public void testOfInvalidVersionEmpty() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("");
		});
	}

	@Test
	public void testOfInvalidVersionBlank() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("	 	");
		});
	}

	@Test
	public void testOfInvalidVersionLetters() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("0.5.5d");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("0.5.d5");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("0.5d.5");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("0.d5.5");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("0d.5.5");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("d0.5.5");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("d0d.d5d.d5d");
		});
	}

	@Test
	public void testOfInvalidVersionWrongSeparator() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("0:5:3");
		});
	}

	@Test
	public void testOfInvalidVersionWrongSuffix() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("0.5.3-");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("0.5.3-PI");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("0.5.3-GREEN");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("0.5.3-ALPH");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("0.5.3-BGTA");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("0.5.3-PRE2");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("0.5.3-2PRE");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("0.5.32PRE");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("0.5.3PRE");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("0.5.3.PRE");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("0.5.3.ALPHA");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("0.5.3.BETA");
		});
	}

	@Test
	public void testOfInvalidVersionPreRelease() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("0.5.3-BETA.3m");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("0.5.3-BETA.");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("0.5.3-BETA.d");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SemanticVersion.of("0.5.3-PRE.-1");
		});
	}

	@Test
	public void testOfInvalidVersionNull() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			SemanticVersion.of(null);
		});
	}

	@Test
	public void testOfValidVersion() {
		Assertions.assertTrue(this.alpha.equals(SemanticVersion.of("1.2.3-ALPHA")));
		Assertions.assertTrue(this.beta.equals(SemanticVersion.of("1.2.3-BETA")));
		Assertions.assertTrue(this.full.equals(SemanticVersion.of("1.2.3")));
		Assertions.assertTrue(new SemanticVersion(EnumVersionState.ALPHA, 10, 20, 30, 40).equals(SemanticVersion.of("10.20.30-ALPHA.40")));
		Assertions.assertTrue(new SemanticVersion(EnumVersionState.BETA, 10, 20, 30, 40).equals(SemanticVersion.of("10.20.30-BETA.40")));
		Assertions.assertTrue(new SemanticVersion(EnumVersionState.FULL, 10, 20, 30, 40).equals(SemanticVersion.of("10.20.30-PRE.40")));
	}

	@Test
	public void testOfValidVersionWithTrailingWhitespaces() {
		Assertions.assertTrue(this.alpha.equals(SemanticVersion.of("	 	 	 	 	 		 	 1.2.3-ALPHA	 	 	")));
		Assertions.assertTrue(SemanticVersion.of("1.2.3-ALPHA	  ").equals(SemanticVersion.of("	 	 	 	 	 		 	 1.2.3-ALPHA	 	 	")));
	}

}
