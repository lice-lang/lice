@file:Suppress("FunctionName")

package org.lice.core

import org.lice.lang.NumberOperator
import org.lice.model.MetaData
import org.lice.parse.*
import org.lice.util.InterpretException
import org.lice.util.InterpretException.Factory.tooFewArgument
import org.lice.util.InterpretException.Factory.typeMisMatch
import org.lice.util.cast

@Suppress("unused")
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
						else typeMisMatch("Number", value, meta)
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
		else typeMisMatch("Int", a, meta)
	}

	fun `int-$bin`(meta: MetaData, ls: List<Any?>): String {
		val a = ls.first()
		return if (a is Number) "0b${Integer.toBinaryString(a.toInt())}"
		else typeMisMatch("Int", a, meta)
	}

	fun `int-$oct`(meta: MetaData, ls: List<Any?>): String {
		val a = ls.first()
		return if (a is Number) "0o${Integer.toOctalString(a.toInt())}"
		else typeMisMatch("Int", a, meta)
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
			else -> typeMisMatch("Number", a as? Number ?: b, metaData)
		}
	}

	fun `list-$array`(meta: MetaData, it: List<Any?>) = cast<List<*>>(it.first(meta)).toTypedArray()
	fun `array-$list`(meta: MetaData, it: List<Any?>) = cast<Array<*>>(it.first(meta)).toList()
	fun `-$chars`(meta: MetaData, it: List<Any?>) = it.joinToString("").toCharArray()
}
