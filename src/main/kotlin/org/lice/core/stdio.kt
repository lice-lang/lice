/**
 * Created by ice1000 on 2017/2/25.
 *
 * @author ice1000
 * @since 3.1.5
 */
@file:Suppress("NOTHING_TO_INLINE")
@file:JvmName("Standard")
@file:JvmMultifileClass

package org.lice.core

import org.lice.compiler.util.InterpretException
import org.lice.lang.Echoer
import java.util.*

fun SymbolList.addStdioFunctions() {
	provideFunction("print") { ls ->
		ls.forEach { Echoer.echo(it) }
		if (ls.isNotEmpty()) ls.last() else null
	}
	provideFunction("print-err") { ls ->
		ls.forEach { Echoer.echoErr(it) }
		if (ls.isNotEmpty()) ls.last() else null
	}
	provideFunction("println") { ls ->
		ls.forEach { Echoer.echo(it) }
		Echoer.echoln()
		if (ls.isNotEmpty()) ls.last() else null
	}
	provideFunction("getInts") { ls ->
		(0..ls.first().toString().toInt()).map { i.nextInt() }
	}
	provideFunction("getBigInts") { ls ->
		(0..ls.first().toString().toInt()).map { i.nextBigInteger() }
	}
	provideFunction("getBigDecs") { ls ->
		(0..ls.first().toString().toInt()).map { i.nextBigDecimal() }
	}
}

val i = Scanner(System.`in`)
