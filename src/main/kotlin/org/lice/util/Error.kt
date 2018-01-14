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

open class LiceException(string: String, @JvmField protected val meta: MetaData) : RuntimeException(string) {
	protected fun prettify(cachedCodeLines: List<String>): String {
		val builder = StringBuilder()
		builder.appendln("Error " + message)
		if (meta.beginLine != -1)
			builder.append("At " + meta.beginLine)
		if (meta.beginIndex != -1) {
			builder.append(":" + meta.beginIndex)
		}
		builder.appendln(": " + message)
		if (meta.beginLine != -1) {
			builder.appendln(cachedCodeLines[meta.beginLine - 1])
		}
		if (meta.beginIndex != -1) {
			for (i in meta.beginIndex.downTo(2))
				builder.append(' ')
			builder.append('^')
			if (meta.endIndex != -1) {
				for (i in meta.endIndex.downTo(meta.beginIndex + 2))
					builder.append('~')
			}
			builder.appendln()
		}
		builder.appendln()
		return builder.toString()
	}
}

class ParseException(string: String, meta: MetaData = MetaData()) : LiceException(string, meta) {
	fun prettyPrint(cachedCodeLines: List<String>) {
		prettify(cachedCodeLines).let(System.err::println)
	}
}

class InterpretException(string: String, meta: MetaData = MetaData()) :
		LiceException(string, meta) {
	fun prettyPrint(cachedCodeLines: List<String>) {
		prettify(cachedCodeLines).let(System.err::println)
	}

	companion object Factory {
		fun undefinedVariable(
				name: String,
				meta: MetaData): Nothing = throw InterpretException("undefined variable: $name", meta)

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

