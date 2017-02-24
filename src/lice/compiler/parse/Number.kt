/**
 * Created by ice1000 on 2017/2/18.
 *
 * @author ice1000
 */
@file:JvmName("Parse")
@file:JvmMultifileClass

package lice.compiler.parse

//val Char.isDigit: Boolean
//	get() = this >= '0' && this <= '9'

fun String.isInt() =
		fold(true, { res, char ->
			res && char.isDigit()
		})

fun Char.isOctalInt() = this in '0'..'8'

fun Char.safeLower(): Char {
	if (this in 'A'..'Z') return this - ('A' - 'a');
	return this
}

fun String.isHexInt(): Boolean {
	if (this[0] == '-') return substring(1).isHexInt()
	return when {
		length <= 2 -> false
		this[0] != '0' || this[1].safeLower() != 'x' -> false
		else -> (2..length - 1)
				.map { this[it].toLowerCase() }
				.none { !it.isDigit() && (it < 'a' || it > 'f') }
	}
}

fun String.isBinInt(): Boolean {
	if (this[0] == '-') return substring(1).isBinInt()
	return when {
		length <= 2 -> false
		this[0] != '0' || this[1].safeLower() != 'b' -> false
		else -> (2..length - 1).none { this[it] != '0' && this[it] != '1' }
	}
}

fun String.isOctInt(): Boolean {
	if (this[0] == '-') return substring(1).isOctInt()
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

