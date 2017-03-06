/**
 * Created by ice1000 on 2017/2/17.
 *
 * @author ice1000
 */
@file:JvmName("Utilities")
@file:JvmMultifileClass
@file:Suppress("NOTHING_TO_INLINE")

package lice.compiler.util

import lice.compiler.model.MetaData
import lice.compiler.model.Value

class ParseException(string: String) : RuntimeException(string) {
	companion object Factory {
		inline fun undefinedFunction(name: String, meta: MetaData): Nothing {
			throw ParseException("""undefined function: $name
at line: ${meta.lineNumber}""")
		}

		inline fun undefinedVariable(name: String, meta: MetaData): Nothing {
			throw ParseException("""undefined variable: $name
at line: ${meta.lineNumber}""")
		}
	}
}

class InterpretException(string: String) : RuntimeException(string) {
	companion object Factory {
		inline fun typeMisMatch(expected: String, actual: Value, meta: MetaData): Nothing =
				throw InterpretException("""type mismatch: expected: $expected, found: ${actual.type.name}
at line: ${meta.lineNumber}""")

		inline fun typeMisMatch(expected: String, actual: Any, meta: MetaData): Nothing =
				throw InterpretException("""type mismatch: expected: $expected, found: ${actual.javaClass.name}
at line: ${meta.lineNumber}""")

		inline fun tooFewArgument(expected: Int, actual: Int, meta: MetaData): Nothing {
			throw InterpretException("""expected $expected or more arguments, found: $actual
at line: ${meta.lineNumber}""")
		}

		inline fun numberOfArgumentNotMatch(expected: Int, actual: Int, meta: MetaData): Nothing {
			throw InterpretException("""expected $expected arguments, found: $actual
at line: ${meta.lineNumber}""")
		}
	}
}

fun showError(string: String, exit: Boolean = false) {
	if (exit)
		throw RuntimeException(string)
	else
		serr(string)
}
