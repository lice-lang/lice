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
		(1..(ls.first() ?: 1).toString().toInt()).map { liceScanner.nextInt() }
	}
	provideFunction("getFloats") { ls ->
		(1..(ls.first() ?: 1).toString().toInt()).map { liceScanner.nextFloat() }
	}
	provideFunction("getDoubles") { ls ->
		(1..(ls.first() ?: 1).toString().toInt()).map { liceScanner.nextDouble() }
	}
	provideFunction("getLines") { ls ->
		(1..(ls.first() ?: 1).toString().toInt()).map { liceScanner.nextLine() }
	}
	provideFunction("getTokens") { ls ->
		(1..(ls.first() ?: 1).toString().toInt()).map { liceScanner.next() }
	}
	provideFunction("getBigInts") { ls ->
		(1..(ls.first() ?: 1).toString().toInt()).map { liceScanner.nextBigInteger() }
	}
	provideFunction("getBigDecs") { ls ->
		(1..(ls.first() ?: 1).toString().toInt()).map { liceScanner.nextBigDecimal() }
	}
}

val liceScanner = Scanner(System.`in`)
