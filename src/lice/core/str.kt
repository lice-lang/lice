/**
 * Created by ice1000 on 2017/2/26.
 *
 * @author ice1000
 */
@file:Suppress("NOTHING_TO_INLINE")
@file:JvmName("Standard")
@file:JvmMultifileClass

package lice.core

import lice.compiler.model.ValueNode
import lice.compiler.parse.*
import lice.compiler.util.InterpretException
import lice.compiler.util.SymbolList


inline fun SymbolList.addStringFunctions() {
	setFunction("->str", { ls -> ValueNode(ls[0].eval().o.toString()) })
	setFunction("str->int", { ls ->
		val res = ls[0].eval()
		when (res.o) {
			is String -> ValueNode(when {
				res.o.isOctInt() -> res.o.toOctInt()
				res.o.isInt() -> res.o.toInt()
				res.o.isBinInt() -> res.o.toBinInt()
				res.o.isHexInt() -> res.o.toHexInt()
				else -> throw InterpretException("give string: \"${res.o}\" cannot be parsed as a number!")
			})
			else -> InterpretException.typeMisMatch("String", res)
		}
	})
	setFunction("int->hex", { ls ->
		val a = ls[0].eval()
		when (a.o) {
			is Int -> ValueNode("0x${Integer.toHexString(a.o)}")
			else -> InterpretException.typeMisMatch("Int", a)
		}
	})
	setFunction("int->bin", { ls ->
		val a = ls[0].eval()
		when (a.o) {
			is Int -> ValueNode("0b${Integer.toBinaryString(a.o)}")
			else -> InterpretException.typeMisMatch("Int", a)
		}
	})
	setFunction("int->oct", { ls ->
		val a = ls[0].eval()
		when (a.o) {
			is Int -> ValueNode("0${Integer.toOctalString(a.o)}")
			else -> InterpretException.typeMisMatch("Int", a)
		}
	})
	setFunction("str-con", { ls ->
		ValueNode(ls.fold(StringBuilder(ls.size)) { sb, value ->
			sb.append(value.eval().o.toString())
		}.toString())
	})
	setFunction("format", { ls ->
		if (ls.isEmpty()) InterpretException.tooFewArgument(1, ls.size)
		val format = ls[0].eval()
		when (format.o) {
			is String -> ValueNode(kotlin.String.format(format.o, *ls
					.subList(1, ls.size)
					.map { it.eval().o }
					.toTypedArray()
			))
			else -> InterpretException.typeMisMatch("String", format)
		}
	})
	setFunction("->chars", { ls ->
		ValueNode(ls.fold(StringBuilder(ls.size)) { sb, value ->
			sb.append(value.eval().o.toString())
		}
				.toString()
				.toCharArray()
				.toList())
	})
	setFunction("split", { ls ->
		val str = ls[0].eval()
		val regex = ls[1].eval()
		ValueNode(str.o.toString().split(regex.o.toString()).toList())
	})
}
