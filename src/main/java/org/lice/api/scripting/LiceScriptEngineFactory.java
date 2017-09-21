package org.lice.api.scripting;

import org.lice.Lice;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import java.util.*;

/**
 * Created by Glavo on 17-9-21.
 *
 * @author Glavo
 * @since 4.0.0
 */
public final class LiceScriptEngineFactory implements ScriptEngineFactory {
	private static final List<String> names = immutableList("lice");
	private static final List<String> extensions = immutableList("lice");
	private static final List<String> mimeTypes = immutableList("application/lice", "text/lice");
	private static final Map<String, String> parameters = new HashMap<>();

	static {
		parameters.put("javax.script.engine_version", Lice.VERSION);
		parameters.put("javax.script.engine", "lice");
		parameters.put("javax.script.language_version", Lice.VERSION);
		parameters.put("javax.script.name", "lice");
	}


	@Override
	public String getEngineName() {
		return "lice";
	}

	@Override
	public String getEngineVersion() {
		return Lice.VERSION;
	}

	@Override
	public List<String> getExtensions() {
		return extensions;
	}

	@Override
	public List<String> getMimeTypes() {
		return mimeTypes;
	}

	@Override
	public List<String> getNames() {
		return names;
	}

	@Override
	public String getLanguageName() {
		return "lice";
	}

	@Override
	public String getLanguageVersion() {
		return Lice.VERSION;
	}

	@Override
	public Object getParameter(String key) {
		return parameters.get(key);
	}

	@Override
	public String getMethodCallSyntax(String obj, String m, String... args) {
		return null; //TODO
	}

	@Override
	public String getOutputStatement(String toDisplay) {
		return "(print " + toDisplay + ")";
	}

	@Override
	public String getProgram(String... statements) {
		return null; //TODO
	}

	@Override
	public ScriptEngine getScriptEngine() {
		return null; //TODO
	}

	@SafeVarargs
	private static <T> List<T> immutableList(T... elements) {
		return Collections.unmodifiableList(Arrays.asList(elements));
	}
}
