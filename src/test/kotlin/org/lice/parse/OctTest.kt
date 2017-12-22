package org.lice.parse

import org.jetbrains.annotations.TestOnly
import org.junit.Assert.assertEquals
import org.junit.Test
import org.lice.parse.toOctInt

/**
 * @author ice1000
 */
class OctTest {
	@TestOnly
	@Test(timeout = 1000)
	fun toOctTest() {
		assertEquals(520, "0o1010".toOctInt().toLong())
		assertEquals(355920, "0o1267120".toOctInt().toLong())
		assertEquals(294920, "0o1100010".toOctInt().toLong())
	}
}
