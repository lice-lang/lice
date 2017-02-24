/**
 * Created by ice1000 on 2017/2/17.
 *
 * @author ice1000
 */
package lice.compiler.util

import lice.compiler.model.Node
import lice.compiler.model.Node.Objects.EmptyNode
import lice.compiler.model.Value.Objects.Nullptr
import lice.compiler.model.ValueNode
import lice.compiler.parse.*
import lice.compiler.util.InterpretException.Factory.tooFewArgument
import lice.compiler.util.InterpretException.Factory.typeMisMatch
import java.io.File
import java.net.URL
import java.util.*

@Suppress("NOTHING_TO_INLINE")

class SymbolList(init: Boolean = true) {
	val functionMap: MutableMap<String, Int> = mutableMapOf()
	val functionList: MutableList<(List<Node>) -> Node> = mutableListOf()

	val variableMap: MutableMap<String, Node> = mutableMapOf()
	val rand = Random(System.currentTimeMillis())

	init {
		if (init) initialize()
	}

	inline fun addNumberFunctions() {
		addFunction("int->double", { ls ->
			ValueNode((ls[0].eval().o as Int).toDouble())
		})
		addFunction("+", { list ->
			ValueNode(list.fold(0) { sum, value ->
				val res = value.eval()
				when (res.o) {
					is Int -> res.o + sum
					else -> typeMisMatch("Int", res)
				}
			})
		})
		addFunction("-", { ls ->
			when (ls.size) {
				0 -> ValueNode(0)
				1 -> ValueNode(ls[0].eval())
				else -> {
					var res = ls[0].eval().o as Int
					for (i in 1..ls.size - 1)
						res -= ls[i].eval().o as Int
					ValueNode(res)
				}
			}
		})
		addFunction("/", { ls ->
			when (ls.size) {
				0 -> ValueNode(1)
				1 -> ValueNode(ls[0].eval())
				else -> {
					var res = ls[0].eval().o as Int
					for (i in 1..ls.size - 1)
						res /= ls[i].eval().o as Int
					ValueNode(res)
				}
			}
		})
		addFunction("%", { ls ->
			when (ls.size) {
				0 -> ValueNode(0)
				1 -> ValueNode(ls[0].eval())
				else -> {
					var res = ls[0].eval().o as Int
					@Suppress("DEPRECATION")
					for (i in 1..ls.size - 1)
						res = res.mod(ls[i].eval().o as Int)
					ValueNode(res)
				}
			}
		})
		addFunction("*", { ls ->
			ValueNode(ls.fold(1) { sum, value ->
				val res = value.eval()
				when (res.o) {
					is Int -> res.o * sum
					else -> typeMisMatch("Int", res)
				}
			})
		})
		addFunction("==", { list ->
			val ls = list.map(Node::eval)
			ValueNode((1..ls.size - 1).none {
				ls[it].o != ls[it - 1].o
			})
		})
		addFunction("!=", { list ->
			val ls = list.map(Node::eval)
			ValueNode((1..ls.size - 1).none {
				ls[it].o == ls[it - 1].o
			})
		})
		addFunction("<", { list ->
			val ls = list.map(Node::eval)
			ValueNode((1..ls.size - 1).none {
				ls[it].o as Int <= ls[it - 1].o as Int
			})
		})
		addFunction(">", { list ->
			val ls = list.map(Node::eval)
			ValueNode((1..ls.size - 1).none {
				(ls[it].o as Int) >= ls[it - 1].o as Int
			})
		})
		addFunction(">=", { list ->
			val ls = list.map(Node::eval)
			ValueNode((1..ls.size - 1).none {
				ls[it].o as Int > ls[it - 1].o as Int
			})
		})
		addFunction("<=", { ls ->
			val list = ls.map(Node::eval)
			ValueNode((1..list.size - 1).none {
				(list[it].o as Int) < list[it - 1].o as Int
			})
		})
		addFunction("sqrt", { ls ->
			ValueNode(Math.sqrt((ls[0].eval().o as Int).toDouble()))
		})
		addFunction("rand", { ValueNode(rand.nextInt()) })
	}

