package org.lice.parse

import junit.framework.TestCase.assertEquals
import org.junit.Test

class IntegratedTest {
	@Test(timeout = 500)
	fun Test1() {
		val srcCode = """
(def fibonacci n
	(if (== n 1)
		(1)
		(if (== n 2)
		(1)
		(+ (fibonacci (- n 1)) (fibonacci (- n 2))))))
(-> i 1)
(while (< i 20)
	(|>
		(print (fibonacci i))
		(print ("
"))
		(-> i (+ i 1))))
"""
		Parser.parseTokenStream(Lexer(srcCode)).accept(Sema()).eval()
	}

	@Test(timeout = 500)
	fun Test2() {
		val srcCode = """
(print (+ 1 1) "\n")
(print (- 10N 1.0) "\n")
(print (/ 10.2M 5) "\n")
(print (/ 10 5.0) "\n")
(print (* 10 5.2M) "\n")

(alias + plus)
(undef +)
(alias plus +)
(print (+ 1 2) "\n")

(type 23)

(-> ass 10)

(str->sym "ass")

(print (format "ass %s can", "we") "
")
"""
		Parser.parseTokenStream(Lexer(srcCode)).accept(Sema()).eval()
	}

	@Test(timeout = 500)
	fun Test3() {
		/// Intentionally empty translation unit
		val srcCode = """
"""
		Parser.parseTokenStream(Lexer(srcCode)).accept(Sema()).eval()
	}

	@Test(timeout = 500)
	fun Test4() {
		val srcCode = """
; defining functions
(def check
		 (if (def? println)
				 (print "defined: println.
")
			 (print "undefined: println.
")))

; calling functions
(check)

(def println anything
		 (print anything "
"))

(check)

; plus
(println (+ 1 1))

(println (|| true false))

; defining call-by-name functions
(defexpr unless cond block (|>
  (if (! cond) (block))))

; calling that function
(unless false (lambda (println "on! false")))

; loops
(def loop count block (|>
		 (-> i 0)
		 (while (< i count) (|> (block i)
		 (-> i (+ i 1))))))

(loop 20 (lambda i (println i)))

; let
(defexpr let x y block (|>
		(-> x y)
		(block)
		(undef x)))

(let reimu 100 (lambda (|>
		(print x))))

; extern functions
; must be a static Java function
(extern "java.util.Objects" "equals")

; calling the extern function
(equals 1 1)
"""
		Parser.parseTokenStream(Lexer(srcCode)).accept(Sema()).eval()
	}

	@Test(timeout = 500)
	fun Test5() {
		assertEquals(233, Parser.parseTokenStream(Lexer("233")).accept(Sema()).eval())
		assertEquals(0x233, Parser.parseTokenStream(Lexer("0x233")).accept(Sema()).eval())
		assertEquals(0b10101, Parser.parseTokenStream(Lexer("0b10101")).accept(Sema()).eval())
	}
}
