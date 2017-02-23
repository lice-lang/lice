/**
 * Created by ice1000 on 2017/2/23.
 *
 * @author ice1000
 */
@file:JvmName("Parse")
@file:JvmMultifileClass

package lice.compiler.parse

fun String.isString() =
		length >= 2 && this[0] == '\"' && this[length - 1] == '\"'

