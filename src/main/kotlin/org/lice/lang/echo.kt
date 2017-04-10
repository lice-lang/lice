/**
 * Created by ice1000 on 2017/4/10.
 *
 * @author ice1000
 * @since 2.6.0
 */
@file:Suppress("NOTHING_TO_INLINE")
@file:JvmName("Standard")
@file:JvmMultifileClass

package org.lice.lang

object Echoer {
	var printer: (Any?) -> Unit = ::print
	var printerErr: (Any?) -> Unit = System.out::print
	fun echo(a: Any? = "") = printer(a)
	fun echoln(a: Any? = "") = echo("$a\n")
	fun echoErr(a: Any? = "") = printerErr(a)
	fun echolnErr(a: Any? = "") = printerErr("$a\n")
}

