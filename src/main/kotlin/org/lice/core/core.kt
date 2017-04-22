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
import org.lice.compiler.util.forceRun
import org.lice.lang.*
import java.awt.Image
import java.awt.image.RenderedImage
import java.io.File
import java.net.URL
import javax.imageio.ImageIO
import kotlin.concurrent.thread

@SinceKotlin("1.1")
typealias ParamList = List<String>

@SinceKotlin("1.1")
typealias Mapper<T> = (T) -> T

private var lambdaNameCounter = -100

internal fun lambdaNameGen() = "\t${++lambdaNameCounter}"

inline fun Any?.booleanValue() = this as? Boolean ?: (this != null)

fun SymbolList.addStandard() {
	addGetSetFunction()
	addControlFlowFunctions()
	addNumberFunctions()
	addLiterals()
	addStringFunctions()
	addBoolFunctions()
	addCollectionsFunctions()
	addListFunctions()
	val defFunc = { name: String, params: ParamList, block: Mapper<Node>, body: Node ->
		defineFunction(name, { ln, args ->
			val backup = params.map { getFunction(it) }
			if (args.size != params.size)
				numberOfArgumentNotMatch(params.size, args.size, ln)
			args
					.map(block)
					.forEachIndexed { index, obj ->
						when (obj) {
							is SymbolNode -> defineFunction(params[index], obj.function())
							else -> defineFunction(params[index], { _, _ -> obj })
						}
					}
			val ret = ValueNode(body.eval().o ?: Nullptr, ln)
			backup.forEachIndexed { index, node ->
				if (node != null) defineFunction(params[index], node)
				else removeFunction(params[index])
			}
			ret
		})
	}
	val definer = { funName: String, block: Mapper<Node> ->
		defineFunction(funName, { meta, ls ->
			if (ls.size < 2) tooFewArgument(2, ls.size, meta)
			val name = (ls.first() as SymbolNode).name
			val body = ls.last()
			val params = ls
					.subList(1, ls.size - 1)
					.map {
						when (it) {
							is SymbolNode -> it.name
							else -> InterpretException.notSymbol(meta)
						}
					}
			val override = isFunctionDefined(name)
			defFunc(name, params, block, body)
			return@defineFunction ValueNode(DefineResult(
					"${if (override) "overridden" else "defined"}: $name"))
		})
	}
	definer("def", { node -> ValueNode(node.eval().o ?: Nullptr) })
	definer("deflazy", { node -> LazyValueNode({ node.eval() }) })
	definer("defexpr", { it })
	val lambdaDefiner = { funName: String, mapper: Mapper<Node> ->
		defineFunction(funName, { meta, ls ->
			if (ls.isEmpty()) tooFewArgument(1, ls.size, meta)
			val body = ls.last()
			val params = ls
					.subList(0, ls.size - 1)
					.map {
						when (it) {
							is SymbolNode -> it.name
							else -> typeMisMatch("Symbol", it.eval(), meta)
						}
					}
			val name = lambdaNameGen()
			defFunc(name, params, mapper, body)
			SymbolNode(this, name, meta)
		})
	}
	lambdaDefiner("lambda", { node -> ValueNode(node.eval().o ?: Nullptr) })
	lambdaDefiner("lazy", { node -> LazyValueNode({ node.eval() }) })
	lambdaDefiner("expr", { it })
	defineFunction("def?", { ln, ls ->
		val a = (ls.first() as? SymbolNode)?.name
		ValueNode(isFunctionDefined(a), ln)
	})
	defineFunction("undef", { ln, ls ->
		val a = (ls.first() as? SymbolNode)?.name
		ValueNode(null != removeFunction(a), ln)
	})
	defineFunction("alias", { meta, ls ->
		val a = getFunction((ls.first() as? SymbolNode)?.name)
		a?.let { function ->
			ls.forEachIndexed { index, _ ->
				if (index != 0)
					defineFunction((ls[index] as SymbolNode).name, function)
			}
		}
		ValueNode(null != a, meta)
	})

	provideFunctionWithMeta("eval", { ln, ls ->
		val value = ls.first()
		when (value) {
			is String -> mapAst(buildNode(value), symbolList = this).eval().o
			else -> typeMisMatch("String", value, ln)
		}
	})
	provideFunction("debug", {
		BeforeEval.hook = { Echoer.echoln("eval =>> $this") }
		null
	})
	provideFunction("print", { ls ->
		ls.forEach { Echoer.echo(it) }
		if (ls.isNotEmpty()) ls.last() else null
	})
	provideFunction("print", { ls ->
		ls.forEach { Echoer.echoErr(it) }
		if (ls.isNotEmpty()) ls.last() else null
	})
	provideFunctionWithMeta("new", { meta, ls ->
		val a = ls.first()
		when (a) {
			is String -> Class.forName(a).newInstance()
			else -> typeMisMatch("String", a, meta)
		}
	})
	defineFunction("", { _, ls ->
		var ret = Nullptr
		ls.forEach {
			val res = it.eval()
			ret = res
			Echoer.echoln("${res.o.toString()} => ${res.type.name}")
		}
		ValueNode(ret)
	})
	provideFunction("type", { ls ->
		ls.first()?.javaClass ?: NullptrType::class.java
	})
	provideFunction("gc", { System.gc() })
	provideFunction("|>", { it.last() })
	defineFunction("force|>", { ln, ls ->
		var ret = Nullptr
		forceRun { ls.forEach { node -> ret = node.eval() } }
		ValueNode(ret, ln)
	})
	defineFunction("no-run|>", { ln, _ -> getNullNode(ln) })

	provideFunctionWithMeta("load-file", { ln, ls ->
		val o = ls.first()
		when (o) {
			is File -> createRootNode(o, this)
			is String -> createRootNode(File(o), this)
			else -> typeMisMatch("File or String", o, ln)
		}
	})

	provideFunction("exit", { System.exit(0) })

	defineFunction("str->sym", { ln, ls ->
		val a = ls.first().eval()
		when (a.o) {
			is String -> SymbolNode(this, a.o, ln)
			else -> typeMisMatch("String", a, ln)
		}
	})

	defineFunction("sym->str", { ln, ls ->
		val a = ls.first()
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
		val str = (ls.first() as SymbolNode).name
		val v = ls[1]
		val res = when (v) {
			is SymbolNode -> v
			else -> ValueNode(v.eval().o ?: Nullptr, ln)
		}
		defineFunction(str, { _, _ -> res })
		res
	})
	defineFunction("<->", { ln, ls ->
		if (ls.size < 2)
			tooFewArgument(2, ls.size, ln)
		val str = (ls.first() as SymbolNode).name
		if (!isFunctionDefined(str)) {
			val node = ValueNode(ls[1].eval(), ln)
			defineFunction(str, { _, _ -> node })
			return@defineFunction node
		}
		getFunction(str)?.invoke(ln, emptyList()) ?: getNullNode(ln)
	})
}

