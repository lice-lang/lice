package org.lice.parse

import org.junit.Test

class ParserSandbox {
	@Test(timeout = 1000)
	fun run() {
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
		val l = Lexer(srcCode)
		val rootNode = Parser.parseTokenStream(l)
	}
}
