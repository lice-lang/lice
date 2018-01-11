package org.lice.parse

import org.intellij.lang.annotations.Language
import org.junit.Test

class ParserSandbox {
	@Test(timeout = 1000)
	fun run() {
		@Language("Lice")
		val srcCode = """
(def fibonacci n
	(if (== n 1)
		(1)
		(if (== n 2)
		(1)
		(+ (fibonacci (- n 1)) (fibonacci (- n 2))))))
(-> i 1)
(while (< i 20)
	(|>
		(print (fibonacci i))
		(print ("
"))
		(-> i (+ i 1))))
			"""
		val rootNode = Parser.parseTokenStream(Lexer(srcCode))
	}
}
