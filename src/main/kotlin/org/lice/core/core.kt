/**
 * Created by ice1000 on 2017/2/25.
 *
 * @author ice1000
 * @since 1.0.0
 */
@file:Suppress("NOTHING_TO_INLINE")
@file:JvmName("Standard")
@file:JvmMultifileClass

package org.lice.core

import org.lice.compiler.model.*
import org.lice.compiler.model.Node.Objects.getNullNode
import org.lice.compiler.model.Value.Objects.Nullptr
import org.lice.compiler.parse.*
import org.lice.compiler.util.InterpretException
import org.lice.compiler.util.InterpretException.Factory.numberOfArgumentNotMatch
import org.lice.compiler.util.InterpretException.Factory.tooFewArgument
import org.lice.compiler.util.InterpretException.Factory.typeMisMatch
import org.lice.compiler.util.SymbolList
import org.lice.compiler.util.forceRun
import org.lice.lang.DefineResult
import org.lice.lang.Pair
import org.lice.lang.Symbol
import java.awt.Image
import java.awt.image.RenderedImage
import java.io.File
import java.net.URL
import javax.imageio.ImageIO
import kotlin.concurrent.thread

inline fun SymbolList.addStandard() {
	addGetSetFunction()
	addControlFlowFunctions()
	addNumberFunctions()
	addStringFunctions()
	addBoolFunctions()
	addCollectionsFunctions()
	addListFunctions()

	defineFunction("def", { ln, ls ->
		if (ls.size < 2) tooFewArgument(2, ls.size, ln)
		val name = (ls[0] as SymbolNode).name
		val body = ls.last()
		val params = ls
				.subList(1, ls.size - 1)
				.map {
					when (it) {
						is SymbolNode -> it.name
						else -> typeMisMatch("Symbol", it.eval(), ln)
					}
				}
		val override = isFunctionDefined(name)
		defineFunction(name, { ln, args ->
			val backup = params.map { getVariable(it) }
			if (args.size != params.size)
				numberOfArgumentNotMatch(params.size, args.size, ln)
			args
					.map { node ->
						node.eval().o ?: Nullptr
					}
					.forEachIndexed { index, obj ->
						setVariable(params[index], ValueNode(obj))
					}
			val ret = ValueNode(body.eval().o ?: Nullptr, ln)
			backup.forEachIndexed { index, node ->
				if (node != null)
					setVariable(params[index], node)
				else
					removeVariable(params[index])
			}
			ret
		})
		return@defineFunction ValueNode(DefineResult(
				"${if (override) "overriding" else "new function defined"}: $name"))
	})
	defineFunction("defexpr", { ln, ls ->
		if (ls.size < 2) tooFewArgument(2, ls.size, ln)
		val name = (ls[0] as SymbolNode).name
		val body = ls.last()
		val params = ls
				.subList(1, ls.size - 1)
				.map {
					when (it) {
						is SymbolNode -> it.name
						else -> typeMisMatch("Symbol", it.eval(), ln)
					}
				}
		val override = isFunctionDefined(name)
		defineFunction(name, { ln, args ->
			val backup = params.map { getVariable(it) }
			if (args.size != params.size)
				numberOfArgumentNotMatch(params.size, args.size, ln)
			args
					.map { node -> FExprValueNode({ node.eval().o }) }
					.forEachIndexed { index, fexpr ->
						setVariable(params[index], fexpr)
					}
			val ret = ValueNode(body.eval().o ?: Nullptr, ln)
			backup.forEachIndexed { index, node ->
				if (node != null)
					setVariable(params[index], node)
				else
					removeVariable(params[index])
			}
			ret
		})
		return@defineFunction ValueNode(DefineResult(
				"${if (override) "overriding" else "new function defined"}: $name"))
	})
	defineFunction("def?", { ln, ls ->
		val a = (ls[0] as SymbolNode).name
		ValueNode(isFunctionDefined(a), ln)
	})
	defineFunction("undef", { ln, ls ->
		val a = (ls[0] as SymbolNode).name
		removeFunction(a)
		getNullNode(ln)
	})
	defineFunction("alias", { meta, ls ->
		val a = getFunction((ls[0] as SymbolNode).name)
		a?.let { function ->
			ls.forEachIndexed { index, _ ->
				if (index != 0)
					defineFunction((ls[index] as SymbolNode).name, function)
			}
		}
		getNullNode(meta)
	})

	defineFunction("eval", { ln, ls ->
		val value = ls[0].eval()
		when (value.o) {
			is String -> ValueNode(mapAst(
					node = buildNode(value.o),
					symbolList = this
			).eval(), ln)
			else -> typeMisMatch("String", value, ln)
		}
	})

	defineFunction("print", { ln, ls ->
		ls.forEach { print(it.eval().o) }
		if (ls.isNotEmpty()) ls.last() else EmptyNode(ln)
	})
	defineFunction("print-err", { ln, ls ->
		ls.forEach { System.err.print(it.eval().o.toString()) }
		if (ls.isNotEmpty()) ls.last() else EmptyNode(ln)
	})
	defineFunction("println-err", { ln, ls ->
		ls.forEach { System.err.print(it.eval().o.toString()) }
		System.err.println()
		if (ls.isNotEmpty()) ls.last() else EmptyNode(ln)
	})
	defineFunction("println", { ln, ls ->
		ls.forEach { print(it.eval().o) }
		println()
		if (ls.isNotEmpty()) ls.last() else EmptyNode(ln)
	})

	defineFunction("new", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is String -> ValueNode(Class.forName(a.o).newInstance(), ln)
			is Symbol -> ValueNode(Class.forName(a.o.name).newInstance(), ln)
			else -> typeMisMatch(
					expected = "String or Symbol",
					actual = a,
					meta = ln
			)
		}
	})
	defineFunction("", { _, ls ->
		var ret = Nullptr
		ls.forEach {
			val res = it.eval()
			ret = res
			println("${res.o.toString()} => ${res.type.name}")
		}
		ValueNode(ret)
	})
	defineFunction("type", { _, ls ->
		ls.forEach { println(it.eval().type.canonicalName) }
		ls.last()
	})
	defineFunction("gc", { ln, _ ->
		System.gc()
		getNullNode(ln)
	})

	defineFunction("|>", { ln, ls ->
		var ret = Nullptr
		ls.forEach { ret = it.eval() }
		ValueNode(ret, ln)
	})
	defineFunction("force|>", { ln, ls ->
		var ret = Nullptr
		forceRun { ls.forEach { node -> ret = node.eval() } }
		ValueNode(ret, ln)
	})
	defineFunction("no-run|>", { ln, _ -> getNullNode(ln) })

	defineFunction("load-file", { ln, ls ->
		val o = ls[0].eval()
		when (o.o) {
			is File -> ValueNode(createRootNode(
					file = o.o,
					symbolList = this
			).eval(), ln)
			is String -> ValueNode(createRootNode(
					file = File(o.o),
					symbolList = this
			).eval(), ln)
			else -> typeMisMatch(
					expected = "File",
					actual = o,
					meta = ln
			)
		}
	})

	defineFunction("exit", { ln, _ ->
		System.exit(0)
		getNullNode(ln)
	})

	defineFunction("str->sym", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is String -> SymbolNode(this, a.o, ln)
			else -> typeMisMatch("String", a, ln)
		}
	})

	defineFunction("sym->str", { ln, ls ->
		val a = ls[0]
		when (a) {
			is SymbolNode -> ValueNode(a.name, ln)
			else -> typeMisMatch("Symbol", a, ln)
		}
	})
}

