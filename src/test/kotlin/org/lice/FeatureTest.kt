package org.lice

import org.jetbrains.annotations.TestOnly
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import org.lice.lang.Pair as LicePair

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
		"(print \"fuck\")" shouldBe "fuck"
		""""lover~lover~fucker~fucker~"""" shouldBe "lover~lover~fucker~fucker~"
	}

	/**
	 * test for primitives literals
	 */
	@Test(timeout = 1000)
	fun test2() {
		"(print 233666)" shouldBe 233666
		"233666" shouldBe 233666
		"233666.0" shouldBe 233666F
		"233666.0000000000000000000000000000000" shouldBe 233666.0
		"true".shouldBeTrue()
		"false".shouldBeFalse()
		"null".shouldBeNull()
	}

	/**
	 * test for plus minus times devide
	 */
	@Test(timeout = 1000)
	fun test3() {
		"(+ 1 (+ 1 2 3))" shouldBe 7
		"(- 1 (+ 1 2 3))" shouldBe -5
		"(* 4 (+ 2 3))" shouldBe 20
		"+" shouldBe 0
		"-" shouldBe 0
		"(+)" shouldBe 0
		"(-)" shouldBe 0
		"(* 4 (+ 2 3))" shouldBe 20
	}

	/**
	 * test for big integers
	 */
	@Test(timeout = 1000)
	fun test4() {
		"(+ 10000000000000000000000000000000N 233)" shouldBe BigInteger("10000000000000000000000000000233")
		"0xDBEN" shouldBe BigInteger(0xDBE.toString())
	}

	/**
	 * test for big decimals
	 */
	@Test(timeout = 1000)
	fun test5() {
		"(+ 10000000000000000000000000000000N 0.233)" shouldBe BigDecimal("10000000000000000000000000000000.233")
		"(+ 100000000000000000000000000M 1)" shouldBe BigDecimal("100000000000000000000000001")
	}

	/**
	 * comparision
	 */
	@Test(timeout = 1000)
	fun test6() {
		"(>= 9 8 7 7 6 6 6 5 4 3 1 -1)".shouldBeTrue()
		"(>= 9 8 7 7 6 6 6 5 8 3 1 -1)".shouldBeFalse()
	}

	/**
	 * string connection
	 */
	@Test(timeout = 1000)
	fun test7() {
		"""
(str-con "boy" "Next" "Door")
""" shouldBe "boyNextDoor"
	}

	/**
	 * parsing string
	 */
	@Test(timeout = 1000)
	fun test8() {
		"""
(str->int "0xDBE")
""" shouldBe 0xDBE
	}

	/**
	 * string evaluation
	 */
	@Test(timeout = 1000)
	fun test9() {
		"""
(eval "(+ 1 1)")
""" shouldBe 2
	}

	/**
	 * run/begin block
	 */
	@Test(timeout = 1000)
	fun test10() {
		"(|> (+ 1 1) (+ 2 2))" shouldBe 4
	}

	/**
	 * force running
	 */
	@Test(timeout = 1000)
	fun test11() {
		"(force|> (+ () ()))".shouldBeNull()
		"()".shouldBeNull()
	}

	/**
	 * variable
	 */
	@Test(timeout = 1000)
	fun test12() {
		"""
(-> ice1000 233)

ice1000
""" shouldBe 233
	}

	/**
	 * function
	 */
	@Test(timeout = 1000)
	fun test13() {
		"""
(defexpr ice1000 233)

ice1000
""" shouldBe 233
		//language=TEXT
		"""
(defexpr ice1000 a a)

(ice1000 233)
""" shouldBe 233
	}

	/**
	 * recursion
	 */
	@Test(timeout = 1000)
	fun test14() {
		//language=TEXT
		"""
(def gcd a b (if (=== b 0) a (gcd b (% a b))))

(gcd 15 20)
""" shouldBe 5
		//language=TEXT
		"""
(def in? ls a (> (count ls a) 0))

(def fib n (if (in? (list 0 1) n)
               1
               (+ (fib (- n 1)) (fib (- n 2)))))

(fib 10)
""" shouldBe 89
	}

	/**
	 * if condition
	 */
	@Test(timeout = 1000)
	fun test15() {
		"(if (>= 9 8 7 7 6 6 6 5 4 3 1 -1) 1 (+ 1 1))" shouldBe 1
		"(if (>= 9 8 7 7 6 6 6 5 8 3 1 -1) 1 (+ 1 1))" shouldBe 2
	}

	/**
	 * when condition
	 */
	@Test(timeout = 1000)
	fun test16() {
		"""(when
(!== 1 1), 233
(=== 1 1), 666
)""" shouldBe 666
		"""(when
(!== 1 1), 233
(=== 2 1), 666
123
)""" shouldBe 123
	}

	/**
	 * while loop
	 */
	@Test(timeout = 1000)
	fun test17() {
		"""
(def exp-mod a b m (|>
  (-> ret 1)

  (while (!= 0 b) (|>

    (if (!= 0 (& b 1))
      (-> ret (% (* a ret) m)))
    (-> b (/ b 2))
    (-> a (% (* a a) m))))
  ret))

(exp-mod 23 2 26)
""" shouldBe 9
	}

	/**
	 * lazy evaluation
	 */
	@Test(timeout = 1000)
	fun test18() {
		"""
(deflazy fuck a b b)

(-> lover 233)

(fuck (-> lover 666) ())

lover
""" shouldBe 233
		"""
(def fuck a b b)

(-> lover 233)

(fuck (-> lover 666) ())

lover
""" shouldBe 666
		//language=TEXT
		"""
(deflazy fuck a b (|> b b))

(-> lover 233)

(fuck () (-> lover (+ lover 1)))
""" shouldBe 233 + 1
	}

	/**
	 * assignment
	 */
	@Test(timeout = 1000)
	fun test19() {
		"""
(-> ice1k 233)

(<-> ice1k 666)
ice1k
""" shouldBe 233
		"""
(-> ice1k null)

(<-> ice1k 666)
ice1k
""".shouldBeNull()
		"""
(<-> ice1k 666)
ice1k
""" shouldBe 666
	}

	/**
	 * literals
	 */
	@Test(timeout = 1000)
	fun test20() {
		"null".shouldBeNull()
		"true".shouldBeTrue()
		"false".shouldBeFalse()
	}

	/**
	 * escape
	 */
	@Test(timeout = 1000)
	fun test21() {
		""""\n"""" shouldBe "\n"
		""""\r"""" shouldBe "\r"
		""""\b"""" shouldBe "\b"
		""""\t"""" shouldBe "\t"
		""""\'"""" shouldBe "\'"
		""""\\"""" shouldBe "\\"
// FIXME assertEquals("\"", Lice.run(""""\"""""))
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
		"([| (list 233 344 455 566))" shouldBe 233
		"([| ([|] 233 344 455 566))" shouldBe 233
	}

	/**
	 * count
	 */
	@Test(timeout = 1000)
	fun test24() {
		"(count (list 233 233 455 566) 233)" shouldBe 2
		"(count (.. 233 234) 1)" shouldBe 0
	}

	/**
	 * nested expression
	 */
	@Test(timeout = 1000)
	fun test25() {
		//language=TEXT
		"((((2))))" shouldBe 2
		//language=TEXT
		"((if true 0 1))" shouldBe 0
	}

	/**
	 * returning an expression
	 */
	@Test(timeout = 1000)
	fun test26() {
		//language=TEXT
		"((if false * /) 11 11)" shouldBe 1
		//language=TEXT
		"((if true * /) 11 11)" shouldBe 121
	}

	/**
	 * I'll show you the procedure:
	 *
	 * (((if true + -)) 11 11)
	 * ((+) 11 11)
	 * (+ 11 11)
	 * 22
	 */
	@Test(timeout = 1000)
	fun test27() {
		"((if true + -) 11 11)" shouldBe 22
		"(((if true + -)) 11 11)" shouldBe 22
		"+" shouldBe 0
		"-" shouldBe 0
		"*" shouldBe 1
	}

	/**
	 * yes, it's true!
	 * lambda expression!
	 */
	@Test(timeout = 1000)
	fun test28() {
		//language=TEXT
		"((lambda a b (+ a b)) 120 230)" shouldBe 120 + 230
		"((lambda a b (* a b)) 120 230)" shouldBe 120 * 230
		"((lambda a (+ a a)) 233)" shouldBe 466
	}

	/**
	 * yes, it's true!
	 * expr(call by name) and lazy(call by need)!
	 */
	@Test(timeout = 1000)
	fun test29() {
		//language=TEXT
		"""
((lazy unused
       "any-val")
  (|> (def side-effect true)
      233))

(def? side-effect)
""".shouldBeFalse()
		//language=TEXT
		"""
(-> side-effect 10)
((expr used-twice
        (+ used-twice used-twice))
  (|> (-> side-effect (+ side-effect 1))
      233))

side-effect
""" shouldBe 12
		//language=TEXT
		"""
(-> side-effect 10)
((lambda used-twice
         (+ used-twice used-twice))
  (|> (-> side-effect (+ side-effect 1))
      233))

side-effect
""" shouldBe 11
	}

	/**
	 * linked list
	 */
	@Test(timeout = 1000)
	fun test30() {
		"""([| ([|] 233 666 555 "Fuck you"))""" shouldBe 233
		"""([| (|] ([|] 233 666 555 "Fuck you")))""" shouldBe 666
	}

	/**
	 * function as parameter
	 */
	@Test(timeout = 1000)
	fun test31() {
		"""
(defexpr fuck op (op true 1 2))
(fuck if)
""" shouldBe 1
		"""
(deflazy unless condition a b (if condition b a))
(defexpr fuck op (op true 1 2))
(fuck unless)
""" shouldBe 2
		"""
(defexpr fuck op (op 1 2 3 4))
(fuck +)
""" shouldBe 10
		"""
(defexpr fuck op (op 1 2 3 4))
(fuck list)
""" shouldBe listOf(1, 2, 3, 4)
		"""
(defexpr fuck op (op 1 2 3 4))
(fuck [|])
""" shouldBe LicePair(1, LicePair(2, LicePair(3, 4)))
	}

	/**
	 * expr will keep the parameter everywhere
	 * lazy will eval params when first invoked
	 * lambda will eval params before invoked
	 *
	 * conclusion: if you want to pass 'function'
	 * as parameter, please use 'expr'.
	 */
	@Test(timeout = 1000)
	fun test32() {
		"((expr op (op 1 2)) +)" shouldBe 3
		"((lazy op (op 1 2)) +)" shouldBe 0
		"((lambda op (op 1 2)) +)" shouldBe 0
		//language=TEXT
		"""
(def fun a b
  (+ (* a a) (* b b)))
((expr op (op 3 4)) fun)
""" shouldBe 25
	}

	/**
	 * lambda as parameter
	 *
	 * sample: defining a fold
	 */
	@Test(timeout = 1000)
	fun test33() {
		"((expr op (op 1 2)) (lambda a b (+ a b)))" shouldBe 3
		"((expr op (op 3 4)) (lambda a b (+ (* a a) (* b b))))" shouldBe 25
		//language=TEXT
		"""
(defexpr fold ls init op
 (for-each index-var ls
   (-> init (op init index-var))))

(fold (.. 1 4) 0 +)
""" shouldBe 10
	}
}