inline fun SymbolList.addControlFlowFunctions() {
	defineFunction("if", { ln, ls ->
		if (ls.size < 2)
			tooFewArgument(2, ls.size, ln)
		val a = ls.first().eval().o
		val condition = a.booleanValue()
		when {
			condition -> ls[1]
			ls.size >= 3 -> ls[2]
			else -> getNullNode(ln)
		}
	})
	defineFunction("when", { ln, ls ->
		for (i in (0..ls.size - 2) step 2) {
			val a = ls[i].eval().o
			val condition = a.booleanValue()
			if (condition) return@defineFunction ls[i + 1]
		}
		if (ls.size % 2 == 0) getNullNode(ln)
		else ls.last()
	})
	defineFunction("while", { ln, ls ->
		if (ls.size < 2)
			tooFewArgument(2, ls.size, ln)
		var a = ls.first().eval().o
		var ret: Node = EmptyNode(ln)
		while (a.booleanValue()) {
			// execute loop
			ret.eval()
			ret = ls[1]
			// update a
			a = ls.first().eval().o
		}
		ret
	})
}

inline fun SymbolList.addConcurrentFunctions() {
	defineFunction("thread|>", { ln, ls ->
		var ret: Any? = null
		thread { ls.forEach { node -> ret = node.eval().o } }
		ValueNode(ret, ret?.javaClass ?: Any::class.java, ln)
	})
	provideFunctionWithMeta("sleep", { ln, ls ->
		val a = ls.first()
		when (a) {
			is Number -> Thread.sleep(a.toLong())
			else -> typeMisMatch("Number", a, ln)
		}
		a
	})
}

inline fun SymbolList.addFileFunctions() {
	provideFunction("file", { File(it.first().toString()).apply { if (!exists()) createNewFile() } })
	provideFunction("url", { URL(it.first().toString()) })
	provideFunction("dir", { File(it.first().toString()).apply { if (!exists()) mkdirs() } })
	provideFunction("file-exist?", { File(it.first().toString()).exists() })
	provideFunctionWithMeta("read-file", { ln, ls ->
		val a = ls.first()
		when (a) {
			is File -> a.readText()
			else -> typeMisMatch("File", a, ln)
		}
	})
	provideFunctionWithMeta("read-url", { ln, ls ->
		val a = ls.first()
		when (a) {
			is URL -> a.readText()
			else -> typeMisMatch("URL", a, ln)
		}
	})
	provideFunctionWithMeta("write-file", { ln, ls ->
		val a = ls.first()
		val b = ls[1]
		when (a) {
			is File -> {
				when (b) {
					is Image -> ImageIO.write(b as RenderedImage, "PNG", a)
					else -> a.writeText(b.toString())
				}
			}
			else -> typeMisMatch("File", a, ln)
		}
		a
	})
}

