package org.lice.parse

import org.intellij.lang.annotations.Language
import org.junit.Test
import org.lice.Lice
import org.lice.core.SymbolList
import org.lice.evalTo
import kotlin.test.assertFails

class IntegratedTest {
	@Test
	fun test1() {
		@Language("Lice")
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
		(print ("\n"))
		(-> i (+ i 1))))
(fibonacci 10)
"""
		srcCode evalTo 55
	}

	@Test
	fun test2() {
		@Language("Lice")
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

(print (format "ass %s can", "we") "\n")
"""
		Parser.parseTokenStream(Lexer(srcCode)).accept(Sema()).eval()
	}

	@Test
	fun test3() {
		/// Intentionally empty translation unit
		val srcCode = """
"""
		srcCode evalTo null
	}

	@Test
	fun test4() {
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
		Lice.runBarely(srcCode)
	}

	@Test
	fun test5() {
		"233" evalTo 233
		"0x233" evalTo 0x233
		"0b10101" evalTo 0b10101
	}

	@Test
	fun failTests() {
		assertFails { Lice.runBarely("1a") }
		assertFails { Lice.runBarely("0xz") }
		assertFails { Lice.runBarely("0b2") }
		assertFails { Lice.runBarely("0o9") }
		assertFails { Lice.runBarely("undefined-variable") }
		assertFails { Lice.runBarely("(+ (->str 1))") }
		assertFails { Lice.runBarely("(def)") }
		assertFails { Lice.runBarely("(def name)") }
		assertFails { Lice.runBarely("(-> name)") }
		assertFails { Lice.runBarely("(; 1)") }
		assertFails { Lice.runBarely(""""\g"""") }
		assertFails { Lice.runBarely("(") }
		assertFails { Lice.runBarely(")") }
		assertFails { Lice.runBarely("\"") }
		assertFails { SymbolList().extractLiceFunction("undefined-function")(emptyList()) }
		assertFails { SymbolList().extractLiceVariable("undefined-variable") }
	}
}
