/**
 * Created by ice1000 on 2017/2/12.
 *
 * @author ice1000
 */
package lice.compiler.ast

import lice.compiler.model.Ast
import lice.compiler.model.EmptyNode
import lice.compiler.model.EmptyStringNode
import lice.compiler.model.StringNode
import java.io.File


fun createAst(file: File): Ast {
	val variableMap = mutableMapOf<String, Int>()
	val functionMap = mutableMapOf(
			Pair("+", { ls: List<Int> -> ls.fold(0, { a, b -> a + b }) })
	)
	val code = file.readText()
	var index = 0
	fun buildNode(code: String): StringNode {
		val elements = mutableListOf<String>()
		code.forEach {
			when (it) {
				'(' -> {
				}
				')' -> {
				}
				' ' -> {
				}
				else -> {
				}
			}
			++index
		}
		return EmptyStringNode
	}
	buildNode(code)
	return Ast(EmptyNode)
}
