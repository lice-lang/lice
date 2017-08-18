package org.lice.api.scripting

import org.lice.core.SymbolList
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.Reader
import java.io.Writer
import javax.script.Bindings
import javax.script.ScriptContext

/**
 * Created by Glavo on 17-6-8.
 *
 * @author Glavo
 * @since 0.1.0
 */

class LiceContext
@JvmOverloads
constructor(
		var bindings: SymbolList = SymbolList(),
		private var reader: Reader = InputStreamReader(System.`in`),
		private var writer: Writer = OutputStreamWriter(System.out),
		private var errorWriter: Writer = OutputStreamWriter(System.err)
) : ScriptContext {
	override fun getReader(): Reader = reader

	override fun setReader(reader: Reader?): Unit {
		this.reader = reader!!
	}

	override fun getWriter(): Writer = writer

	override fun setWriter(writer: Writer?) {
		this.writer = writer!!
	}

	fun removeAttribute(name: String?): Any =
			bindings[name]!!.apply {
				bindings.remove(name)
			}

	override fun removeAttribute(name: String?, scope: Int): Any =
			removeAttribute(name)


	override fun getBindings(scope: Int): Bindings = bindings

	override fun setBindings(bindings: Bindings?, scope: Int) {
		this.bindings = bindings as SymbolList
	}


	override fun getErrorWriter(): Writer = errorWriter


	override fun setErrorWriter(writer: Writer?) {
		this.errorWriter = writer!!
	}


	override fun getAttribute(name: String?): Any =
			bindings[name]!!

	fun setAttribute(name: String?, value: Any?) {
		bindings[name] = value
	}

	override fun getAttribute(name: String?, scope: Int): Any =
			getAttribute(name)

	override fun setAttribute(name: String?, value: Any?, scope: Int) {
		setAttribute(name, value)
	}

	override fun getScopes(): MutableList<Int> =
			throw UnsupportedOperationException()

	override fun getAttributesScope(name: String?): Int =
			throw UnsupportedOperationException()
}