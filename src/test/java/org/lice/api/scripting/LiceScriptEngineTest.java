package org.lice.api.scripting;

import org.junit.Test;

import javax.script.*;

import static org.junit.Assert.*;

/**
 * Created by Glavo on 17-8-11.
 *
 * @author Glavo
 * *
 * @since 0.1.0
 */
@SuppressWarnings("AssertEqualsBetweenInconvertibleTypes")
public class LiceScriptEngineTest {
	@Test
	public void testAPI() throws Exception {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("lice");

		assertEquals(engine.eval("0"), 0);

		engine.eval("(def x 10)");

		assertEquals(engine.eval("x"), 10);
	}
}