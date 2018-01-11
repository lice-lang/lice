/**
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 * @since 1.0.0
 */
@file:JvmName("Model")
@file:JvmMultifileClass

package org.lice.model

import org.lice.core.Func
import org.lice.core.SymbolList
import org.lice.model.MetaData.Factory.EmptyMetaData
import org.lice.util.InterpretException.Factory.notFunction
import org.lice.util.InterpretException.Factory.undefinedVariable
import org.lice.util.className

class MetaData(
		val beginLine: Int = -1,
		val endLine: Int = -1,
		val beginIndex: Int = -1,
		val endIndex: Int = -1) {
	companion object Factory {
		val EmptyMetaData = MetaData()
	}
}

interface Node {
	fun eval(): Any?
	val meta: MetaData
	override fun toString(): String
}

class ValueNode(private val value: Any?, override val meta: MetaData = EmptyMetaData) : Node {
	override fun eval() = value
	override fun toString() = "value: <$value> => ${value.className()}"
}

class LazyValueNode(lambda: () -> Any?, override val meta: MetaData = EmptyMetaData) : Node {
	private val value by lazy(lambda)
	override fun eval() = value
	override fun toString() = "lazy: <$value>"
}

class ExpressionNode(val node: Node, override val meta: MetaData, val params: List<Node>) : Node {
	override fun eval() = (node.eval() as? Func ?: notFunction(meta)).invoke(meta, params).eval()
	override fun toString() = "function with ${params.size} params"
}

class SymbolNode(private val symbolList: SymbolList, val name: String, override val meta: MetaData) : Node {
	override fun eval() =
			if (symbolList.isVariableDefined(name)) symbolList.getVariable(name).let {
				if (it is Node) it.eval() else it
			} else undefinedVariable(name, meta)

	override fun toString() = "symbol: <$name>"
}
