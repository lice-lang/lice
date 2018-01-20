@file:Suppress("FunctionName", "unused")

package org.lice.core

import org.lice.Lice
import org.lice.lang.Echoer
import org.lice.lang.NumberOperator
import org.lice.model.*
import org.lice.parse.*
import org.lice.util.*
import org.lice.util.InterpretException.Factory.notSymbol
import org.lice.util.InterpretException.Factory.tooFewArgument
import java.lang.reflect.Modifier
import java.nio.file.Paths
import java.util.*

/**
 * `$` in the function names will be replaced with `>`,
 * `&` in the function names will be replaced with `<`,
 * `_` in the function names will be replaced with `/`.
 * `#` in the function names will be replaced with `.`.
 * `{` in the function names will be replaced with `[`.
 * `}` in the function names will be replaced with `]`.
 * @author ice1000
 */
class FunctionMangledHolder(private val symbolList: SymbolList) {
	fun `|$`(meta: MetaData, ls: List<Any?>) = ls.lastOrNull()
	fun `-$str`(meta: MetaData, it: List<Any?>) = it.first(meta).toString()
	fun `-$double`(meta: MetaData, it: List<Any?>) = cast<Number>(it.first(meta)).toDouble()
	fun `-$int`(meta: MetaData, it: List<Any?>) = cast<Number>(it.first(meta)).toInt()
	fun `&`(meta: MetaData, ls: List<Any?>) =
			(1 until ls.size).all { NumberOperator.compare(cast(ls[it - 1], meta), cast(ls[it], meta), meta) < 0 }

	fun `$`(meta: MetaData, ls: List<Any?>) =
			(1 until ls.size).all { NumberOperator.compare(cast(ls[it - 1], meta), cast(ls[it], meta), meta) > 0 }

	fun `&=`(meta: MetaData, ls: List<Any?>) =
			(1 until ls.size).all { NumberOperator.compare(cast(ls[it - 1], meta), cast(ls[it], meta), meta) <= 0 }

	fun `$=`(meta: MetaData, ls: List<Any?>) =
			(1 until ls.size).all { NumberOperator.compare(cast(ls[it - 1], meta), cast(ls[it], meta), meta) >= 0 }

	fun `_`(meta: MetaData, ls: List<Any?>): Number {
		val init = cast<Number>(ls.first(meta), meta)
		return when (ls.size) {
			0 -> 1
			1 -> init
			else -> ls.drop(1)
					.fold(NumberOperator(init)) { sum, value ->
						if (value is Number) sum.div(value, meta)
						else InterpretException.typeMisMatch("Number", value, meta)
					}.result
		}
	}

	fun `str-$int`(meta: MetaData, ls: List<Any?>): Int {
		val res = ls.first(meta).toString()
		return when {
			res.isOctInt() -> res.toOctInt()
			res.isInt() -> res.toInt()
			res.isBinInt() -> res.toBinInt()
			res.isHexInt() -> res.toHexInt()
			else -> throw InterpretException("give string: \"$res\" cannot be parsed as a number!", meta)
		}
	}

	fun `int-$hex`(meta: MetaData, ls: List<Any?>): String {
		val a = ls.first()
		return if (a is Number) "0x${Integer.toHexString(a.toInt())}"
		else InterpretException.typeMisMatch("Int", a, meta)
	}

	fun `int-$bin`(meta: MetaData, ls: List<Any?>): String {
		val a = ls.first()
		return if (a is Number) "0b${Integer.toBinaryString(a.toInt())}"
		else InterpretException.typeMisMatch("Int", a, meta)
	}

	fun `int-$oct`(meta: MetaData, ls: List<Any?>): String {
		val a = ls.first()
		return if (a is Number) "0o${Integer.toOctalString(a.toInt())}"
		else InterpretException.typeMisMatch("Int", a, meta)
	}

	fun `join-$str`(meta: MetaData, ls: List<Any?>) =
			cast<Iterable<*>>(ls.first()).joinToString(ls.getOrNull(1)?.toString().orEmpty())

	fun `{|}`(meta: MetaData, ls: List<Any?>) = ls.reduceRight(::Pair)
	fun `{|`(meta: MetaData, ls: List<Any?>): Any? {
		val a = ls.first(meta)
		return when (a) {
			is Pair<*, *> -> a.first
			is Iterable<*> -> a.first()
			is Array<*> -> a.first()
			else -> null
		}
	}

	fun `|}`(meta: MetaData, ls: List<Any?>): Any? {
		val a = ls.first(meta)
		return when (a) {
			is Pair<*, *> -> a.second
			is Iterable<*> -> a.drop(1)
			is Array<*> -> a.drop(1)
			else -> null
		}
	}

