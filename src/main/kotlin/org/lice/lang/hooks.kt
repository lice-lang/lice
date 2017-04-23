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

import org.lice.compiler.model.Node

typealias Output = Any?.() -> Unit

object Echoer {
	val stdout: Output = ::print
	var printer: Output = stdout
		get

	var stderr: Output = System.out::print
	var printerErr: Output = stderr
		get

	val nothing: Output = { }

	fun echo(a: Any? = "") = printer(a)
	fun echoln(a: Any? = "") = echo("$a\n")
	fun echoErr(a: Any? = "") = printerErr(a)
	fun echolnErr(a: Any? = "") = printerErr("$a\n")

	fun closeOutput() {
		printer = nothing
		printerErr = nothing
	}

	fun openOutput() {
		printer = stdout
		printerErr = stderr
	}
}

object BeforeEval {
	var hook: Node.() -> Unit = { }
		get

	fun apply(node: Node) = hook(node)
}
