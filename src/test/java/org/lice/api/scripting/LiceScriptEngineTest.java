package org.lice.api.scripting;

import org.junit.Test;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import static org.junit.Assert.assertEquals;

/**
 * Created by Glavo on 17-8-11.
 *
 * @author Glavo
 * *
 * @since 0.1.0
 */
@SuppressWarnings("AssertEqualsBetweenInconvertibleTypes")
public class LiceScriptEngineTest {
	@Test public void testAPI() throws Exception {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("lice");
		assertEquals(0, engine.eval("0"));
		Bindings bindings = engine.createBindings();
		engine.eval("(-> x 10)", bindings);
		assertEquals(10, engine.eval("x", bindings));
		engine.eval("(-> x 10)");
		bindings.entrySet().forEach(System.out::println);
	}
}