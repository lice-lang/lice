package org.lice.repl

import org.lice.compiler.parse.buildNode
import org.lice.compiler.parse.mapAst
import org.lice.compiler.util.DEBUGGING
import org.lice.compiler.util.VERBOSE
import org.lice.core.SymbolList
import org.lice.lang.Echoer

/**
 * starting the read-eval-print-loop machine
 * Created by ice1000 on 2017/2/23.
 *
 * @author ice1000
 * @since 1.0.0
 */
class Repl(symbolList: SymbolList?) {
	var stackTrace: Throwable? = null
	val symbolList = symbolList ?: SymbolList(true)

	init {
		"""Lice language repl $VERSION_CODE
			|see: https://github.com/lice-lang/lice

			|回首向来萧瑟处，也无风雨也无晴。
			|Stay young stay simple, and make yourself naive.""".trimMargin()
		Echoer.echo(HINT)
		DEBUGGING = false
		VERBOSE = false
	}

	fun handle(str: String): Boolean {
		try {
			mapAst(buildNode(str), symbolList).eval()
		} catch(e: Throwable) {
			stackTrace = e
			Echoer.echolnErr(e.message ?: "")
		}
		print(HINT)
		return true
	}

	companion object HintHolder {
		val HINT = "Lice > "
	}
}
