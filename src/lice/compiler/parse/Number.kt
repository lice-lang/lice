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

//fun isHex(string: String) =

