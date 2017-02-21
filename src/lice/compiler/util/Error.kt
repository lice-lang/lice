/**
 * Created by ice1000 on 2017/2/17.
 *
 * @author ice1000
 */
@file:JvmName("Utilities")
@file:JvmMultifileClass
package lice.compiler.util

class ParseException(string: String) : RuntimeException(string)

class InterpretException(string: String) : RuntimeException(string)

fun showError(string: String, exit: Boolean = false) {
	if (exit)
		throw RuntimeException(string)
	else
		serr(string)
}
