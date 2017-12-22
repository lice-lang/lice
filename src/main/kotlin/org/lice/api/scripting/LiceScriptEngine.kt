package org.lice.api.scripting

import org.lice.core.SymbolList
import org.lice.parse.buildNode
import org.lice.parse.mapAst
import java.io.Reader
import javax.script.*

/**
 * Created by Glavo on 17-6-8.
 *
 * @author Glavo
 * @since 0.1.0
 */
class LiceScriptEngine : ScriptEngine {
	private var context: LiceContext = LiceContext()

	override fun createBindings(): SymbolList = SymbolList()

	override fun setContext(context: ScriptContext) {
		if (context is LiceContext) this.context = context
	}

	override fun eval(script: String, context: ScriptContext): Any? {
		context as LiceContext
		return eval(script, context.bindings)
	}

	override fun eval(reader: Reader, context: ScriptContext): Any? {
		context as LiceContext
		return eval(reader.readText(), context.bindings)
	}

	override fun eval(script: String): Any? {
		return eval(script, context.bindings)
	}

	override fun eval(reader: Reader): Any? {
		return eval(reader.readText(), context.bindings)
	}

	override fun eval(script: String, n: Bindings?): Any? {
		n as SymbolList

		return mapAst(buildNode(script), n).eval()
	}

	override fun eval(reader: Reader?, n: Bindings?): Any? {
		return eval(reader!!.readText(), n)
	}

	override fun getBindings(scope: Int): SymbolList =
			context.bindings

	override fun put(key: String?, value: Any?) {
		context.bindings[key] = value
	}

	override fun getFactory(): LiceScriptEngineFactory =
			LiceScriptEngineFactory()

	override fun get(key: String?): Any =
			context.bindings[key] ?: throw NoSuchElementException(key)

	override fun setBindings(bindings: Bindings?, scope: Int) {
		if (bindings is SymbolList)
			context.bindings = bindings
	}

	override fun getContext(): LiceContext =
			context

}