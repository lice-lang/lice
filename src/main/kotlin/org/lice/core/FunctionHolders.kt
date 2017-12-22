@file:Suppress("unused")

package org.lice.core

import org.lice.lang.Echoer
import org.lice.parse.*
import org.lice.util.cast
import java.io.File

class FunctionHolders(val symbolList: SymbolList) {
	fun `===`(it: List<Any?>) = (1 until it.size).all { i -> it[i] == it[i - 1] }
	fun `!==`(it: List<Any?>) = (1 until it.size).none { i -> it[i] == it[i - 1] }
	fun eval(ls: List<Any?>) = mapAst(buildNode(ls.first().toString()), symbolList = symbolList).eval()
	fun type(ls: List<Any?>) = ls.first()?.javaClass ?: Nothing::class.java
	fun `load-file`(ls: List<Any?>) = createRootNode(File(ls.first().toString()), symbolList).eval()
	fun print(ls: List<Any?>) = ls.forEach { Echoer.echo(it) }
	fun exit(ls: List<Any?>) = System.exit(0)
	fun sqrt(it: List<Any?>) = Math.sqrt((it.first() as Number).toDouble())
	fun sin(it: List<Any?>) = Math.sin((it.first() as Number).toDouble())
	fun rand(it: List<Any?>) = Math.random()
	fun `&&`(ls: List<Any?>) = ls.all(Any?::booleanValue)
	fun `||`(ls: List<Any?>) = ls.any(Any?::booleanValue)
	fun `!`(ls: List<Any?>) = ls.first().booleanValue().not()
	fun `&`(list: List<Any?>) = list.map { cast<Number>(it).toInt() }.reduce { last, self -> last and self }
	fun `|`(list: List<Any?>) = list.map { cast<Number>(it).toInt() }.reduce { last, self -> last or self }
	fun `^`(list: List<Any?>) = list.map { cast<Number>(it).toInt() }.reduce { last, self -> last xor self }
	fun `~`(it: List<Any?>) = (it.first() as Number).toInt().inv()
	fun `str-con`(it: List<Any?>) = it.joinToString(transform = Any?::toString, separator = "")
}
