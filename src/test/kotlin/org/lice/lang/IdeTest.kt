package org.lice.lang

import org.junit.Test
import org.lice.core.SymbolList

class IdeTest {
	@Test
	fun testAllSymbols() {
		println(SymbolList.classPath)
		println(SymbolList.pathSeperator)
		println(SymbolList.preludeSymbols)
		println(SymbolList.preludeVariables)
	}
}