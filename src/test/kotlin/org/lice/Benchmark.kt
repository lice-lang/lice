package org.lice

import org.junit.Test
import org.lice.core.Func
import org.lice.core.SymbolList
import org.lice.model.MetaData
import org.lice.parse.buildNode
import org.lice.parse.mapAst

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
		//language=TEXT
		private const val core = """
(defexpr let x y block (|>
			(-> x y)
			(block)
			(undef x)))
(let reimu 100 (lambda (|>
		x)))
"""

		//language=TEXT
		const val func = """
(def codes-to-run (|>
$core))
"""

		//language=TEXT
		const val code = """
; loops
(def loop count block (|>
		 (-> i 0)
		 (while (< i count) (|> (block i)
		 (-> i (+ i 1))))))

(loop $cnt (lambda i (|> $core)))

(print "loop count: " i)
"""

		//language=TEXT
		const val code2 = """
; loops
(def loop count block (|>
		 (-> i 0)
		 (while (< i count) (|> (block i)
		 (-> i (+ i 1))))))

(loop $cnt (lambda i (code)))

(print "loop count: " i)
"""

		//language=TEXT
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

	val lice3 = mapAst(node = buildNode(code3))
	val lice = mapAst(node = buildNode(code))
	val lice2 = mapAst(node = buildNode(code2), symbolList = SymbolList.with {
		provideFunction("code") { java() }
	})

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
		val codes = symbols.getVariable("codes-to-run") as Func
		repeat(cnt) {
			codes.invoke(MetaData.EmptyMetaData, emptyList())
		}
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