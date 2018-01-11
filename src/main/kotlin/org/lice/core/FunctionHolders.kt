@file:Suppress("unused")

package org.lice.core

import org.lice.Lice
import org.lice.lang.Echoer
import org.lice.util.cast
import java.io.File
import java.util.*

class FunctionHolders(val symbolList: SymbolList) {
	fun `===`(it: List<Any?>) = (1 until it.size).all { i -> it[i] == it[i - 1] }
	fun `!==`(it: List<Any?>) = (1 until it.size).none { i -> it[i] == it[i - 1] }
	fun eval(ls: List<Any?>) = Lice.run(ls.first().toString(), symbolList = symbolList)
	fun type(ls: List<Any?>) = ls.first()?.javaClass ?: Nothing::class.java
	fun `load-file`(ls: List<Any?>) = Lice.run(File(ls.first().toString()), symbolList)
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

	private val liceScanner = Scanner(System.`in`)
	fun println(ls: List<Any?>): Any? {
		ls.forEach { Echoer.echo(it) }
		Echoer.echo("\n")
		return if (ls.isNotEmpty()) ls.last() else null
	}

	fun getInts(ls: List<Any?>) = (1..(ls.first() ?: 1).toString().toInt()).map { liceScanner.nextInt() }
	fun getFloats(ls: List<Any?>) = (1..(ls.first() ?: 1).toString().toInt()).map { liceScanner.nextFloat() }
	fun getDoubles(ls: List<Any?>) = (1..(ls.first() ?: 1).toString().toInt()).map { liceScanner.nextDouble() }
	fun getLines(ls: List<Any?>) = (1..(ls.first() ?: 1).toString().toInt()).map { liceScanner.nextLine() }
	fun getTokens(ls: List<Any?>) = (1..(ls.first() ?: 1).toString().toInt()).map { liceScanner.next() }
	fun getBigInts(ls: List<Any?>) = (1..(ls.first() ?: 1).toString().toInt()).map { liceScanner.nextBigInteger() }
	fun getBigDecs(ls: List<Any?>) = (1..(ls.first() ?: 1).toString().toInt()).map { liceScanner.nextBigDecimal() }

	fun list(ls: List<Any?>) = ls
	fun size(it: List<Any?>) = (it.first() as? Iterable<*>)?.count() ?: -1
	fun reverse(ls: List<Any?>) = (ls.first() as? Iterable<*>)?.reversed()
	fun split(ls: List<Any?>): List<String> {
		val str = ls.first()
		val regex = ls[1]
		return str.toString().split(regex.toString()).toList()
	}

	fun count(ls: List<Any?>): Int {
		val e = ls[1]
		return (ls.first() as? Iterable<*>)?.count { it == e } ?: -1
	}

}
