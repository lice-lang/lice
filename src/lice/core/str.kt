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
	defineFunction("->str", { ln, ls -> ValueNode(ls[0].eval().o.toString(), ln) })
	defineFunction("str->int", { ln, ls ->
		val res = ls[0].eval()
		when (res.o) {
			is String -> ValueNode(when {
				res.o.isOctInt() -> res.o.toOctInt()
				res.o.isInt() -> res.o.toInt()
				res.o.isBinInt() -> res.o.toBinInt()
				res.o.isHexInt() -> res.o.toHexInt()
				else -> throw InterpretException("give string: \"${res.o}\" cannot be parsed as a number!")
			}, ln)
			else -> InterpretException.typeMisMatch("String", res)
		}
	})
	defineFunction("int->hex", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is Int -> ValueNode("0x${Integer.toHexString(a.o)}", ln)
			else -> InterpretException.typeMisMatch("Int", a)
		}
	})
	defineFunction("int->bin", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is Int -> ValueNode("0b${Integer.toBinaryString(a.o)}", ln)
			else -> InterpretException.typeMisMatch("Int", a)
		}
	})
	defineFunction("int->oct", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is Int -> ValueNode("0${Integer.toOctalString(a.o)}", ln)
			else -> InterpretException.typeMisMatch("Int", a)
		}
	})
	defineFunction("str-con", { ln, ls ->
		ValueNode(ls.fold(StringBuilder(ls.size)) { sb, value ->
			sb.append(value.eval().o.toString())
		}.toString(), ln)
	})
	defineFunction("format", { ln, ls ->
		if (ls.isEmpty()) InterpretException.tooFewArgument(1, ls.size)
		val format = ls[0].eval()
		when (format.o) {
			is String -> ValueNode(kotlin.String.format(format.o, *ls
					.subList(1, ls.size)
					.map { it.eval().o }
					.toTypedArray()
			), ln)
			else -> InterpretException.typeMisMatch("String", format)
		}
	})
	defineFunction("->chars", { ln, ls ->
		ValueNode(ls.fold(StringBuilder(ls.size)) { sb, value ->
			sb.append(value.eval().o.toString())
		}
				.toString()
				.toCharArray()
				.toList(), ln)
	})
	defineFunction("split", { ln, ls ->
		val str = ls[0].eval()
		val regex = ls[1].eval()
		ValueNode(str.o.toString().split(regex.o.toString()).toList(), ln)
	})
}
