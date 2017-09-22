package org.lice.api.scripting;

import javax.script.Bindings;
import javax.script.ScriptContext;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Glavo on 17-9-22.
 *
 * @author Glavo
 * @since 4.0.0
 */
public final class LiceContext implements ScriptContext {
	private Map<Integer, Bindings> allBindings;

	public LiceContext() {
		this.allBindings = new HashMap<>();
	}


	@Override
	public void setBindings(Bindings bindings, int scope) {
		this.allBindings.put(scope, bindings);
	}

	@Override
	public Bindings getBindings(int scope) {
		return allBindings.get(scope);
	}

	@Override
	public void setAttribute(String name, Object value, int scope) {
		Bindings bindings = allBindings.get(scope);
		Objects.requireNonNull(bindings);
		bindings.put(name, value);
	}

	@Override
	public Object getAttribute(String name, int scope) {
		Bindings bindings = allBindings.get(scope);
		Objects.requireNonNull(bindings);
		return bindings.get(name);
	}

	@Override
	public Object removeAttribute(String name, int scope) {
		return null;
	}

	@Override
	public Object getAttribute(String name) {
		return null;
	}

	@Override
	public int getAttributesScope(String name) {
		return 0;
	}

	@Override
	public Writer getWriter() {
		return null;
	}

	@Override
	public Writer getErrorWriter() {
		return null;
	}

	@Override
	public void setWriter(Writer writer) {

	}

	@Override
	public void setErrorWriter(Writer writer) {

	}

	@Override
	public Reader getReader() {
		return null;
	}

	@Override
	public void setReader(Reader reader) {

	}

	@Override
	public List<Integer> getScopes() {
		return null;
	}
}
