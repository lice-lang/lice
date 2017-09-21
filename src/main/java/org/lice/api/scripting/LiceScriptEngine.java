package org.lice.api.scripting;

import javax.script.*;
import java.io.Reader;

/**
 * Created by Glavo on 17-9-22.
 *
 * @author Glavo
 * @since 4.0.0
 */
public class LiceScriptEngine implements ScriptEngine {
	@Override
	public Object eval(String script, ScriptContext context) throws ScriptException {
		return null;
	}

	@Override
	public Object eval(Reader reader, ScriptContext context) throws ScriptException {
		return null;
	}

	@Override
	public Object eval(String script) throws ScriptException {
		return null;
	}

	@Override
	public Object eval(Reader reader) throws ScriptException {
		return null;
	}

	@Override
	public Object eval(String script, Bindings n) throws ScriptException {
		return null;
	}

	@Override
	public Object eval(Reader reader, Bindings n) throws ScriptException {
		return null;
	}

	@Override
	public void put(String key, Object value) {

	}

	@Override
	public Object get(String key) {
		return null;
	}

	@Override
	public Bindings getBindings(int scope) {
		return null;
	}

	@Override
	public void setBindings(Bindings bindings, int scope) {

	}

	@Override
	public Bindings createBindings() {
		return null;
	}

	@Override
	public ScriptContext getContext() {
		return null;
	}

	@Override
	public void setContext(ScriptContext context) {

	}

	@Override
	public ScriptEngineFactory getFactory() {
		return null;
	}
}
