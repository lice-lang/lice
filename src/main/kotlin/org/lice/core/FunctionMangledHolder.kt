package org.lice.core

import org.lice.lang.NumberOperator
import org.lice.model.MetaData
import org.lice.parse.*
import org.lice.util.InterpretException
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
class FunctionMangledHolder(val symbolList: SymbolList) {
	fun `|$`(meta: MetaData, ls: List<Any?>) = ls.lastOrNull()
	fun `-$str`(meta: MetaData, it: List<Any?>) = it.first().toString()
	fun `-$double`(meta: MetaData, it: List<Any?>) = cast<Number>(it.first()).toDouble()
	fun `-$int`(meta: MetaData, it: List<Any?>) = cast<Number>(it.first()).toInt()
	fun `&`(meta: MetaData, ls: List<Any?>) =
			(1 until ls.size).all { NumberOperator.compare(ls[it - 1] as Number, ls[it] as Number, meta) < 0 }

	fun `$`(meta: MetaData, ls: List<Any?>) =
			(1 until ls.size).all { NumberOperator.compare(ls[it - 1] as Number, ls[it] as Number, meta) > 0 }

	fun `&=`(meta: MetaData, ls: List<Any?>) =
			(1 until ls.size).all { NumberOperator.compare(ls[it - 1] as Number, ls[it] as Number, meta) <= 0 }

	fun `$=`(meta: MetaData, ls: List<Any?>) =
			(1 until ls.size).all { NumberOperator.compare(ls[it - 1] as Number, ls[it] as Number, meta) >= 0 }

	fun `_`(meta: MetaData, ls: List<Any?>): Number {
		val init = cast<Number>(ls.first())
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

	fun `str-$int`(ln: MetaData, ls: List<Any?>): Int {
		val res = ls.first().toString()
		return when {
			res.isOctInt() -> res.toOctInt()
			res.isInt() -> res.toInt()
			res.isBinInt() -> res.toBinInt()
			res.isHexInt() -> res.toHexInt()
			else -> throw InterpretException("give string: \"$res\" cannot be parsed as a number!", ln)
		}
	}

	fun `int-$hex`(ln: MetaData, ls: List<Any?>): String {
		val a = ls.first()
		return if (a is Number) "0x${Integer.toHexString(a.toInt())}"
		else InterpretException.typeMisMatch("Int", a, ln)
	}

	fun `int-$bin`(ln: MetaData, ls: List<Any?>): String {
		val a = ls.first()
		return if (a is Number) "0b${Integer.toBinaryString(a.toInt())}"
		else InterpretException.typeMisMatch("Int", a, ln)
	}

	fun `int-$oct`(ln: MetaData, ls: List<Any?>): String {
		val a = ls.first()
		return if (a is Number) "0${Integer.toOctalString(a.toInt())}"
		else InterpretException.typeMisMatch("Int", a, ln)
	}

	fun `join-$str`(ln: MetaData, ls: List<Any?>) = cast<Iterable<*>>(ls.first()).joinToString(ls[1].toString())
	fun `{|}`(ln: MetaData, ls: List<Any?>) = ls.reduceRight(::Pair)
	fun `{|`(ln: MetaData, ls: List<Any?>): Any? {
		val a = ls.first()
		return when (a) {
			is Pair<*, *> -> a.first
			is Collection<*> -> a.first()
			else -> null
		}
	}

	fun `|}`(ln: MetaData, ls: List<Any?>): Any? {
		val a = ls.first()
		return when (a) {
			is Pair<*, *> -> a.second
			is Iterable<*> -> a.drop(1)
			else -> null
		}
	}

	fun `##`(metaData: MetaData, ls: List<Any?>): List<Int> {
		if (ls.size < 2) InterpretException.tooFewArgument(2, ls.size, metaData)
		val a = ls.first()
		val b = ls[1]
		return when {
			a is Number && b is Number -> {
				val begin = a.toInt()
				val end = b.toInt()
				if (begin <= end) (begin..end).toList()
				else (end..begin).reversed().toList()
			}
			else -> InterpretException.typeMisMatch("Number", a as? Number ?: b, metaData)
		}
	}

	fun `-$chars`(ln: MetaData, it: List<Any?>) = it.fold(StringBuilder(it.size)) { sb, value ->
		sb.append(value.toString())
	}.toString().toCharArray().toList()

}