inline fun SymbolList.addMathFunctions() {
	provideFunction("sqrt", { Math.sqrt((it.first() as Number).toDouble()) })
	provideFunction("cbrt", { Math.cbrt((it.first() as Number).toDouble()) })
	provideFunction("sin", { Math.sin((it.first() as Number).toDouble()) })
	provideFunction("sinh", { Math.sinh((it.first() as Number).toDouble()) })
	provideFunction("cosh", { Math.cosh((it.first() as Number).toDouble()) })
	provideFunction("rand", {
		if (it.isNotEmpty()) rand.nextInt(it.first() as Int)
		else rand.nextInt()
	})
}

inline fun SymbolList.addStringFunctions() {
	provideFunction("->str", { it.first().toString() })
	provideFunctionWithMeta("str->int", { ln, ls ->
		val res = ls.first()
		when (res) {
			is String -> when {
				res.isOctInt() -> res.toOctInt()
				res.isInt() -> res.toInt()
				res.isBinInt() -> res.toBinInt()
				res.isHexInt() -> res.toHexInt()
				else -> throw InterpretException("give string: \"$res\" cannot be parsed as a number!", ln)
			}
			else -> typeMisMatch("String", res, ln)
		}
	})
	provideFunctionWithMeta("int->hex", { ln, ls ->
		val a = ls.first()
		when (a) {
			is Int -> "0x${Integer.toHexString(a)}"
			else -> typeMisMatch("Int", a, ln)
		}
	})
	provideFunctionWithMeta("int->bin", { ln, ls ->
		val a = ls.first()
		when (a) {
			is Int -> "0b${Integer.toBinaryString(a)}"
			else -> typeMisMatch("Int", a, ln)
		}
	})
	provideFunctionWithMeta("int->oct", { ln, ls ->
		val a = ls.first()
		when (a) {
			is Int -> "0${Integer.toOctalString(a)}"
			else -> typeMisMatch("Int", a, ln)
		}
	})
	provideFunction("str-con", {
		it.fold(StringBuilder(it.size)) { sb, value -> sb.append(value.toString()) }.toString()
	})
	provideFunctionWithMeta("format", { ln, ls ->
		if (ls.isEmpty()) InterpretException.tooFewArgument(1, ls.size, ln)
		val format = ls.first()
		when (format) {
			is String -> String.format(format, *ls.toTypedArray())
			else -> typeMisMatch("String", format, ln)
		}
	})
	provideFunction("->chars", {
		it.fold(StringBuilder(it.size)) { sb, value ->
			sb.append(value.toString())
		}.toString().toCharArray().toList()
	})
	provideFunction("split", { ls ->
		val str = ls.first()
		val regex = ls[1]
		str.toString().split(regex.toString()).toList()
	})
}

inline fun SymbolList.addListFunctions() {
	provideFunction("[|]", { ls ->
		ls.reduceRight { value, pairs: Any? ->
			Pair(value, pairs)
		}
	})
	provideFunction("[|", { ls ->
		val a = ls.first()
		when (a) {
			is Pair<*, *> -> a.first
			is Collection<*> -> a.first()
			else -> null
		}
	})
	provideFunction("|]", { ls ->
		val a = ls.first()
		when (a) {
			is Pair<*, *> -> a.second
			is Iterable<*> -> a.drop(1)
			else -> null
		}
	})
}

inline fun SymbolList.addCollectionsFunctions() {
	provideFunctionWithMeta("..", { ln, ls ->
		if (ls.size < 2) tooFewArgument(2, ls.size, ln)
		val a = ls.first()
		val b = ls[1]
		return@provideFunctionWithMeta when {
			a is Number && b is Number -> {
				val begin = a.toInt()
				val end = b.toInt()
				when {
					begin <= end -> (begin..end).toList()
					else -> (end..begin).reversed().toList()
				}
			}
			else -> typeMisMatch("Number", a as? Number ?: b, ln)
		}
	})
	provideFunction("list", { it })
	defineFunction("for-each", { ln, ls ->
		if (ls.size < 3)
			tooFewArgument(3, ls.size, ln)
		val i = (ls.first() as SymbolNode).name
		val a = ls[1].eval()
		when (a.o) {
			is Collection<*> -> {
				var ret: Any? = null
				a.o.forEach {
					defineFunction(i, { _, _ -> ValueNode(it ?: Nullptr, ln) })
					ret = ls[2].eval().o
				}
				ValueNode(ret ?: Nullptr, ln)
			}
			else -> typeMisMatch("List", a, ln)
		}
	})
	provideFunction("size", {
		val i = it.first()
		when (i) {
			is Collection<*> -> i.size
			is Iterable<*> -> i.count()
			else -> -1
		}
	})
	provideFunction("reverse", { ls ->
		val i = ls.first()
		when (i) {
			is Collection<*> -> i.reversed()
			is Iterable<*> -> i.reversed()
			else -> emptyList()
		}
	})
	provideFunction("count", { ls ->
		val i = ls.first()
		val e = ls[1]
		when (i) {
			is Collection<*> -> i.count { it == e }
			is Iterable<*> -> i.count { it == e }
			else -> -1
		}
	})
}