	inline fun addBoolFunctions() {
		addFunction("&&", { ls ->
			ValueNode(ls.fold(true) { sum, value ->
				val o = value.eval()
				if (o.o is Boolean) o.o && sum
				else typeMisMatch("Boolean", o)
			})
		})
		addFunction("||", { ls ->
			ValueNode(ls.fold(false) { sum, value ->
				val o = value.eval()
				if (o.o is Boolean) o.o || sum
				else typeMisMatch("Boolean", o)
			})
		})
		addFunction("!", { ls -> ValueNode(!(ls[0].eval().o as Boolean)) })
	}

	inline fun addFileFunctions() {
		addFunction("file", { ls ->
			val a = ls[0].eval()
			when (a.o) {
				is String -> ValueNode(File(a.o)
						.apply { if (!exists()) createNewFile() })
				else -> typeMisMatch("String", a)
			}
		})
		addFunction("file-exists?", { ls ->
			val a = ls[0].eval()
			when (a.o) {
				is String -> ValueNode(File(a.o).exists())
				else -> typeMisMatch("String", a)
			}
		})
		addFunction("read-file", { ls ->
			val a = ls[0].eval()
			when (a.o) {
				is File -> ValueNode(a.o.readText())
				else -> typeMisMatch("File", a)
			}
		})
		addFunction("url", { ls ->
			val a = ls[0].eval()
			when (a.o) {
				is String -> ValueNode(URL(a.o))
				else -> typeMisMatch("String", a)
			}
		})
		addFunction("read-url", { ls ->
			val a = ls[0].eval()
			when (a.o) {
				is URL -> ValueNode(a.o.readText())
				else -> typeMisMatch("URL", a)
			}
		})
		addFunction("load-file", { ls ->
			val o = ls[0].eval()
			when (o.o) {
				is File -> ValueNode(createAst(o.o).root.eval())
				else -> typeMisMatch("File", o)
			}
		})
		addFunction("write-file", { ls ->
			val a = ls[0].eval()
			val b = ls[1].eval()
			when (a.o) {
				is File -> a.o.writeText(b.o.toString())
				else -> typeMisMatch("File", a)
			}
			ValueNode(b.o.toString())
		})
	}

	inline fun addGetSetFunction() {
		addFunction("->", { ls ->
			if (ls.size < 2)
				tooFewArgument(2, ls.size)
			val str = ls[0].eval()
			val res = ValueNode(ls[1].eval())
			when (str.o) {
				is String -> setVariable(str.o, res)
				else -> typeMisMatch("String", str)
			}
			res
		})
		addFunction("<-", { ls ->
			if (ls.isEmpty())
				tooFewArgument(1, ls.size)
			val str = ls[0].eval()
			when (str.o) {
				is String -> getVariable(str.o) ?: EmptyNode
				else -> typeMisMatch("String", str)
			}
		})
		addFunction("<->", { ls ->
			if (ls.size < 2)
				tooFewArgument(2, ls.size)
			val str = ls[0].eval()
			when (str.o) {
				is String -> {
					if (getVariable(str.o) == null) setVariable(str.o, ValueNode(ls[1].eval()))
					getVariable(str.o)!!
				}
				else -> typeMisMatch("String", str)
			}
		})
	}

	inline fun addStringFunctions() {
		addFunction("to-str", { ls -> ValueNode(ls[0].eval().o.toString()) })
		addFunction("str->int", { ls ->
			val res = ls[0].eval()
			when (res.o) {
				is String -> ValueNode(when {
					res.o.isOctInt() -> res.o.toOctInt()
					res.o.isInt() -> res.o.toInt()
					res.o.isBinInt() -> res.o.toBinInt()
					res.o.isHexInt() -> res.o.toHexInt()
					else -> throw InterpretException("give string: \"${res.o}\" cannot be parsed as a number!")
				})
				else -> typeMisMatch("String", res)
			}
		})
		addFunction("int->hex", { ls ->
			val a = ls[0].eval()
			when (a.o) {
				is Int -> ValueNode("0x${Integer.toHexString(a.o)}")
				else -> typeMisMatch("Int", a)
			}
		})
		addFunction("int->bin", { ls ->
			val a = ls[0].eval()
			when (a.o) {
				is Int -> ValueNode("0b${Integer.toBinaryString(a.o)}")
				else -> typeMisMatch("Int", a)
			}
		})
		addFunction("int->oct", { ls ->
			val a = ls[0].eval()
			when (a.o) {
				is Int -> ValueNode("0${Integer.toOctalString(a.o)}")
				else -> typeMisMatch("Int", a)
			}
		})
		addFunction("str-con", { ls ->
			ValueNode(ls.fold(StringBuilder(ls.size)) { sb, value ->
				val res = value.eval()
				when (res.o) {
					is String -> sb.append(res.o)
					else -> typeMisMatch("String", res)
				}
			}.toString())
		})
		addFunction("print", { ls ->
			ls.forEach { print(it.eval().o) }
			println("")
			ls[0]
		})
		addFunction("println", { ls ->
			ls.forEach { println(it.eval().o) }
			ls[0]
		})
		addFunction("format", { ls ->
			if (ls.isEmpty()) tooFewArgument(1, ls.size)
			val format = ls[0].eval()
			when (format.o) {
				is String -> ValueNode(String.format(format.o, *ls
						.subList(1, ls.size)
						.map { it.eval().o }
						.toTypedArray()
				))
				else -> typeMisMatch("String", format)
			}
		})
		addFunction("eval", { ls ->
			val value = ls[0].eval()
			when (value.o) {
				is String -> ValueNode(mapAst(buildNode(value.o), this@SymbolList).eval())
				else -> typeMisMatch("String", value)
			}
		})
	}

