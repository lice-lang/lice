/**
 * Created by ice1000 on 2017/2/18.
 *
 * @author ice1000
 */
package lice.compiler.parse

//val Char.isDigit: Boolean
//	get() = this >= '0' && this <= '9'

fun String.isInt() =
		fold(true, { res, char ->
			res && char.isDigit()
		})

fun Char.safeLower(): Char {
	if (this >= 'A' && this <= 'Z') return this - ('A' - 'a');
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

fun String.toHexInt(): Int {
	var ret = 0
	(2..length - 1)
			.reversed()
			.forEach {

			}
}

