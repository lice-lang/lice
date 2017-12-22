package org.lice.lang

object Echoer {
	var repl: Boolean = false

	private val stdout: Any?.() -> Unit = { print(this) }
	var printer: (Any?.() -> Unit)? = stdout

	fun echo(a: Any? = "") {
		printer?.invoke(a)
	}

	fun replEcholn(a: Any? = "") {
		if (repl) echo("$a\n")
	}

	fun closeOutput() {
		printer = null
	}

	fun openOutput() {
		printer = stdout
	}
}