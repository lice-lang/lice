package org.lice

import org.jetbrains.annotations.TestOnly
import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Created by ice1000 on 2017/4/1.
 *
 * @author ice1000
 */

@TestOnly
class FeatureTest {

	/**
	 * test for string literals
	 */
	@Test(timeout = 1000)
	fun test1() {
		assertEquals("fuck", Lice.run("""
(print "fuck")
""").o)
		assertEquals("lover~lover~fucker~fucker~", Lice.run(""""lover~lover~fucker~fucker~"""").o)
	}

	/**
	 * test for int literals
	 */
	@Test(timeout = 1000)
	fun test2() {
		assertEquals(233666, Lice.run("""
(print 233666)
""").o)
		assertEquals(233666, Lice.run("233666").o)
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

	/**
	 * comparision
	 */
	@Test
	fun test6() {
		assertTrue(true == Lice.run("(>= 9 8 7 7 6 6 6 5 4 3 1 -1)").o)
		assertTrue(true != Lice.run("(>= 9 8 7 7 6 6 6 5 8 3 1 -1)").o)
	}

	/**
	 * string connection
	 */
	@Test
	fun test7() {
		assertEquals("boyNextDoor", Lice.run("""
(str-con "boy" "Next" "Door")
""").o)
	}

	/**
	 * parsing string
	 */
	@Test
	fun test8() {
		assertEquals(0xDBE, Lice.run("""
(str->int "0xDBE")
""").o)
	}

	/**
	 * string evaluation
	 */
	@Test
	fun test9() {
		assertEquals(2, Lice.run("""
(eval "(+ 1 1)")
""").o)
	}

	/**
	 * run/begin block
	 */
	@Test
	fun test10() {
		assertEquals(4, Lice.run("""
(|> (+ 1 1) (+ 2 2))
""").o)
	}

	/**
	 * force running
	 */
	@Test
	fun test11() {
		assertNull(Lice.run("""
(force|> (+ () ()))
""").o)
		assertNull(Lice.run("()").o)
	}

	/**
	 * variable
	 */
	@Test
	fun test12() {
		assertEquals(233, Lice.run("""
(-> ice1000 233)

ice1000
""").o)
	}

	/**
	 * function
	 */
	@Test
	fun test13() {
		assertEquals(233, Lice.run("""
(defexpr ice1000 233)

ice1000
""").o)
		//language=TEXT
		assertEquals(233, Lice.run("""
(defexpr ice1000 a a)

(ice1000 233)
""").o)
	}

	/**
	 * recursion
	 */
	@Test
	fun test14() {
		//language=TEXT
		assertEquals(5, Lice.run("""
(def gcd a b (if (=== b 0) a (gcd b (% a b))))

(gcd 15 20)
""").o)
	}
}