inline fun SymbolList.addGetSetFunction() {
	defineFunction("->", { ln, ls ->
		if (ls.size < 2)
			tooFewArgument(2, ls.size, ln)
		val str = (ls[0] as SymbolNode).name
		val res = ValueNode(ls[1].eval(), ln)
		setVariable(str, res)
		res
	})
	defineFunction("<->", { ln, ls ->
		if (ls.size < 2)
			tooFewArgument(2, ls.size, ln)
		val str = (ls[0] as SymbolNode).name
		if (getVariable(name = str) == null) {
			val node = ValueNode(ls[1].eval(), ln)
			setVariable(
					name = str,
					value = node
			)
			return@defineFunction node
		}
		getVariable(name = str)!!
	})
}

inline fun SymbolList.addControlFlowFunctions() {
	defineFunction("if", { ln, ls ->
		if (ls.size < 2)
			tooFewArgument(2, ls.size, ln)
		val a = ls[0].eval().o
		val condition = a as? Boolean ?: (a != null)
		val ret = when {
			condition -> ls[1].eval().o
			ls.size >= 3 -> ls[2].eval().o
			else -> null
		}
		when {
			ret != null -> ValueNode(ret, ln)
			else -> getNullNode(ln)
		}
	})
	defineFunction("when", { ln, ls ->
		for (i in (0..ls.size - 2) step 2) {
			val a = ls[i].eval().o
			val condition = a as? Boolean ?: (a != null)
			val ret = when {
				condition -> ls[i + 1].eval().o
				else -> null
			}
			if (ret != null) return@defineFunction ValueNode(ret, ln)
		}
		if (ls.size % 2 == 0) getNullNode(ln)
		else ValueNode(ls.last().eval())
	})
	defineFunction("while", { ln, ls ->
		if (ls.size < 2)
			tooFewArgument(2, ls.size, ln)
		var a = ls[0].eval().o
		var ret: Any? = null
		while (a as? Boolean ?: (a != null)) {
			// execute loop
			ret = ls[1].eval().o
			// update a
			a = ls[0].eval().o
		}
		when {
			ret != null -> ValueNode(ret, ln)
			else -> getNullNode(ln)
		}
	})
}

