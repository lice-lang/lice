package org.lice.parse

import org.junit.Test
import org.lice.core.SymbolList

class Sandbox {
	@Test(timeout = 1000)
	fun run() {
		val src = """
			(print ((if true + -) 111 111))
"""
		val l = Lexer(src)
		Parser.parseTokenStream(l).accept(SymbolList()).eval()
	}
}
