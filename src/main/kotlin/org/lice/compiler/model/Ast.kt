/**
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 * @since 1.0.0
 */
@file:JvmName("Model")
@file:JvmMultifileClass

package org.lice.compiler.model

import org.lice.compiler.model.MetaData.Factory.EmptyMetaData
import org.lice.compiler.model.Value.Objects.Nullptr
import org.lice.compiler.util.ParseException.Factory.undefinedVariable
import org.lice.core.SymbolList
import org.lice.lang.NullptrType

data class MetaData(
		val lineNumber: Int) {
	companion object Factory {
		val EmptyMetaData = MetaData(-1)
	}
}

interface AbstractValue {
	val o: Any?
	val type: Class<*>
}

class Value(
		override val o: Any?,
		override val type: Class<*>) : AbstractValue {
	constructor(
			o: Any
	) : this(o, o.javaClass)

	companion object Objects {
		val Nullptr = Value(null, NullptrType::class.java)
	}
}

interface Node {
	//	fun eval(params: List<Node> = emptyList<Node>()): Value
	fun eval(): Value

	val meta: MetaData

	override fun toString(): String

	companion object Objects {
		fun getNullNode(meta: MetaData) = EmptyNode(meta)
	}
}

class ValueNode
@JvmOverloads
constructor(
		val value: Value,
		override val meta: MetaData = EmptyMetaData) : Node {

	@JvmOverloads
	constructor(any: Any, meta: MetaData = EmptyMetaData) : this(Value(any), meta)

	@JvmOverloads
	constructor(any: Any?, type: Class<*>, meta: MetaData = EmptyMetaData) : this(Value(any, type), meta)

	override fun eval() = value

	override fun toString() = "value: <${value.o}> => ${value.type}"
}

class LazyValueNode
//@JvmOverloads
constructor(
		lambda: () -> Value,
		override val meta: MetaData = EmptyMetaData) : Node {
	val value: Value by lazy(lambda)
	override fun eval() = value
	override fun toString() = "lazy value, not evaluated"
}

class ExpressionNode(
		val node: Node,
		override val meta: MetaData,
		val params: List<Node>) : Node {

	override fun eval(): Value = when (node) {
		is SymbolNode -> node.eval(params)
		is ExpressionNode -> node.eval(params)
		else -> node.eval()
	}

	fun eval(extern: List<Node>): Value = when (node) {
		is SymbolNode -> node.eval(params, extern)
		is ExpressionNode -> node.eval(params)
		else -> node.eval()
	}

	override fun toString() = "function: <$> with ${params.size} params"
}

class SymbolNode(
		val symbolList: SymbolList,
		val name: String,
		override val meta: MetaData) : Node {

	override fun eval() = eval(emptyList())

	fun eval(params: List<Node>, params2: List<Node> = emptyList()): Value {
		val node = (symbolList.getFunction(name)?.invoke(meta, params)
				?: undefinedVariable(name, meta))
		return when (node) {
			is SymbolNode -> node.eval(params2)
			is ExpressionNode -> node.eval(params2)
			else -> node.eval()
		}
	}

	override fun toString() = "symbol: <$name>"
}

class EmptyNode(override val meta: MetaData) : Node {
	override fun eval() = Nullptr
	//	override fun eval(params: List<Node>) = Nullptr
	override fun toString() = "null: <null>"
}

