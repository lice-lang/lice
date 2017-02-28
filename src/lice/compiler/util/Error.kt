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
		fun undefinedFunction(name: String, lineNumber: Int): Nothing {
			throw ParseException("""undefined function: $name
at line: $lineNumber""")
		}
	}
}

class InterpretException(string: String) : RuntimeException(string) {
	companion object Factory {
		fun typeMisMatch(expected: String, actual: Value, lineNumber: Int): Nothing =
				throw InterpretException("""type mismatch: expected: $expected, found: ${actual.type.name}
at line: $lineNumber""")

		fun tooFewArgument(expected: Int, actual: Int, lineNumber: Int): Nothing {
			throw InterpretException("""expected $expected or more arguments, found: $actual
at line: $lineNumber""")
		}
	}
}

fun showError(string: String, exit: Boolean = false) {
	if (exit)
		throw RuntimeException(string)
	else
		serr(string)
}
