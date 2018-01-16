@file:Suppress("FunctionName")

package org.lice.core

import org.lice.model.*
import org.lice.util.*
import org.lice.util.InterpretException.Factory.tooFewArgument

@Suppress("unused")
/**
 * `$` in the function names will be replaced with `>`.
 * @author ice1000
 */
class FunctionDefinedMangledHolder(private val symbolList: SymbolList) {
	fun `def?`(meta: MetaData, it: List<Node>): Node {
		val a = (it.first(meta) as? SymbolNode ?: InterpretException.notSymbol(meta)).name
		return ValueNode(symbolList.isVariableDefined(a), meta)
	}

	fun undef(meta: MetaData, it: List<Node>): Node {
		val a = (it.first(meta) as? SymbolNode ?: InterpretException.notSymbol(meta)).name
		return ValueNode(null != symbolList.removeVariable(a), meta)
	}

	fun `for-each`(meta: MetaData, ls: List<Node>): Node {
		if (ls.size < 3) tooFewArgument(3, ls.size, meta)
		val i = (ls.first() as SymbolNode).name
		val a = ls[1].eval()
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
}