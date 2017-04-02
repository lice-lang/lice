package org.lice.parser

/**
 * Created by glavo on 17-4-2.
 *
 * @author Glavo
 * @since 3.0
 */

fun Char?.isBlank(): Boolean =
	this == ' ' || this == '\n' || this == '\b' || this == 'r'
		|| this == '\t' || this == null


fun Char?.isBracket(): Boolean =
	this == '(' || this == ')'
		|| this == '[' || this == ']'
		|| this == '{' || this == '}'