package org.lice

import org.jetbrains.annotations.TestOnly
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.lice.core.SymbolList
import java.io.File
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Demos for language feature
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
"""))
		assertEquals("lover~lover~fucker~fucker~", Lice.run(""""lover~lover~fucker~fucker~""""))
	}

	/**
	 * test for primitives literals
	 */
	@Test(timeout = 1000)
	fun test2() {
		assertEquals(233666, Lice.run("""
(print 233666)
"""))
		assertEquals(233666, Lice.run("233666"))
		assertEquals(233666F, Lice.run("233666.0"))
		assertEquals(233666.0, Lice.run("233666.0000000000000000000000000000000"))
		assertTrue(true == Lice.run("true"))
		assertFalse(true == Lice.run("false"))
		assertNull(Lice.run("null"))
	}

	/**
	 * test for plus minus times devide
	 */
	@Test(timeout = 1000)
	fun test3() {
		assertEquals(7, Lice.run("""
(+ 1 (+ 1 2 3))
"""))
		assertEquals(-5, Lice.run("""
(- 1 (+ 1 2 3))
"""))
		assertEquals(20, Lice.run("""
(* 4 (+ 2 3))
"""))
		assertEquals(0, Lice.run("+"))
		assertEquals(0, Lice.run("-"))
		assertEquals(0, Lice.run("(+)"))
		assertEquals(0, Lice.run("(-)"))
		assertEquals(20, Lice.run("""
(* 4 (+ 2 3))
"""))
	}

	/**
	 * test for big integers
	 */
	@Test(timeout = 1000)
	fun test4() {
		assertEquals(BigInteger("10000000000000000000000000000233"), Lice.run("""
(+ 10000000000000000000000000000000N 233)
"""))
		assertEquals(BigInteger(0xDBE.toString()), Lice.run("0xDBEN"))
	}

	/**
	 * test for big decimals
	 */
	@Test(timeout = 1000)
	fun test5() {
		assertEquals(BigDecimal("10000000000000000000000000000000.233"), Lice.run("""
(+ 10000000000000000000000000000000N 0.233)
"""))
	}

	/**
	 * comparision
	 */
	@Test(timeout = 1000)
	fun test6() {
		assertTrue(true == Lice.run("(>= 9 8 7 7 6 6 6 5 4 3 1 -1)"))
		assertTrue(true != Lice.run("(>= 9 8 7 7 6 6 6 5 8 3 1 -1)"))
	}

	/**
	 * string connection
	 */
	@Test(timeout = 1000)
	fun test7() {
		assertEquals("boyNextDoor", Lice.run("""
(str-con "boy" "Next" "Door")
"""))
	}

	/**
	 * parsing string
	 */
	@Test(timeout = 1000)
	fun test8() {
		assertEquals(0xDBE, Lice.run("""
(str->int "0xDBE")
"""))
	}

	/**
	 * string evaluation
	 */
	@Test(timeout = 1000)
	fun test9() {
		assertEquals(2, Lice.run("""
(eval "(+ 1 1)")
"""))
	}

	/**
	 * run/begin block
	 */
	@Test(timeout = 1000)
	fun test10() {
		assertEquals(4, Lice.run("""
(|> (+ 1 1) (+ 2 2))
"""))
	}

	/**
	 * force running
	 */
	@Test(timeout = 1000)
	fun test11() {
		assertNull(Lice.run("""
(force|> (+ () ()))
"""))
		assertNull(Lice.run("()"))
	}

	/**
	 * variable
	 */
	@Test(timeout = 1000)
	fun test12() {
		assertEquals(233, Lice.run("""
(-> ice1000 233)

ice1000
"""))
	}

	/**
	 * function
	 */
	@Test(timeout = 1000)
	fun test13() {
		assertEquals(233, Lice.run("""
(defexpr ice1000 233)

ice1000
"""))
		//language=TEXT
		assertEquals(233, Lice.run("""
(defexpr ice1000 a a)

(ice1000 233)
"""))
	}

	/**
	 * recursion
	 */
	@Test(timeout = 1000)
	fun test14() {
		//language=TEXT
		assertEquals(5, Lice.run("""
(def gcd a b (if (=== b 0) a (gcd b (% a b))))

(gcd 15 20)
"""))
		//language=TEXT
		assertEquals(89, Lice.run("""
(def in? ls a (> (count ls a) 0))

(def fib n (if (in? (list 0 1) n)
               1
               (+ (fib (- n 1)) (fib (- n 2)))))

(fib 10)
"""))
	}

	/**
	 * if condition
	 */
	@Test(timeout = 1000)
	fun test15() {
		assertEquals(1, Lice.run("(if (>= 9 8 7 7 6 6 6 5 4 3 1 -1) 1 (+ 1 1))"))
		assertEquals(2, Lice.run("(if (>= 9 8 7 7 6 6 6 5 8 3 1 -1) 1 (+ 1 1))"))
	}

	/**
	 * when condition
	 */
	@Test(timeout = 1000)
	fun test16() {
		assertEquals(666, Lice.run("""(when
(!== 1 1), 233
(=== 1 1), 666
)"""))
		assertEquals(123, Lice.run("""(when
(!== 1 1), 233
(=== 2 1), 666
123
)"""))
	}

	/**
	 * while loop
	 */
	@Test(timeout = 1000)
	fun test17() {
		assertEquals(9, Lice.run("""
(def exp-mod a b m (|>
  (-> ret 1)

  (while (!= 0 b) (|>

    (if (!= 0 (& b 1))
      (-> ret (% (* a ret) m)))
    (-> b (/ b 2))
    (-> a (% (* a a) m))))
  ret))