inline fun SymbolList.addConcurrentFunctions() {
	defineFunction("thread|>", { ln, ls ->
		var ret: Node = getNullNode(ln)
		thread { ls.forEach { node -> ret = ValueNode(node.eval()) } }
		ret
	})
	defineFunction("sleep", { ln, ls ->
		val a = ls[0].eval()
		when {
			a.o is Number -> Thread.sleep(a.o.toLong())
			else -> typeMisMatch("Number", a, ln)
		}
		getNullNode(ln)
	})
}

inline fun SymbolList.addFileFunctions() {
	defineFunction("file", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is String -> ValueNode(File(a.o)
					.apply { if (!exists()) createNewFile() }, ln)
			else -> typeMisMatch("String", a, ln)
		}
	})
	defineFunction("directory", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is String -> ValueNode(File(a.o)
					.apply { if (!exists()) mkdirs() }, ln)
			else -> typeMisMatch("String", a, ln)
		}
	})
	defineFunction("file-exists?", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is String -> ValueNode(File(a.o).exists(), ln)
			else -> typeMisMatch("String", a, ln)
		}
	})
	defineFunction("read-file", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is File -> ValueNode(a.o.readText(), ln)
			else -> typeMisMatch("File", a, ln)
		}
	})
	defineFunction("url", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is String -> ValueNode(URL(a.o), ln)
			else -> typeMisMatch("String", a, ln)
		}
	})
	defineFunction("read-url", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is URL -> ValueNode(a.o.readText(), ln)
			else -> typeMisMatch("URL", a, ln)
		}
	})
	defineFunction("write-file", { ln, ls ->
		val a = ls[0].eval()
		val b = ls[1].eval()
		when (a.o) {
			is File -> {
				when (b.o) {
					is Image -> ImageIO.write(b.o as RenderedImage, "PNG", a.o)
					else -> a.o.writeText(b.o.toString())
				}
			}
			else -> typeMisMatch("File", a, ln)
		}
		ValueNode(a.o, ln)
	})
}

inline fun SymbolList.addMathFunctions() {
	defineFunction("sqrt", { ln, ls ->
		ValueNode(Math.sqrt((ls[0].eval().o as Number).toDouble()), ln)
	})
	defineFunction("cbrt", { ln, ls ->
		ValueNode(Math.cbrt((ls[0].eval().o as Number).toDouble()), ln)
	})
	defineFunction("sin", { ln, ls ->
		ValueNode(Math.sin((ls[0].eval().o as Number).toDouble()), ln)
	})
	defineFunction("sinh", { ln, ls ->
		ValueNode(Math.sinh((ls[0].eval().o as Number).toDouble()), ln)
	})
	defineFunction("cosh", { ln, ls ->
		ValueNode(Math.cosh((ls[0].eval().o as Number).toDouble()), ln)
	})
	defineFunction("rand", { ln, ls ->
		if (ls.isNotEmpty()) ValueNode(rand.nextInt(ls[0].eval().o as Int), ln)
		else ValueNode(rand.nextInt(), ln)
	})
}


