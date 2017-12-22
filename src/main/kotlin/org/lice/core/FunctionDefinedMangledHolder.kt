package org.lice.core

import org.lice.model.*
import org.lice.util.*

@Suppress("unused")
/**
 * `$` in the function names will be replaced with `>`.
 * @author ice1000
 */
class FunctionDefinedMangledHolder(val symbolList: SymbolList) {
	fun `def?`(metaData: MetaData, ls: List<Node>): Node {
		val a = (ls.first() as? SymbolNode ?: InterpretException.notSymbol(metaData)).name
		return ValueNode(symbolList.isVariableDefined(a), metaData)
	}

	fun undef(metaData: MetaData, ls: List<Node>): Node {
		val a = (ls.first() as? SymbolNode ?: InterpretException.notSymbol(metaData)).name
		return ValueNode(null != symbolList.removeVariable(a), metaData)
	}

	fun alias(meta: MetaData, ls: List<Node>): Node {
		val a = symbolList.getVariable(cast<SymbolNode>(ls.first()).name)
		a?.let { function ->
			ls.forEachIndexed { index, _ ->
				val name = cast<SymbolNode>(ls[index]).name
				if (index != 0) {
					if (function is Node) symbolList.defineVariable(name, function)
					else symbolList.defineFunction(name, cast(function))
				}
			}
		}
		return ValueNode(null != a, meta)
	}

	fun `force|$`(meta: MetaData, ls: List<Node>): Node {
		var ret: Any? = null
		forceRun { ls.forEach { node -> ret = node.eval() } }
		return ValueNode(ret, meta)
	}

	fun `str-$sym`(ln: MetaData, ls: List<Node>) = SymbolNode(symbolList, ls.first().eval().toString(), ln)
	fun `sym-$str`(ln: MetaData, ls: List<Node>): Node {
		val a = ls.first()
		return if (a is SymbolNode) ValueNode(a.name, ln)
		else InterpretException.typeMisMatch("Symbol", a, ln)
	}

	fun `-$`(metaData: MetaData, ls: List<Node>): Node {
		if (ls.size < 2) InterpretException.tooFewArgument(2, ls.size, metaData)
		symbolList.defineVariable(cast<SymbolNode>(ls.first()).name, ValueNode(ls[1].eval()))
		return ls.first()
	}

	fun `if`(metaData: MetaData, ls: List<Node>): Node {
		if (ls.size < 2)
			InterpretException.tooFewArgument(2, ls.size, metaData)
		val a = ls.first().eval()
		val condition = a.booleanValue()
		return when {
			condition -> ls[1]
			ls.size >= 3 -> ls[2]
			else -> EmptyNode(metaData)
		}
	}

	fun `when`(metaData: MetaData, ls: List<Node>): Node {
		for (i in (0..ls.size - 2) step 2) {
			val a = ls[i].eval()
			val condition = a.booleanValue()
			if (condition) return ls[i + 1]
		}
		return if (ls.size % 2 == 0) EmptyNode(metaData) else ls.last()
	}

	fun `while`(metaData: MetaData, ls: List<Node>): Node {
		if (ls.size < 2)
			InterpretException.tooFewArgument(2, ls.size, metaData)
		var a = ls.first().eval()
		var ret: Node = EmptyNode(metaData)
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