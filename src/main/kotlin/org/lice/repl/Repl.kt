package org.lice.repl

import org.lice.compiler.parse.buildNode
import org.lice.compiler.parse.mapAst
import org.lice.compiler.util.DEBUGGING
import org.lice.compiler.util.VERBOSE
import org.lice.compiler.util.println
import org.lice.compiler.util.serr
import org.lice.core.SymbolList
import org.lice.lang.Echoer

/**
 * starting the read-eval-print-loop machine
 * Created by ice1000 on 2017/2/23.
 *
 * @author ice1000
 * @since 1.0.0
 */
class Repl {
	var stackTrace: Throwable? = null

	init {
		"""Lice language repl $VERSION_CODE
			|see: https://github.com/lice-lang/lice

			|回首向来萧瑟处,也无风雨也无晴。
			|Stay young stay simple, and make yourself naive.

			|for help please input: help"""
				.trimMargin()
				.println()
		Echoer.echo(HINT)
		DEBUGGING = false
		VERBOSE = false
	}

	@JvmOverloads
	fun handle(
			str: String,
			symbolList: SymbolList = SymbolList(true)): Boolean {
		if (str == "pst") {
			if (stackTrace != null) stackTrace?.printStackTrace()
			else "No stack trace.".println()
		} else try {
			mapAst(buildNode(str), symbolList).eval()
		} catch(e: Throwable) {
			stackTrace = e
			serr(e.message ?: "")
		}
		print("\n$HINT")
		return true
	}

	companion object HintHolder {
		val HINT = "Lice > "
	}
}