inline fun SymbolList.addStringFunctions() {
	defineFunction("->str", { ln, ls -> ValueNode(ls[0].eval().o.toString(), ln) })
	defineFunction("str->int", { ln, ls ->
		val res = ls[0].eval()
		when (res.o) {
			is String -> ValueNode(when {
				res.o.isOctInt() -> res.o.toOctInt()
				res.o.isInt() -> res.o.toInt()
				res.o.isBinInt() -> res.o.toBinInt()
				res.o.isHexInt() -> res.o.toHexInt()
				else -> throw InterpretException("give string: \"${res.o}\" cannot be parsed as a number!", ln)
			}, ln)
			else -> typeMisMatch("String", res, ln)
		}
	})
	defineFunction("int->hex", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is Int -> ValueNode("0x${Integer.toHexString(a.o)}", ln)
			else -> typeMisMatch("Int", a, ln)
		}
	})
	defineFunction("int->bin", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is Int -> ValueNode("0b${Integer.toBinaryString(a.o)}", ln)
			else -> typeMisMatch("Int", a, ln)
		}
	})
	defineFunction("int->oct", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is Int -> ValueNode("0${Integer.toOctalString(a.o)}", ln)
			else -> typeMisMatch("Int", a, ln)
		}
	})
	defineFunction("str-con", { ln, ls ->
		ValueNode(ls.fold(StringBuilder(ls.size)) { sb, value ->
			sb.append(value.eval().o.toString())
		}.toString(), ln)
	})
	defineFunction("format", { ln, ls ->
		if (ls.isEmpty()) InterpretException.tooFewArgument(1, ls.size, ln)
		val format = ls[0].eval()
		when (format.o) {
			is String -> ValueNode(kotlin.String.format(format.o, *ls
					.subList(1, ls.size)
					.map { it.eval().o }
					.toTypedArray()
			), ln)
			else -> typeMisMatch("String", format, ln)
		}
	})
	defineFunction("->chars", { ln, ls ->
		ValueNode(ls.fold(StringBuilder(ls.size)) { sb, value ->
			sb.append(value.eval().o.toString())
		}
				.toString()
				.toCharArray()
				.toList(), ln)
	})
	defineFunction("split", { ln, ls ->
		val str = ls[0].eval()
		val regex = ls[1].eval()
		ValueNode(str
				.o
				.toString()
				.split(regex.o.toString())
				.toList(), ln)
	})
}

inline fun SymbolList.addListFunctions() {
	defineFunction("[|]", { ln, ls ->
		ValueNode(ls.foldRight(null) { value, pairs: Any? ->
			Pair(value.eval().o, pairs)
		}, Pair::class.java, ln)
	})
	defineFunction("head", { ln, ls ->
		val a = ls[0].eval()
		if (a.o is Pair<*, *>) when (a.o.first) {
			null -> EmptyNode(ln)
			else -> ValueNode(a.o.first, ln)
		}
		else typeMisMatch("Pair", a, ln)
	})
	defineFunction("tail", { ln, ls ->
		val a = ls[0].eval()
		if (a.o is Pair<*, *>) when (a.o.second) {
			null -> EmptyNode(ln)
			else -> ValueNode(a.o.second, ln)
		}
		else typeMisMatch("Pair", a, ln)
	})
}

inline fun SymbolList.addCollectionsFunctions() {
	defineFunction("..", { ln, ls ->
		if (ls.size < 2)
			tooFewArgument(2, ls.size, ln)
		val a = ls[0].eval()
		val b = ls[1].eval()
		return@defineFunction when {
			a.o is Number && b.o is Number -> {
				val begin = a.o.toInt()
				val end = b.o.toInt()
				val progression = when {
					begin <= end -> begin..end
					else -> (begin..end).reversed()
				}
				ValueNode(progression.toList(), ln)
			}
			else -> typeMisMatch("Number", if (a.o is Number) a else b, ln)
		}
	})
	defineFunction("list", { ln, ls ->
		ValueNode(ls.map { it.eval().o }, ln)
	})
	defineFunction("for-each", { ln, ls ->
		if (ls.size < 3)
			tooFewArgument(3, ls.size, ln)
		val i = (ls[0] as SymbolNode).name
		val a = ls[1].eval()
		when (a.o) {
			is Collection<*> -> {
				var ret: Any? = null
				a.o.forEach {
					setVariable(i, ValueNode(it ?: Nullptr, ln))
					ret = ls[2].eval().o
				}
				ValueNode(ret ?: Nullptr, ln)
			}
			else -> typeMisMatch("List", a, ln)
		}
	})
	defineFunction("size", { ln, ls ->
		val i = ls[0].eval()
		when (i.o) {
			is Collection<*> -> ValueNode(i.o.size, ln)
			else -> ValueNode(ls.size, ln)
		}
	})
	defineFunction("reverse", { ln, ls ->
		val i = ls[0].eval()
		when (i.o) {
			is Collection<*> -> ValueNode(i.o.reversed(), ln)
			else -> ValueNode(ls.size, ln)
		}
	})
	defineFunction("count", { ln, ls ->
		val i = ls[0].eval()
		val e = ls[1].eval()
		when (i.o) {
			is Collection<*> -> ValueNode(i.o.count { e.o == it }, ln)
			else -> ValueNode(0, ln)
		}
	})
	defineFunction("empty?", { ln, ls ->
		ValueNode((ls[0].eval().o as? Collection<*>)?.isEmpty() ?: true, ln)
	})
	defineFunction("in?", { ln, ls ->
		val i = ls[0].eval()
		val e = ls[1].eval()
		when (i.o) {
			is Collection<*> -> ValueNode(e.o in i.o, ln)
			else -> ValueNode(false, ln)
		}
	})
}
