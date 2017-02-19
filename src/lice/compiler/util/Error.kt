/**
 * Created by ice1000 on 2017/2/17.
 *
 * @author ice1000
 */
package lice.compiler.util

class ParseException(string: String) : RuntimeException(string)

fun showError(string: String): Nothing = throw ParseException(string)
