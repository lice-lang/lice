package org.lice.lang

import org.junit.Test
import org.lice.core.SymbolList
import org.lice.model.*
import org.lice.println

class IdeTest {
	@Test
	fun testAllSymbols() {
		println(SymbolList.classPath)
		println(SymbolList.pathSeperator)
		println(SymbolList.preludeSymbols)
		println(SymbolList.preludeVariables)
		ValueNode(1, MetaData.EmptyMetaData)
				.toString()
				.println()
		SymbolNode(SymbolList(), "a", MetaData.EmptyMetaData)
				.toString()
				.println()
	}
}