/**
 * Created by ice1000 on 2017/2/23.
 *
 * @author ice1000
 */
@file:JvmName("Parse")
@file:JvmMultifileClass

package org.lice.compiler.parse

fun String.isString() =
		length >= 2 &&
				(this[0] == '\"' || this[0] == '“') &&
				(this[length - 1] == '\"' || this[length - 1] == '”')


fun String.repeat(times: Int): String {
	val sb = StringBuilder()
	for (i in 1..times) sb.append(this)
	return sb.toString()
}
