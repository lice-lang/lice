@file:Suppress("unused", "FunctionName")

package org.lice.core

import org.lice.lang.Echoer

class FunctionHolders(private val symbolList: SymbolList) {
	fun print(it: List<Any?>) = it.forEach(Echoer::echo)
	fun exit(it: List<Any?>) = System.exit(0)
	fun rand(it: List<Any?>) = Math.random()
	fun `&&`(it: List<Any?>) = it.all(Any?::booleanValue)
	fun `||`(it: List<Any?>) = it.any(Any?::booleanValue)
	fun `str-con`(it: List<Any?>) = it.joinToString(transform = Any?::toString, separator = "")
	fun `===`(it: List<Any?>) = (1 until it.size).all { i -> it[i] == it[i - 1] }
	fun `!==`(it: List<Any?>) = (1 until it.size).none { i -> it[i] == it[i - 1] }
	fun list(it: List<Any?>) = it
	fun array(it: List<Any?>) = it.toTypedArray()
	fun println(it: List<Any?>): Any? {
		it.forEach(Echoer::echo)
		Echoer.echo("\n")
		return it.lastOrNull()
	}
}
