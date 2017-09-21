package org.lice.api.scripting;

import javax.script.Bindings;
import javax.script.ScriptContext;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

/**
 * Created by Glavo on 17-9-22.
 *
 * @author Glavo
 * @since 4.0.0
 */
public final class LiceContext implements ScriptContext {
	@Override
	public void setBindings(Bindings bindings, int scope) {
		
	}

	@Override
	public Bindings getBindings(int scope) {
		return null;
	}

	@Override
	public void setAttribute(String name, Object value, int scope) {

	}

	@Override
	public Object getAttribute(String name, int scope) {
		return null;
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