	fun `##`(metaData: MetaData, ls: List<Any?>): Iterable<Int> {
		if (ls.size < 2) tooFewArgument(2, ls.size, metaData)
		val a = ls.first()
		val b = ls[1]
		return when {
			a is Number && b is Number -> {
				val begin = a.toInt()
				val end = b.toInt()
				if (begin <= end) (begin..end)
				else (end..begin).reversed()
			}
			else -> InterpretException.typeMisMatch("Number", a as? Number ?: b, metaData)
		}
	}

	fun `list-$array`(meta: MetaData, it: List<Any?>) = cast<List<*>>(it.first(meta)).toTypedArray()
	fun `array-$list`(meta: MetaData, it: List<Any?>) = cast<Array<*>>(it.first(meta)).toList()
	fun `-$chars`(meta: MetaData, it: List<Any?>) = it.joinToString("").toCharArray()
}

class FunctionHolders(private val symbolList: SymbolList) {
	fun print(it: List<Any?>) = it.forEach(Echoer::echo)
	fun type(it: List<Any?>) = it.firstOrNull()?.javaClass ?: Nothing::class.java
	fun exit(it: List<Any?>) = System.exit(it.firstOrNull() as? Int ?: 0)
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

class FunctionWithMetaHolders(private val symbolList: SymbolList) {
	fun `-`(meta: MetaData, it: List<Any?>) = when (it.size) {
		0 -> 0
		1 -> it.first(meta)
		else -> it.drop(1).fold(NumberOperator(it.first() as Number)) { sum, value ->
			if (value is Number) sum.minus(value, meta)
			else InterpretException.typeMisMatch("Number", value, meta)
		}.result
	}

	fun `+`(meta: MetaData, it: List<Any?>) =
			it.fold(NumberOperator(0)) { sum, value ->
				if (value is Number) sum.plus(value, meta)
				else InterpretException.typeMisMatch("Number", value, meta)
			}.result

	fun extern(meta: MetaData, it: List<Any?>): Any? {
		val name = it[1, meta].toString()
		val clazz = it.first(meta).toString()
		val method = Class.forName(clazz).declaredMethods
				.firstOrNull { Modifier.isStatic(it.modifiers) && it.name == name }
				?: throw UnsatisfiedLinkError("Method $name not found for class $clazz\nat line: ${meta.beginLine}")
		symbolList.provideFunction(name) { runReflection { method.invoke(null, *it.toTypedArray()) } }
		return name
	}

	fun `==`(meta: MetaData, ls: List<Any?>) = (1 until ls.size)
			.all { NumberOperator.compare(cast(ls[it - 1]), cast(ls[it]), meta) == 0 }

	fun `!=`(meta: MetaData, ls: List<Any?>) = (1 until ls.size)
			.none { NumberOperator.compare(cast(ls[it - 1]), cast(ls[it]), meta) == 0 }

	fun `%`(meta: MetaData, ls: List<Any?>) = when (ls.size) {
		0 -> 0
		1 -> ls.first()
		else -> ls.drop(1)
				.fold(NumberOperator(cast(ls.first()))) { sum, value ->
					if (value is Number) sum.rem(value, meta)
					else InterpretException.typeMisMatch("Number", value, meta)
				}.result
	}

	fun `*`(meta: MetaData, ls: List<Any?>) = ls.fold(NumberOperator(1)) { sum, value ->
		if (value is Number) sum.times(value, meta)
		else InterpretException.typeMisMatch("Number", value, meta)
	}.result

	fun format(meta: MetaData, ls: List<Any?>) =
			String.format(ls.first(meta).toString(), *ls.subList(1, ls.size).toTypedArray())

