package org.lice.repl

import org.lice.compiler.parse.buildNode
import org.lice.compiler.parse.mapAst
import org.lice.compiler.util.println
import org.lice.core.SymbolList
import org.lice.lang.Echoer

/**
 * starting the read-eval-print-loop machine
 * Created by ice1000 on 2017/2/23.
 *
 * @author ice1000
 * @since 1.0.0
 */
class Repl
@JvmOverloads
constructor(val symbolList: SymbolList = SymbolList(true)) {

	init {
		"""Lice language repl $VERSION_CODE
			|see: https://github.com/lice-lang/lice

			|剑未佩妥，出门已是江湖。千帆过尽，归来仍是少年。"""
				.trimMargin()
				.println()
		Echoer.echo(HINT)
	}

	fun handle(str: String): Boolean {
		try {
			mapAst(buildNode(str), symbolList).eval()
		} catch(e: Throwable) {
//			stackTrace = e
			Echoer.echolnErr(e.message ?: "")
		}
		Echoer.echo(HINT)
		return true
	}

	companion object HintHolder {
		const val HINT = "|> "
	}
}
