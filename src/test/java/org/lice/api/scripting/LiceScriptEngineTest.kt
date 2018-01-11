package org.lice.api.scripting

import org.junit.Assert.assertEquals
import org.junit.Test
import javax.script.ScriptEngineManager

/**
 * Created by Glavo on 17-8-11.
 *
 * @author Glavo
 * *
 * @since 0.1.0
 */
class LiceScriptEngineTest {
	@Test
	fun testAPI() {
		val engine = ScriptEngineManager().getEngineByName("lice")
		assertEquals(0, engine.eval("0"))
		val bindings = engine.createBindings()
		engine.eval("(-> x 10)", bindings)
		assertEquals(10, engine.eval("x", bindings))
		engine.eval("(-> x 10)")
		assertEquals(bindings.keys.toList(), bindings.entries.map { it.key })
		assertEquals(bindings.values.toList(), bindings.entries.map { it.value })
	}
}