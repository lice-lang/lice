package org.lice.parser

/**
 * Created by glavo on 17-4-2.
 *
 * @author Glavo
 * @author ice1000
 * @since 3.0
 */

fun Char?.isBlank() = ' ' == this
		|| '\n' == this
		|| '\b' == this
		|| '\r' == this
		|| '\t' == this
		|| '　' == this
		|| null == this

fun Char?.isBracket() = '(' == this
		|| ')' == this
		|| '[' == this
		|| ']' == this
		|| '{' == this
		|| '}' == this
		|| '（' == this
		|| '）' == this
		|| '【' == this
		|| '】' == this
