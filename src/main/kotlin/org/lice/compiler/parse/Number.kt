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
	if (!isNegative && this[0] == '-') return substring(1).isInt(true)
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
	if (!isNegative && this[0] == '-') return substring(1).isHexInt(true)
	return when {
		length <= 2 -> false
		this[0] != '0' || this[1].safeLower() != 'x' -> false
		else -> (2..length - 1)
				.map { this[it].toLowerCase() }
				.none { !it.isDigit() && (it < 'a' || it > 'f') }
	}
}

fun String.isBigInt(isNegative: Boolean = false): Boolean {
	if (!isNegative && this[0] == '-') return substring(1).isBigInt(true)
	return when {
		length <= 1 -> false
		this[length - 1].safeLower() != 'n' -> false
		else -> {
			val a = substring(0..length - 2)
			a.isInt() || a.isHexInt() || a.isBinInt() || a.isOctInt()
		}
	}
}

fun String.isBinInt(isNegative: Boolean = false): Boolean {
	if (!isNegative && this[0] == '-') return substring(1).isBinInt(true)
	return when {
		length <= 2 -> false
		this[0] != '0' || this[1].safeLower() != 'b' -> false
		else -> (2..length - 1).none { this[it] != '0' && this[it] != '1' }
	}
}

fun String.isOctInt(isNegative: Boolean = false): Boolean {
	if (!isNegative && this[0] == '-') return substring(1).isOctInt(true)
	return when {
		length <= 1 -> false
		this[0] != '0' -> false
		else -> (1..length - 1).none { !this[it].isOctalInt() }
	}
}

fun String.toHexInt(): Int {
	if (this[0] == '-') return -substring(1).toHexInt()
	var ret = 0
	(2..length - 1).forEach {
		ret = ret shl 4
		val char = this[it].safeLower()
		if (char.isDigit()) ret += (char - '0')
		else /* if (char >= 'a' && char <= 'f') */ ret += (char - 'a' + 10)
//		ret *= 16
	}
	return ret
}

fun String.toBinInt(): Int {
	if (this[0] == '-') return -substring(1).toBinInt()
	var ret = 0
	(2..length - 1).forEach {
		ret = ret shl 1
		if (this[it] == '1') ++ret
//		ret *= 2
	}
	return ret
}

fun String.toBigInt() = BigInteger(this.substring(0, length - 1))

fun String.toBigDec() = BigDecimal(this.substring(0, length - 1))

fun String.toOctInt(): Int {
	if (this[0] == '-') return -substring(1).toBinInt()
	var ret = 0
	(1..length - 1).forEach {
		ret = ret shl 3
		ret += this[it] - '0'
//		ret *= 8
	}
	return ret
}

