package lice.compiler.util

import lice.compiler.model.Value

/**
 * Created by ice1000 on 2017/2/17.
 *
 * @author ice1000
 */
class SymbolList {
	val map = mutableMapOf(
			Pair("+", { ls: List<Value> ->
				ls.fold(0) { sum, value ->
					if (value.o is Int) value.o + sum
					else throw ParseException("type mismatch : expected: Int, found: ${value.type.name}")
				}
			})
	)
}