	fun sqrt(meta: MetaData, it: List<Any?>) = Math.sqrt(cast<Number>(it.first(meta)).toDouble())
	fun sin(meta: MetaData, it: List<Any?>) = Math.sin(cast<Number>(it.first(meta)).toDouble())
	fun cos(meta: MetaData, it: List<Any?>) = Math.cos(cast<Number>(it.first(meta)).toDouble())
	fun tan(meta: MetaData, it: List<Any?>) = Math.tan(cast<Number>(it.first(meta)).toDouble())
	fun asin(meta: MetaData, it: List<Any?>) = Math.asin(cast<Number>(it.first(meta)).toDouble())
	fun acos(meta: MetaData, it: List<Any?>) = Math.acos(cast<Number>(it.first(meta)).toDouble())
	fun atan(meta: MetaData, it: List<Any?>) = Math.atan(cast<Number>(it.first(meta)).toDouble())
	fun sinh(meta: MetaData, it: List<Any?>) = Math.sinh(cast<Number>(it.first(meta)).toDouble())
	fun cosh(meta: MetaData, it: List<Any?>) = Math.cosh(cast<Number>(it.first(meta)).toDouble())
	fun tanh(meta: MetaData, it: List<Any?>) = Math.tanh(cast<Number>(it.first(meta)).toDouble())
	fun exp(meta: MetaData, it: List<Any?>) = Math.exp(cast<Number>(it.first(meta)).toDouble())
	fun log(meta: MetaData, it: List<Any?>) = Math.log(cast<Number>(it.first(meta)).toDouble())
	fun log10(meta: MetaData, it: List<Any?>) = Math.log10(cast<Number>(it.first(meta)).toDouble())
	fun eval(meta: MetaData, it: List<Any?>) = Lice.run(it.first(meta).toString(), symbolList = symbolList)
	fun `load-file`(meta: MetaData, it: List<Any?>) = Lice.run(Paths.get(it.first(meta).toString()), symbolList)
	fun `!`(meta: MetaData, it: List<Any?>) = it.first(meta).booleanValue().not()
	fun `~`(meta: MetaData, it: List<Any?>) = cast<Int>(it.first(meta)).inv()
	fun `!!`(meta: MetaData, it: List<Any?>): Any? {
		val a = it.first(meta)
		return when (a) {
			is Iterable<*> -> a.toList()[cast(it[1])]
			is Array<*> -> a[cast(it[1])]
			else -> null
		}
	}

	private val liceScanner = Scanner(System.`in`)
	fun getInts(meta: MetaData, it: List<Any?>) = (1..cast(it.first(meta) ?: 1)).map { liceScanner.nextInt() }
	fun getFloats(meta: MetaData, it: List<Any?>) = (1..cast(it.first(meta))).map { liceScanner.nextFloat() }
	fun getDoubles(meta: MetaData, it: List<Any?>) = (1..cast(it.first(meta))).map { liceScanner.nextDouble() }
	fun getLines(meta: MetaData, it: List<Any?>) = (1..cast(it.first(meta))).map { liceScanner.nextLine() }
	fun getTokens(meta: MetaData, it: List<Any?>) = (1..cast(it.first(meta))).map { liceScanner.next() }
	fun getBigInts(meta: MetaData, it: List<Any?>) = (1..cast(it.first(meta))).map { liceScanner.nextBigInteger() }
	fun getBigDecs(meta: MetaData, it: List<Any?>) = (1..cast(it.first(meta))).map { liceScanner.nextBigDecimal() }
	fun `in?`(meta: MetaData, it: List<Any?>) =
			it[1, meta] in cast<Iterable<*>>(it.first(meta).let { (it as? Array<*>)?.toList() ?: it })

	fun size(meta: MetaData, it: List<Any?>) = cast<Iterable<*>>(it.first(meta)).count()
	fun last(meta: MetaData, it: List<Any?>) = cast<Iterable<*>>(it.first(meta)).last()
	fun reverse(meta: MetaData, it: List<Any?>) = cast<Iterable<*>>(it.first(meta)).reversed()
	fun distinct(meta: MetaData, it: List<Any?>) = cast<Iterable<*>>(it.first(meta)).distinct()
	fun subtract(meta: MetaData, it: List<Any?>) = cast<Iterable<*>>(it.first(meta)).subtract(cast(it[1, meta]))
	fun intersect(meta: MetaData, it: List<Any?>) = cast<Iterable<*>>(it.first(meta)).intersect(cast(it[1, meta]))
	fun union(meta: MetaData, it: List<Any?>) = cast<Iterable<*>>(it.first(meta)).union(cast(it[1, meta]))
	fun `++`(meta: MetaData, it: List<Any?>) = cast<Iterable<*>>(it.first(meta)) + cast<Iterable<*>>(it[1, meta])
	fun sort(meta: MetaData, it: List<Any?>) = cast<Iterable<Comparable<Comparable<*>>>>(it.first(meta)).sorted()
	fun split(meta: MetaData, it: List<Any?>) = it.first(meta).toString().split(it[1].toString()).toList()
	fun count(meta: MetaData, it: List<Any?>) =
			it[1, meta].let { e -> cast<Iterable<*>>(it.first(meta)).count { it == e } }

	fun `&`(meta: MetaData, it: List<Any?>) =
			it.map { cast<Number>(it, meta).toInt() }.reduce { last, self -> last and self }

