package org.lice.core

import org.lice.lang.NumberOperator
import org.lice.model.MetaData
import org.lice.util.InterpretException
import org.lice.util.cast
import java.lang.reflect.Modifier

@Suppress("unused")
class FunctionWithMetaHolders(val symbolList: SymbolList) {
	fun `-`(meta: MetaData, list: List<Any?>) = when (list.size) {
		0 -> 0
		1 -> list.first()
		else -> list.drop(1).fold(NumberOperator(list.first() as Number)) { sum, value ->
			if (value is Number) sum.minus(value, meta)
			else InterpretException.typeMisMatch("Number", value, meta)
		}.result
	}

	fun `+`(meta: MetaData, list: List<Any?>) =
			list.fold(NumberOperator(0)) { sum, value ->
				if (value is Number) sum.plus(value, meta)
				else InterpretException.typeMisMatch("Number", value, meta)
			}.result

	fun extern(meta: MetaData, ls: List<Any?>): Any? {
		val name = ls[1].toString()
		val clazz = ls.first().toString()
		val method = Class.forName(clazz).declaredMethods
				.firstOrNull { Modifier.isStatic(it.modifiers) && it.name == name }
				?: throw UnsatisfiedLinkError("Method $name not found for class $clazz\nat line: ${meta.lineNumber}")
		symbolList.provideFunction(name) {
			method.invoke(null, *it.toTypedArray())
		}
		return name
	}

	fun `==`(meta: MetaData, ls: List<Any?>) = (1 until ls.size)
			.all { NumberOperator.compare(ls[it - 1] as Number, ls[it] as Number, meta) == 0 }

	fun `!=`(meta: MetaData, ls: List<Any?>) = (1 until ls.size)
			.none { NumberOperator.compare(ls[it - 1] as Number, ls[it] as Number, meta) == 0 }

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

	fun format(meta: MetaData, ls: List<Any?>): String {
		if (ls.isEmpty()) InterpretException.tooFewArgument(1, ls.size, meta)
		val format = ls.first().toString()
		return String.format(format, *ls.subList(1, ls.size).toTypedArray())
	}

}