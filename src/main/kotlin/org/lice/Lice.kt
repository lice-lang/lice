/**
 * Created by ice1000 on 2017/2/26.
 *
 * @author ice1000
 * @since 1.0.0
 */
package org.lice

import org.lice.compiler.parse.buildNode
import org.lice.compiler.parse.mapAst
import org.lice.core.bindings
import org.lice.repl.Main
import java.io.File
import javax.script.Bindings

val VERSION: String = "v3.1.1"

object Lice {
	@JvmOverloads
	@JvmStatic
	fun run(
			file: File,
			symbolList: Bindings = bindings()
	) = Main.interpret(file, symbolList).o

	@JvmStatic
	fun runBarely(file: File) = Main.interpret(file, bindings(false)).o

	@JvmStatic
	fun runBarely(code: String) = mapAst(
			node = buildNode(code),
			symbolList = bindings(false)
	).eval().o

	@JvmOverloads
	@JvmStatic
	fun run(
			code: String,
			symbolList: Bindings = bindings()
	) = mapAst(
			node = buildNode(code),
			symbolList = symbolList
	).eval().o
}
