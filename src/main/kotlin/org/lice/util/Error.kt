/**
 * Created by ice1000 on 2017/2/17.
 *
 * @author ice1000
 * @since 1.0.0
 */
@file:JvmName("Utilities")
@file:JvmMultifileClass
@file:Suppress("NOTHING_TO_INLINE")

package org.lice.util

import org.lice.model.MetaData

private fun exceptionPrettyPrint(string: String, meta: MetaData, cachedCodeLines: List<String>) {
	System.err.println("Error " + string)
	if (meta.beginLine != -1)
		System.err.print("At " + meta.beginLine)
	if (meta.beginIndex != -1) {
		System.err.print(":" + meta.beginIndex)
	}
	System.err.println(": " + string)
	System.err.println(cachedCodeLines[meta.beginLine-1])
	if (meta.beginIndex != -1) {
		for (i: Int in meta.beginIndex.downTo(2))
			System.err.print(' ')
		System.err.print('^')
		if (meta.endIndex != -1) {
			for (i: Int in meta.endIndex.downTo(meta.beginIndex+2))
				System.err.print('~')
		}
		System.err.println()
	}
	System.err.println()
}

class ParseException(private val string: String, private val meta: MetaData = MetaData()) : RuntimeException(string) {
	fun prettyPrint(cachedCodeLines: List<String>) {
		exceptionPrettyPrint(string, meta, cachedCodeLines)
	}
}

class InterpretException(private val string: String, private val meta: MetaData = MetaData()) : RuntimeException(string) {
	fun prettyPrint(cachedCodeLines: List<String>) {
		exceptionPrettyPrint(string, meta, cachedCodeLines)
	}

	companion object Factory {
		fun undefinedVariable(name: String, meta: MetaData): Nothing
				= throw InterpretException("undefined variable: $name", meta)

		fun notSymbol(meta: MetaData): Nothing =
				throw InterpretException("type mismatch: symbol expected.", meta)

		fun notFunction(meta: MetaData): Nothing =
				throw InterpretException("type mismatch: function expected.", meta)

		fun typeMisMatch(expected: String, actual: Any?, meta: MetaData): Nothing =
				throw InterpretException("type mismatch: expected: $expected, found: ${actual?.className()}", meta)

		fun tooFewArgument(expected: Int, actual: Int, meta: MetaData): Nothing =
				throw InterpretException("expected $expected or more arguments, found: $actual", meta)

		fun numberOfArgumentNotMatch(expected: Int, actual: Int, meta: MetaData): Nothing =
				throw InterpretException("expected $expected arguments, found: $actual", meta)
	}
}

