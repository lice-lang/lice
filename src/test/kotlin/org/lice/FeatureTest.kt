package org.lice

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Created by ice1000 on 2017/4/1.
 *
 * @author ice1000
 */

class FeatureTest {

	/**
	 * test for string literals
	 */
	@Test(timeout = 1000)
	fun test1() {
		assertEquals("fuck", Lice.run("""
(print "fuck")
""").o)
	}

	/**
	 * test for int literals
	 */
	@Test(timeout = 1000)
	fun test2() {
		assertEquals(233666, Lice.run("""
(print 233666)
""").o)
	}

	/**
	 * test for plus
	 */
	@Test(timeout = 1000)
	fun test3() {
		assertEquals(7, Lice.run("""
(+ 1 (+ 1 2 3))
""").o)
	}

	/**
	 * test for big integers
	 */
	@Test(timeout = 1000)
	fun test4() {
		assertEquals(BigInteger("10000000000000000000000000000233"), Lice.run("""
(+ 10000000000000000000000000000000N 233)
""").o)
	}

	/**
	 * test for big decimals
	 */
	@Test(timeout = 1000)
	fun test5() {
		assertEquals(BigDecimal("10000000000000000000000000000000.233"), Lice.run("""
(+ 10000000000000000000000000000000N 0.233)
""").o)
	}
}
