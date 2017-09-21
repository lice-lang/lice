package org.lice.runtime.ast;

import org.lice.runtime.MetaData;

import javax.script.*;

/**
 * Created by Glavo on 17-9-21.
 *
 * @author Glavo
 * @since 4.0.0
 */
public abstract class Node extends CompiledScript {
	private ScriptEngine engine;
	public MetaData metaData = null;

	@Override
	public Object eval(ScriptContext bindings) throws ScriptException {
		return this.eval(bindings.getBindings(0));
	}

	@Override
	public abstract Object eval(Bindings bindings) throws ScriptException;

	@Override
	public ScriptEngine getEngine() {
		return engine;
	}
}
