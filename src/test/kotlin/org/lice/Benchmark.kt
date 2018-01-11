package org.lice

import org.intellij.lang.annotations.Language
import org.junit.Test
import org.lice.core.SymbolList
import org.lice.parse.*

class Benchmark {
	companion object {
		private val map = hashMapOf<String, Any>()
		@JvmStatic
		fun java() {
			val let = { x: String, y: Any, block: (Map<String, Any>) -> Unit ->
				map[x] = y
				block(map)
				map.remove(x)
			}
			let("reimu", 100) { 233 + it["reimu"] as Int }
		}

		const val cnt = 200000
		@Language("Lice")
		private const val core = """
(defexpr let x y block (|>
			(-> x y)
			(block)
			(undef x)))
(let reimu 100 (lambda (|>
		x)))
"""

		@Language("Lice")
		const val func = """
(def codes-to-run (|>
$core))
"""

		@Language("Lice")
		const val code = """
; loops
(def loop count block (|>
		 (-> i 0)
		 (while (< i count) (|> (block i)
		 (-> i (+ i 1))))))

(loop $cnt (lambda i (|> $core)))

(print "loop count: " i)
"""

		@Language("Lice")
		const val code2 = """
; loops
(def loop count block (|>
		 (-> i 0)
		 (while (< i count) (|> (block i)
		 (-> i (+ i 1))))))

(loop $cnt (lambda i (code)))

(print "loop count: " i)
"""

		@Language("Lice")
		const val code3 = """
(extern "org.lice.Benchmark" "java")

; loops
(def loop count block (|>
		 (-> i 0)
		 (while (< i count) (|> (block i)
		 (-> i (+ i 1))))))

(loop $cnt (lambda i (java)))

(print "loop count: " i)
"""
	}

	private val lice3 = Parser.parseTokenStream(Lexer(code3)).accept(Sema())
	private val lice = Parser.parseTokenStream(Lexer(code)).accept(Sema())
	private val lice2 = Parser.parseTokenStream(Lexer(code2)).accept(Sema(
			SymbolList().apply { provideFunction("code") { java() } }))

	@Test
	fun benchmarkLice() {
		lice.eval()
	}

	@Test
	fun benchmarkLiceCallJava() {
		lice2.eval()
	}

	@Test
	fun benchmarkJavaCallLice() {
		val symbols = SymbolList()
		Lice.run(func, symbols)
		val codes = symbols.extractLiceFunction("codes-to-run")
		repeat(cnt) { codes.invoke(emptyList()) }
	}

	@Test
	fun benchmarkJava() {
		val loop = { count: Int, block: (Int) -> Unit ->
			var i = 0
			while (i < count) {
				block(i)
				i += 1
			}
		}
		loop(cnt) { java() }
	}

	@Test
	fun benchmarkExtern() {
		lice3.eval()
	}

}