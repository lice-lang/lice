package org.lice.api.scripting

import org.junit.Assert.assertEquals
import org.junit.Test
import org.lice.core.LiceEntry
import javax.script.ScriptEngineManager
import kotlin.test.assertFailsWith

/**
 * Created by Glavo on 17-8-11.
 *
 * @author Glavo, ice1000
 * @since 0.1.0
 */
class LiceScriptEngineTest {
	@Test
	fun testAPI() {
		val engine = ScriptEngineManager().getEngineByName("lice")
		val context = LiceContext()
		context.bindings = context.bindings
		context.removeAttribute("+")
		context.setAttribute("assss", 666)
		context.errorWriter = context.errorWriter
		context.writer = context.writer
		context.reader = context.reader
		engine.context = context
		engine.context = engine.context
		val engineBindings = engine.getBindings(0)
		engine.setBindings(engineBindings, 0)
		context.setBindings(context.getBindings(0), 0)
		println(engine.factory.engineName)
		println(engine.factory.engineVersion)
		println(engine.factory.extensions)
		println(engine.factory.getProgram(""))
		println(engine.factory.languageName)
		println(engine.factory.languageVersion)
		println(engine.factory.getOutputStatement("233"))
		println(engine.factory.mimeTypes)
		println(engine.factory.getParameter("javax.script.engine_version"))
		println(engine.factory.getParameter("javax.script.engine"))
		println(engine.factory.getParameter(""))
		assertFailsWith<UnsupportedOperationException> { context.scopes }
		assertFailsWith<UnsupportedOperationException> { engine.factory.getMethodCallSyntax("", "") }
		assertFailsWith<UnsupportedOperationException> { context.getAttributesScope("+") }
		assertEquals(0, engine.eval("0"))
		val bindings = engine.createBindings()
		engine.eval("(-> x 10)", bindings)
		assertEquals(10, engine.eval("x", bindings))
		engine.eval("(-> x 10)")
		engine.put("o", 23)
		assertEquals(bindings.keys.toList(), bindings.entries.map { it.key })
		assertEquals(bindings.values.toList(), bindings.entries.map { it.value })
		println(bindings.containsKey("+"))
		println(bindings.containsValue("+"))
		println(bindings.size)
		for (binding in bindings) println(binding.key)
		println(bindings.isEmpty())
		bindings.entries.forEach { println(it.key) }
		bindings.entries.iterator().remove()
		bindings.clear()
	}
}