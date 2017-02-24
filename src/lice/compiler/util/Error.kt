/**
 * Created by ice1000 on 2017/2/17.
 *
 * @author ice1000
 */
@file:JvmName("Utilities")
@file:JvmMultifileClass

package lice.compiler.util

import lice.compiler.model.Value

class ParseException(string: String) : RuntimeException(string) {
	companion object Factory {
		fun undefinedFunction(name: String): Nothing {
			throw ParseException("Undefined Function: $name")
		}
	}
}

class InterpretException(string: String) : RuntimeException(string) {
	companion object Factory {
		fun typeMisMatch(expected: String, value: Value): Nothing {
			throw InterpretException("type mismatch: expected: $expected, found: ${value.type.name}")
		}

		fun tooFewArgument(expected: Int, actual: Int): Nothing {
			throw InterpretException("Expected $expected or more arguments, found: $actual")
		}
	}
}

fun showError(string: String, exit: Boolean = false) {
	if (exit)
		throw RuntimeException(string)
	else
		serr(string)
}
