/**
 * Created by ice1000 on 2017/2/18.
 *
 * @author ice1000
 * @since 1.0.0
 */
@file:JvmName("Parse")
@file:JvmMultifileClass

package org.lice.compiler.parse

import java.math.BigDecimal
import java.math.BigInteger

//val Char.isDigit: Boolean
//	get() = this >= '0' && this <= '9'

fun String.isInt(isNegative: Boolean = false): Boolean {
	if (!isNegative && '-' == this[0]) return substring(1).isInt(true)
	return isNotEmpty() && fold(true, { res, char ->
		res && char.isDigit()
	})
}

fun Char.isOctalInt() =
		this in '0'..'8'

fun Char.safeLower() =
		when (this) {
			in 'A'..'Z' -> this - ('A' - 'a')
			else -> this
		}

fun String.isHexInt(isNegative: Boolean = false): Boolean {
	if (!isNegative && '-' == this[0]) return substring(1).isHexInt(true)
	return when {
		length <= 2 -> false
		'0' != this[0] || 'x' != this[1].safeLower() -> false
		else -> (2..length - 1)
				.map { this[it].toLowerCase() }
				.all { it.isDigit() || it in 'a'..'f' }
	}
}

fun String.isBigInt(isNegative: Boolean = false): Boolean {
	if (!isNegative && '-' == this[0]) return substring(1).isBigInt(true)
	return when {
		length <= 1 -> false
		'n' != this[length - 1].safeLower() -> false
		else -> {
			val a = substring(0..length - 2)
			a.isInt() || a.isHexInt() || a.isBinInt() || a.isOctInt()
		}
	}
}

fun String.isBigDec(isNegative: Boolean = false): Boolean {
	if (!isNegative && '-' == this[0]) return substring(1).isBigDec(true)
	return when {
		length <= 2 -> false
		'm' != this[length - 1].safeLower() -> false
		else -> {
			val a = substring(0..length - 2)
			1 >= a.count { '.' == it } && a.all { '.' == it || it.isDigit() }
		}
	}
}

fun String.isBinInt(isNegative: Boolean = false): Boolean {
	if (!isNegative && this[0] == '-') return substring(1).isBinInt(true)
	return when {
		length <= 2 -> false
		'0' != this[0] || 'b' != this[1].safeLower() -> false
		else -> (2..length - 1).none { '0' != this[it] && '1' != this[it] }
	}
}

fun String.isOctInt(isNegative: Boolean = false): Boolean {
	if (!isNegative && '-' == this[0]) return substring(1).isOctInt(true)
	return when {
		length <= 1 -> false
		'0' != this[0] -> false
		else -> (1..length - 1).all { this[it].isOctalInt() }
	}
}

fun String.toHexInt(): Int {
	if (this[0] == '-') return -substring(1).toHexInt()
	var ret = 0
	(2..length - 1).forEach {
		ret = ret shl 4
		val char = this[it].safeLower()
		if (char.isDigit()) ret += (char - '0')
		else ret += (char - 'a' + 10)
	}
	return ret
}

fun String.toBinInt(): Int {
	if ('-' == this[0]) return -substring(1).toBinInt()
	var ret = 0
	(2..length - 1).forEach {
		ret = ret shl 1
		if ('1' == this[it]) ++ret
	}
	return ret
}

fun String.toBigInt() = BigInteger(this.substring(0, length - 1).run {
	when {
		isHexInt() -> toHexInt().toString()
		isBinInt() -> toBinInt().toString()
		isOctInt() -> toOctInt().toString()
		else -> this
	}
})

fun String.toBigDec() = BigDecimal(this.substring(0, length - 1))

fun String.toOctInt(): Int {
	if (this[0] == '-') return -substring(1).toBinInt()
	var ret = 0
	(1..length - 1).forEach {
		ret = ret shl 3
		ret += this[it] - '0'
	}
	return ret
}