(exp-mod 23 2 26)
"""))
	}

	/**
	 * lazy evaluation
	 */
	@Test(timeout = 1000)
	fun test18() {
		assertEquals(233, Lice.run("""
(deflazy fuck a b b)

(-> lover 233)

(fuck (-> lover 666) ())

lover
"""))
		assertEquals(666, Lice.run("""
(def fuck a b b)

(-> lover 233)

(fuck (-> lover 666) ())

lover
"""))
		//language=TEXT
		assertEquals(233 + 1, Lice.run("""
(deflazy fuck a b (|> b b))

(-> lover 233)

(fuck () (-> lover (+ lover 1)))
"""))
	}

	/**
	 * assignment
	 */
	@Test(timeout = 1000)
	fun test19() {
		assertEquals(233, Lice.run("""
(-> ice1k 233)

(<-> ice1k 666)
ice1k
"""))
		assertNull(Lice.run("""
(-> ice1k null)

(<-> ice1k 666)
ice1k
"""))
		assertEquals(666, Lice.run("""
(<-> ice1k 666)
ice1k
"""))
	}

	/**
	 * literals
	 */
	@Test(timeout = 1000)
	fun test20() {
		assertNull(Lice.run("null"))
		assertTrue(true == Lice.run("true"))
		assertTrue(false == Lice.run("false"))
	}

	/**
	 * escape
	 */
	@Test(timeout = 1000)
	fun test21() {
		assertEquals("\n", Lice.run(""""\n""""))
		assertEquals("\r", Lice.run(""""\r""""))
		assertEquals("\b", Lice.run(""""\b""""))
		assertEquals("\t", Lice.run(""""\t""""))
// FIXME assertEquals("\"", Lice.run(""""\"""""))
		assertEquals("\'", Lice.run(""""\'""""))
		assertEquals("\\", Lice.run(""""\\""""))
	}

	/**
	 * trigonometric
	 */
	@Test(timeout = 1000)
	fun test22() {
		val r = Random(System.currentTimeMillis()).nextDouble()
		assertEquals(Math.sin(r), Lice.run("(sin $r)") as Double, 1e-10)
	}

	/**
	 * head
	 */
	@Test(timeout = 1000)
	fun test23() {
		assertEquals(233, Lice.run("([| (list 233 344 455 566))"))
		assertEquals(233, Lice.run("([| ([|] 233 344 455 566))"))
	}

	/**
	 * count
	 */
	@Test(timeout = 1000)
	fun test24() {
		assertEquals(2, Lice.run("(count (list 233 233 455 566) 233)"))
		assertEquals(0, Lice.run("(count (.. 233 234) 1)"))
	}

	/**
	 * nested expression
	 */
	@Test(timeout = 1000)
	fun test25() {
		//language=TEXT
		assertEquals(2, Lice.run("((((2))))"))
		//language=TEXT
		assertEquals(0, Lice.run("((if true 0 1))"))
	}

	/**
	 * returning an expression
	 */
	@Test(timeout = 1000)
	fun test26() {
		//language=TEXT
		assertEquals(1, Lice.run("((if false * /) 11 11)"))
		//language=TEXT
		assertEquals(121, Lice.run("((if true * /) 11 11)"))
	}

	/**
	 * mention:
	 * () means evaluation
	 * so if you nest expressions, it will be evaluated without passing the params.
	 *
	 * I'll show you the procedure:
	 *
	 * (((if true + -)) 11 11)
	 * ((+) 11 11)
	 * (0 11 11)
	 * 0
	 */
	@Test(timeout = 1000)
	fun test27() {
		assertEquals(0, Lice.run("(((if true + -)) 11 11)"))
		assertEquals(0, Lice.run("+"))
		assertEquals(0, Lice.run("-"))
		assertEquals(1, Lice.run("*"))
	}

	/**
	 * yes, it's true!
	 * lambda expression!
	 */
	@Test(timeout = 1000)
	fun test28() {
		//language=TEXT
		assertEquals(120 + 230, Lice.run("((lambda a b (+ a b)) 120 230)"))
		assertEquals(120 * 230, Lice.run("((lambda a b (* a b)) 120 230)"))
		assertEquals(466, Lice.run("((lambda a (+ a a)) 233)"))
	}

	/**
	 * yes, it's true!
	 * expr(call by name) and lazy(call by need)!
	 */
	@Test(timeout = 1000)
	fun test29() {
		//language=TEXT
		assertFalse(true == Lice.run("""
((lazy unused
       "any-val")
  (|> (def side-effect true)
      233))

(def? side-effect)
"""))
		//language=TEXT
		assertEquals(12, Lice.run("""
(-> side-effect 10)
((expr used-twice
        (+ used-twice used-twice))
  (|> (-> side-effect (+ side-effect 1))
      233))

side-effect
"""))
		//language=TEXT
		assertEquals(11, Lice.run("""
(-> side-effect 10)
((lambda used-twice
         (+ used-twice used-twice))
  (|> (-> side-effect (+ side-effect 1))
      233))

side-effect
"""))
	}

	/**
	 * linked list
	 */
	@Test(timeout = 1000)
	fun test30() {
		assertEquals(233, Lice.run("([| ([|] 233 666 555 \"Fuck you\"))"))
		assertEquals(666, Lice.run("([| (|] ([|] 233 666 555 \"Fuck you\")))"))
	}

	/**
	 * function as parameter
	 */
	@Test(timeout = 1000)
	fun test31() {
		assertEquals(1, Lice.run("""
(defexpr fuck op (op true 1 2))
(fuck if)
"""))
		assertEquals(2, Lice.run("""
(defexpr fuck op (op true 1 2))
(fuck unless)
"""))
		assertEquals(10, Lice.run("""
(defexpr fuck op (op 1 2 3 4))
(fuck +)
"""))
	}
}
