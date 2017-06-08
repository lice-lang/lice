package org.lice.api.scripting

import org.lice.core.SymbolList
import java.io.Reader
import javax.script.*

/**
 * Created by Glavo on 17-6-8.
 *
 * @author Glavo
 * @since 0.1.0
 */
class LiceScriptEngine(n: Bindings) : ScriptEngine {
	val symbolList: SymbolList = TODO()


	override fun createBindings(): Bindings {
		TODO("Function createBindings is not implemented")
	}

	override fun setContext(context: ScriptContext?) {
		TODO("Function setContext is not implemented")
	}

	override fun eval(script: String?, context: ScriptContext?): Any {
		TODO("Function eval is not implemented")
	}

	override fun eval(reader: Reader?, context: ScriptContext?): Any {
		TODO("Function eval is not implemented")
	}

	override fun eval(script: String?): Any {
		TODO("Function eval is not implemented")
	}

	override fun eval(reader: Reader?): Any {
		TODO("Function eval is not implemented")
	}

	override fun eval(script: String?, n: Bindings?): Any {
		TODO("Function eval is not implemented")
	}

	override fun eval(reader: Reader?, n: Bindings?): Any {
		TODO("Function eval is not implemented")
	}

	override fun getBindings(scope: Int): Bindings {
		TODO("Function getBindings is not implemented")
	}

	override fun put(key: String?, value: Any?) {
		TODO("Function put is not implemented")
	}

	override fun getFactory(): ScriptEngineFactory {
		TODO("Function getFactory is not implemented")
	}

	override fun get(key: String?): Any {
		TODO("Function get is not implemented")
	}

	override fun setBindings(bindings: Bindings?, scope: Int) {
		TODO("Function setBindings is not implemented")
	}

	override fun getContext(): ScriptContext {
		TODO("Function getContext is not implemented")
	}

}