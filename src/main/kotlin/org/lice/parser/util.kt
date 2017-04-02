package org.lice.parser

/**
 * Created by glavo on 17-4-2.
 *
 * @author Glavo
 * @since v3.0
 */

fun Char?.isBlank(): Boolean =
	this == ' ' || this == '\n' || this == '\b' || this == 'r'
		|| this == '\t' || this == null