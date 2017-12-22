package org.lice.api.scripting

import org.lice.core.SymbolList
import org.lice.model.Node
import org.lice.model.ValueNode
import org.lice.util.cast
import java.io.*
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

	override fun setReader(reader: Reader) {
		this.reader = reader
	}

	override fun getWriter(): Writer = writer

	override fun setWriter(writer: Writer?) {
		this.writer = writer!!
	}

	fun removeAttribute(name: String) = bindings.removeVariable(name)
	override fun removeAttribute(name: String, scope: Int) = removeAttribute(name)

	override fun getBindings(scope: Int): Bindings = bindings

	override fun setBindings(bindings: Bindings?, scope: Int) {
		this.bindings = cast(bindings)
	}

	override fun getErrorWriter(): Writer = errorWriter

	override fun setErrorWriter(writer: Writer) {
		this.errorWriter = writer
	}

	override fun getAttribute(name: String) = (bindings.getVariable(name) as Node).eval()

	fun setAttribute(name: String, value: Any?) {
		bindings.defineVariable(name, ValueNode(value))
	}

	override fun getAttribute(name: String, scope: Int) = getAttribute(name)
	override fun setAttribute(name: String, value: Any?, scope: Int) = setAttribute(name, value)

	override fun getScopes(): MutableList<Int> = throw UnsupportedOperationException()
	override fun getAttributesScope(name: String?): Int = throw UnsupportedOperationException()
}