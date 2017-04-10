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
import org.lice.compiler.util.ParseException.Factory.undefinedFunction
import org.lice.compiler.util.ParseException.Factory.undefinedVariable
import org.lice.compiler.util.SymbolList

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

data class Value(
		override val o: Any?,
		override val type: Class<*>) : AbstractValue {
	constructor(
			o: Any
	) : this(o, o.javaClass)

	companion object Objects {
		val Nullptr =
				Value(null, Any::class.java)
	}
}

interface Node {
	fun eval(): Value
	val meta: MetaData

	override fun toString(): String

	companion object Objects {
		fun getNullNode(meta: MetaData) = EmptyNode(meta)
	}
}

data class ValueNode
@JvmOverloads
constructor(
		val value: Value,
		override val meta: MetaData = EmptyMetaData) : Node {

	@JvmOverloads
	constructor(
			any: Any,
			meta: MetaData = EmptyMetaData
	) : this(
			Value(any),
			meta
	)

	@JvmOverloads
	constructor(
			any: Any?,
			type: Class<*>,
			meta: MetaData = EmptyMetaData
	) : this(
			Value(any, type),
			meta
	)

	override fun eval() = value

	override fun toString() = "value: <${value.o}> => ${value.type}"
}

data class FExprValueNode
@JvmOverloads
constructor(
		val fexpr: () -> Any?,
		override val meta: MetaData = EmptyMetaData) : Node {
	override fun eval(): Value {
		val ret = fexpr()
		return if (ret != null) Value(ret) else Nullptr
	}

	override fun toString() = "fexpr: <not evaluated, unknown>"
}


data class ExpressionNode(
		val symbolList: SymbolList,
		val function: String,
		override val meta: MetaData,
		val params: List<Node>) : Node {

	constructor(
			symbolList: SymbolList,
			function: String,
			meta: MetaData,
			vararg params: Node
	) : this(
			symbolList,
			function,
			meta,
			params.toList()
	)

	override fun eval() =
			(symbolList.getFunction(function)
					?: undefinedFunction(function, meta))
					.invoke(meta, params).eval()

	override fun toString() = "function: <$function> with ${params.size} params"
}

data class LambdaNode(
		val lambda: Node,
		val symbolList: SymbolList,
		val params: List<Node>,
		override val meta: MetaData) : Node {

	@Deprecated("difficult to achieve!")
	override fun eval(): Value {
		val str = lambda.eval().o
		return (symbolList.getFunction(str.toString())
				?: undefinedFunction(str.toString(), meta))
				.invoke(meta, params).eval()
	}

	override fun toString() = "lambda: <$${super.toString()}>"
}

data class SymbolNode(
		val symbolList: SymbolList,
		val name: String,
		override val meta: MetaData) : Node {

	override fun eval() =
			(symbolList.getFunction(name)?.invoke(meta, emptyList())
					?: undefinedVariable(name, meta))
					.eval()

	override fun toString() = "symbol: <$name>"
}

data class EmptyNode(override val meta: MetaData) : Node {
	override fun eval() = Nullptr
	override fun toString() = "null: <null>"
}

@Deprecated(
		message =  "",
		level = DeprecationLevel.WARNING,
		replaceWith = ReplaceWith("Node")
)
class Ast(
		val root: Node
)
