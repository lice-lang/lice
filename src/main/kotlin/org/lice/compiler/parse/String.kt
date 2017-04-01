/**
 * Created by ice1000 on 2017/2/23.
 *
 * @author ice1000
 */
@file:JvmName("Parse")
@file:JvmMultifileClass

package org.lice.compiler.parse

import org.intellij.lang.annotations.Language
import java.io.StringReader
import java.util.regex.Pattern

@Language("RegExp")
val pattern = Pattern.compile("\"" + """([^"\x00-\x1F\x7F\\]|\\[\\'"bnrt]|\\u[a-fA-F0-9]{4})*""" + "\"")!!

fun String.isString(): Boolean = pattern.matcher(this).matches()

fun String.escape(): String {
	val sr = StringReader(this.substring(1, this.length - 1))
	val sb = StringBuilder()

	loop@ while (true) {
		var ci: Int
		var c: Char

		ci = sr.read()
		if (ci == -1) break

		c = ci.toChar()

		when (c) {
			'\\' -> {
				ci = sr.read()
				if (ci == -1) return "" //TODO
				c = ci.toChar()
				when (c) {
					'\\' -> {
						sb.append('\\')
						continue@loop
					}
					'\"' -> {
						sb.append('\"')
						continue@loop
					}
					'/' -> {
						sb.append('/')
						continue@loop
					}
					'b' -> {
						sb.append('\b')
						continue@loop
					}
					'n' -> {
						sb.append('\n')
						continue@loop
					}
					'r' -> {
						sb.append('\r')
						continue@loop
					}
					't' -> {
						sb.append('\t')
						continue@loop
					}
					'u' -> {
						val sbb = StringBuilder()
						sbb.append(sr.read().toChar())
						sbb.append(sr.read().toChar())
						sbb.append(sr.read().toChar())
						sbb.append(sr.read().toChar())
						val ii = Integer.parseInt(sbb.toString(), 16)

						sb.append(ii.toChar())
						continue@loop
					}
				}
				sb.append(c)
			}
			else -> sb.append(c)
		}
	}
	return sb.toString()
}

fun String.repeat(times: Int): String {
	val sb = StringBuilder()
	for (i in 1..times) sb.append(this)
	return sb.toString()
}
