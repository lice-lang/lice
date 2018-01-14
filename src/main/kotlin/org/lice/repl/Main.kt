@file:JvmName("Main")

package org.lice.repl

import org.lice.Lice
import org.lice.core.SymbolList
import org.lice.lang.Echoer
import java.nio.file.Paths

/**
 * @author ice1000
 */

fun main(args: Array<String>) {
	Echoer.openOutput()
	if (args.isEmpty()) println("Please specify an input file.")
	else Lice.run(Paths.get(args.first()), SymbolList())
}