	inline fun addListProcessingFunctions() {
		addFunction("[]", { ls ->
			ValueNode(ls.map { it.eval().o })
		})
		addFunction("..", { ls ->
			if (ls.size < 2)
				tooFewArgument(2, ls.size)
			val begin = ls[0].eval().o as Int
			val end = ls[1].eval().o as Int
			val progression = when {
				begin <= end -> begin..end
				else -> (begin..end).reversed()
			}
			ValueNode(progression.toList())
		})
	}

	fun initialize() {
		addNumberFunctions()
		addFileFunctions()
		addGetSetFunction()
		addStringFunctions()
		addBoolFunctions()
		addListProcessingFunctions()

		addFunction("", { ls ->
			ls.forEach {
				val res = it.eval()
				println("${res.o.toString()} => ${res.type.name}")
			}
			EmptyNode
		})
		addFunction("if", { ls ->
			if (ls.size < 2)
				tooFewArgument(2, ls.size)
			val a = ls[0].eval().o
			val condition = a as? Boolean ?: (a != null)
			val ret = when {
				condition -> ls[1].eval().o
				ls.size >= 3 -> ls[2].eval().o
				else -> null
			}
			when {
				ret != null -> ValueNode(ret)
				else -> EmptyNode
			}
		})
		addFunction("while", { ls ->
			if (ls.size < 2)
				tooFewArgument(2, ls.size)
			var a = ls[0].eval().o
			var ret: Any? = null
			while (a as? Boolean ?: (a != null)) {
				// execute loop
				ret = ls[1].eval().o
				// update a
				a = ls[0].eval().o
			}
			when {
				ret != null -> ValueNode(ret)
				else -> EmptyNode
			}
		})
		addFunction("for-each", { ls ->
			if (ls.size < 3)
				tooFewArgument(3, ls.size)
			val i = ls[0].eval()
			if (i.o !is String) typeMisMatch("String", i)
			val a = ls[1].eval()
			when (a.o) {
				is List<*> -> {
					var ret: Any? = null
					a.o.forEach {
						setVariable(i.o, ValueNode(it ?: Nullptr))
						ret = ls[2].eval().o
					}
					ValueNode(ret ?: Nullptr)
				}
				else -> typeMisMatch("List", a)
			}
		})
		addFunction("type", { ls ->
			ls.forEach { println(it.eval().type.canonicalName) }
			ls[0]
		})
		addFunction("gc", {
			System.gc()
			EmptyNode
		})
		addFunction("|>", { ls ->
			var ret = Nullptr
			ls.forEach { ret = it.eval() }
			ValueNode(ret)
		})
	}

	fun addFunction(name: String, node: (List<Node>) -> Node): Int {
		functionMap.put(name, functionList.size)
		functionList.add(node)
		return functionList.size - 1
	}

	fun setVariable(name: String, value: Node) {
		variableMap[name] = value
	}

	fun getVariable(name: String) = variableMap[name]

	fun getFunctionId(name: String) = functionMap[name]

	fun getFunction(id: Int) = functionList[id]
}
