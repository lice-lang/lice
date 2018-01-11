package org.lice

import org.jetbrains.annotations.TestOnly
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

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
		//language=Lice
		"""(print "fuck")""" evalTo null
		//language=Lice
		""""lover~lover~fucker~fucker~"""" evalTo "lover~lover~fucker~fucker~"
	}

	/**
	 * test for primitives literals
	 */
	@Test(timeout = 1000)
	fun test2() {
		//language=Lice
		"(print 233666)" evalTo null
		//language=Lice
		"233666" evalTo 233666
		//language=Lice
		"233666.0" evalTo 233666F
		//language=Lice
		"233666.0000000000000000000000000000000" evalTo 233666.0
		//language=Lice
		"true".shouldBeTrue()
		//language=Lice
		"false".shouldBeFalse()
		//language=Lice
		"null".shouldBeNull()
	}

	/**
	 * test for plus minus times devide
	 */
	@Test(timeout = 1000)
	fun test3() {
		//language=Lice
		"(+ 1 (+ 1 2 3))" evalTo 7
		//language=Lice
		"(- 1 (+ 1 2 3))" evalTo -5
		//language=Lice
		"(* 4 (+ 2 3))" evalTo 20
		//language=Lice
		"(+)" evalTo 0
		//language=Lice
		"(-)" evalTo 0
		//language=Lice
		"(* 4 (+ 2 3))" evalTo 20
	}

	/**
	 * test for big integers
	 */
	@Test(timeout = 1000)
	fun test4() {
		//language=Lice
		"(+ 10000000000000000000000000000000N 233)" evalTo BigInteger("10000000000000000000000000000233")
		//language=Lice
		"(- 10000000000000000000000000000000N 233)" evalTo BigInteger("9999999999999999999999999999767")
		//language=Lice
		"(* 10000000000000000000000000000000N 233)" evalTo BigInteger("2330000000000000000000000000000000")
		//language=Lice
		"(/ 10000000000000000000000000000000N 233)" evalTo BigInteger("42918454935622317596566523605")
		//language=Lice
		"(% 10000000000000000000000000000000N 233)" evalTo BigInteger("35")
		"0xDBEN" evalTo BigInteger(0xDBE.toString())
	}

	/**
	 * test for big decimals
	 */
	@Test(timeout = 1000)
	fun test5() {
		//language=Lice
		"(+ 10000000000000000000000000000000N 0.233)" evalTo BigDecimal("10000000000000000000000000000000.233")
		//language=Lice
		"(+ 100000000000000000000000000M 1)" evalTo BigDecimal("100000000000000000000000001")
		//language=Lice
		"(- 10000000000000000000000000000000N 0.233)" evalTo BigDecimal("9999999999999999999999999999999.767")
		//language=Lice
		"(- 100000000000000000000000000M 1)" evalTo BigDecimal("99999999999999999999999999")
		//language=Lice
		"(* 10000000000000000000000000000000N 0.233)" evalTo BigDecimal("2330000000000000000000000000000.000")
		//language=Lice
		"(* 100000000000000000000000000M 1)" evalTo BigDecimal("100000000000000000000000000")
		//language=Lice
		"(/ 10000000000000000000000000000000N 0.233)" evalTo BigDecimal("42918454935622317596566523605150")
		//language=Lice
		"(/ 100000000000000000000000000M 1)" evalTo BigDecimal("100000000000000000000000000")
		//language=Lice
		"(% 10000000000000000000000000000000N 0.233)" evalTo BigDecimal("0.05")
		//language=Lice
		"(% 100000000000000000000000000M 1)" evalTo BigDecimal("0")
	}

	/**
	 * comparision
	 */
	@Test(timeout = 1000)
	fun test6() {
		//language=Lice
		"(>= 9 8 7 7 6 6 6 5 4 3 1 -1)".shouldBeTrue()
		//language=Lice
		"(>= 9 8 7 7 6 6 6 5 8 3 1 -1)".shouldBeFalse()
	}

	/**
	 * string connection
	 */
	@Test(timeout = 1000)
	fun test7() {
		//language=Lice
		"""
(str-con "boy" "Next" "Door")
""" evalTo "boyNextDoor"
		//language=Lice
		"""
(format "%s%s%s" "boy" "Next" "Door")
""" evalTo "boyNextDoor"
	}

	/**
	 * parsing string
	 */
	@Test(timeout = 1000)
	fun test8() {
		//language=Lice
		"""
(str->int "0xDBE")
""" evalTo 0xDBE
	}

	/**
	 * string evaluation
	 */
	@Test(timeout = 1000)
	fun test9() {
		//language=Lice
		"""
(eval "(+ 1 1)")
""" evalTo 2
	}

	/**
	 * run/begin block
	 */
	@Test(timeout = 1000)
	fun test10() {
		//language=Lice
		"(|> (+ 1 1) (+ 2 2))" evalTo 4
	}

	/**
	 * force running
	 */
	@Test(timeout = 1000)
	fun test11() {
		//language=Lice
		"(force|> (+ () ()))".shouldBeNull()
		//language=Lice
		"()".shouldBeNull()
	}

	/**
	 * variable
	 */
	@Test(timeout = 1000)
	fun test12() {
		//language=Lice
		"""
(-> ice1000 233)

ice1000
""" evalTo 233
	}

	/**
	 * function
	 */
	@Test(timeout = 1000)
	fun test13() {
		//language=Lice
		"""
(defexpr ice1000 233)

(ice1000)
""" evalTo 233
		//language=Lice
		"""
(defexpr ice1000 a a)

(ice1000 233)
""" evalTo 233
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
""" evalTo 5
		//language=TEXT
		"""
(def in? ls a (> (count ls a) 0))

(def fib n (if (> 2 n)
               1
               (+ (fib (- n 1)) (fib (- n 2)))))

(fib 10)
""" evalTo 89
	}

	/**
	 * if condition
	 */
	@Test(timeout = 1000)
	fun test15() {
		//language=Lice
		"(if (>= 9 8 7 7 6 6 6 5 4 3 1 -1) 1 (+ 1 1))" evalTo 1
		//language=Lice
		"(if (>= 9 8 7 7 6 6 6 5 8 3 1 -1) 1 (+ 1 1))" evalTo 2
	}

	/**
	 * when condition
	 */
	@Test(timeout = 1000)
	fun test16() {
		"""(when
(!== 1 1), 233
(=== 1 1), 666
)""" evalTo 666
		"""(when
(!== 1 1), 233
(=== 2 1), 666
123
)""" evalTo 123
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
""" evalTo 9
	}

	/**
	 * lazy evaluation
	 */
	@Test(timeout = 1000)
	fun test18() {
		//language=Lice
		"""
(deflazy fuck a b b)

(-> lover 233)

(fuck (-> lover 666) ())

lover
""" evalTo 233
		//language=Lice
		"""
(def fuck a b b)

(-> lover 233)

(fuck (-> lover 666) ())

lover
""" evalTo 666
		//language=Lice
		"""
(deflazy fuck a b (|> b b))

(-> lover 233)

(fuck () (-> lover (+ lover 1)))
""" evalTo 233 + 1
	}

	/**
	 * assignment
	 */
	@Test(timeout = 1000)
	fun test19() {
		//language=Lice
		"""
(-> ice1k 233)
ice1k
""" evalTo 233
	}

	/**
	 * literals
	 */
	@Test(timeout = 1000)
	fun test20() {
		//language=Lice
		"null".shouldBeNull()
		//language=Lice
		"true".shouldBeTrue()
		//language=Lice
		"false".shouldBeFalse()
	}

	/**
	 * trigonometric
	 */
	@Test(timeout = 1000)
	fun test22() {
		val r = Random(System.currentTimeMillis()).nextDouble()
		//language=Lice
		assertEquals(Math.sin(r), Lice.run("(sin $r)") as Double, 1e-10)
	}

	/**
	 * nested expression
	 */
	@Test(timeout = 1000)
	fun test25() {
		//language=Lice
		"((((2))))" evalTo 2
		//language=Lice
		"(if true 0 1)" evalTo 0
	}

	/**
	 * returning an expression
	 */
	@Test(timeout = 1000)
	fun test26() {
		//language=Lice
		"((if false * /) 11 11)" evalTo 1
		//language=Lice
		"((if true * /) 11 11)" evalTo 121
	}

	/*
	 * (((if true + -)) 11 11)
	 * ((+) 11 11)
	 * (+ 11 11)
	 * 22
	 */
	@Test(timeout = 1000)
	fun test27() {
		//language=Lice
		"((if true + -) 11 11)" evalTo 22
		//language=Lice
		"((if true + -) 11 11)" evalTo 22
	}

	/**
	 * yes, it's true!
	 * lambda expression!
	 */
	@Test(timeout = 1000)
	fun test28() {
		//language=Lice
		"((lambda a b (+ a b)) 120 230)" evalTo 120 + 230
		//language=Lice
		"((lambda a b (* a b)) 120 230)" evalTo 120 * 230
		//language=Lice
		"((lambda a (+ a a)) 233)" evalTo 466
	}

	/**
	 * yes, it's true!
	 * expr(call by name) and lazy(call by need)!
	 */
	@Test(timeout = 1000)
	fun test29() {
		//language=Lice
		"""
((lazy unused
       "any-val")
  (|> (def side-effect true)
      233))

(def? side-effect)
""".shouldBeFalse()
		//language=Lice
		"""
(-> side-effect 10)
((expr used-twice
        (+ used-twice used-twice))
  (|> (-> side-effect (+ side-effect 1))
      233))

side-effect
""" evalTo 12
		//language=Lice
		"""
(-> side-effect 10)
((lambda used-twice
         (+ used-twice used-twice))
  (|> (-> side-effect (+ side-effect 1))
      233))

side-effect
""" evalTo 11
	}

	/**
	 * function as parameter
	 */
	@Test(timeout = 1000)
	fun test31() {
		//language=Lice
		"""
(defexpr fuck op (op true 1 2))
(fuck if)
""" evalTo 1
		//language=Lice
		"""
(deflazy unless condition a b (if condition b a))
(defexpr fuck op (op true 1 2))
(fuck unless)
""" evalTo 2
		//language=Lice
		"""
(defexpr fuck op (op 1 2 3 4))
(fuck +)
""" evalTo 10
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
		//language=Lice
		"((expr op (op 1 2)) +)" evalTo 3
		//language=Lice
		"((lazy op (op 1 2)) +)" evalTo 3
		//language=Lice
		"((lambda op (op 1 2)) +)" evalTo 3
		//language=Lice
		"""
(def fun a b
  (+ (* a a) (* b b)))
((lambda op (op 3 4)) fun)
""" evalTo 25
	}

	/**
	 * lambda as parameter
	 *
	 * sample: defining a fold
	 */
	@Test(timeout = 1000)
	fun test33() {
		//language=Lice
		"((lambda op (op 1 2)) (lambda a b (+ a b)))" evalTo 3
		//language=Lice
		"((lambda op (op 3 4)) (lambda a b (+ (* a a) (* b b))))" evalTo 25
	}

	@Test
	fun test34() {
		//language=Lice
		"""(format "Hello, %s", 233)""" evalTo "Hello, 233"
		//language=Lice
		"(int->hex 12)" evalTo "0x${12.toString(16)}"
		//language=Lice
		"(int->oct 12)" evalTo "0${12.toString(8)}"
		//language=Lice
		"(int->bin 12)" evalTo "0b${12.toString(2)}"
		//language=Lice
		"(sqrt 16)" evalTo 4.0
	}
}