	fun `|`(meta: MetaData, it: List<Any?>) =
			it.map { cast<Number>(it, meta).toInt() }.reduce { last, self -> last or self }

	fun `^`(meta: MetaData, it: List<Any?>) =
			it.map { cast<Number>(it, meta).toInt() }.reduce { last, self -> last xor self }
}

/**
 * `$` in the function names will be replaced with `>`.
 * @author ice1000
 */
class FunctionDefinedMangledHolder(private val symbolList: SymbolList) {
	fun `def?`(meta: MetaData, it: List<Node>): Node {
		val a = (it.first(meta) as? SymbolNode ?: InterpretException.notSymbol(meta)).name
		return ValueNode(symbolList.isVariableDefined(a), meta)
	}

	fun `force|$`(meta: MetaData, ls: List<Node>): Node {
		var ret: Any? = null
		forceRun { ls.forEach { node -> ret = node.eval() } }
		return ValueNode(ret, meta)
	}

	fun `str-$sym`(meta: MetaData, ls: List<Node>) = SymbolNode(symbolList, ls.first(meta).eval().toString(), meta)
	fun `sym-$str`(meta: MetaData, ls: List<Node>): Node {
		val a = ls.first()
		return if (a is SymbolNode) ValueNode(a.name, meta)
		else InterpretException.typeMisMatch("Symbol", a, meta)
	}

	fun `-$`(meta: MetaData, ls: List<Node>): Node {
		if (ls.size < 2) tooFewArgument(2, ls.size, meta)
		symbolList.defineVariable(cast<SymbolNode>(ls.first()).name, ValueNode(ls[1].eval()))
		return ls.first()
	}
}

class FunctionDefinedHolder(private val symbolList: SymbolList) {
	fun `if`(meta: MetaData, ls: List<Node>): Node {
		if (ls.size < 2) tooFewArgument(2, ls.size, meta)
		val a = ls.first().eval()
		val condition = a.booleanValue()
		return when {
			condition -> ls[1]
			ls.size >= 3 -> ls[2]
			else -> ValueNode(null, meta)
		}
	}

	fun `when`(meta: MetaData, ls: List<Node>): Node {
		for (i in (0..ls.size - 2) step 2) {
			val a = ls[i].eval()
			val condition = a.booleanValue()
			if (condition) return ls[i + 1]
		}
		return if (ls.size % 2 == 0) ValueNode(null, meta) else ls.last()
	}

	fun `while`(meta: MetaData, ls: List<Node>): Node {
		if (ls.size < 2) tooFewArgument(2, ls.size, meta)
		var a = ls.first().eval()
		var ret: Node = ValueNode(null, meta)
		while (a.booleanValue()) {
			// execute loop
			ret.eval()
			ret = ls[1]
			// update a
			a = ls.first().eval()
		}
		return ret
	}

	fun undef(meta: MetaData, it: List<Node>): Node {
		val a = (it.first(meta) as? SymbolNode ?: InterpretException.notSymbol(meta)).name
		return ValueNode(null != symbolList.removeVariable(a), meta)
	}

	fun `for-each`(meta: MetaData, ls: List<Node>): Node {
		if (ls.size < 3) tooFewArgument(3, ls.size, meta)
		val i = (ls.first() as SymbolNode).name
		val a = ls[1].eval().let { (it as? Array<*>)?.toList() ?: it }
		return if (a is Iterable<*>) {
			var ret: Any? = null
			a.forEach {
				symbolList.defineVariable(i, ValueNode(it, meta))
				ret = ls[2].eval()
			}
			ValueNode(ret, meta)
		} else InterpretException.typeMisMatch("List", a, meta)
	}

	fun alias(meta: MetaData, ls: List<Node>): Node {
		val function = symbolList.getVariable(cast<SymbolNode>(ls.first(meta)).name) ?: return ValueNode(false, meta)
		ls.indices.forEach { index ->
			if (index != 0) {
				val name = cast<SymbolNode>(ls[index]).name
				if (function is Node) symbolList.defineVariable(name, function)
				else symbolList.defineFunction(name, cast(function, meta))
			}
		}
		return ValueNode(true, meta)
	}

	fun `variable?`(meta: MetaData, ls: List<Node>) =
			ValueNode(symbolList.variables[(ls.first(meta) as? SymbolNode)?.name ?: notSymbol(meta)] is Node, meta)

	fun `function?`(meta: MetaData, ls: List<Node>) =
			ValueNode(symbolList.variables[(ls.first(meta) as? SymbolNode)?.name ?: notSymbol(meta)] as? Func != null, meta)
}
