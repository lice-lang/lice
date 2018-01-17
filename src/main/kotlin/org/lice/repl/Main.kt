@file:JvmName("Main")

package org.lice.repl

import org.lice.Lice
import org.lice.core.SymbolList
import org.lice.lang.Echoer
import java.nio.file.Paths

/**
 * @author ice1000
 */

fun Array<String>.main() {
	Echoer.openOutput()
	if (isEmpty()) println("Please specify an input file.")
	else Lice.run(Paths.get(first()), SymbolList())
}
