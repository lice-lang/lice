package org.lice.api.scripting

import javax.script.ScriptEngine
import javax.script.ScriptEngineFactory

/**
 * Created by Glavo on 17-6-8.
 *
 * @author Glavo
 * @since 3.1.1
 */
class LiceScriptEngineFactory : ScriptEngineFactory {
	override fun getLanguageVersion(): String = org.lice.VERSION

	override fun getEngineVersion(): String = org.lice.VERSION

	override fun getScriptEngine(): ScriptEngine {
		TODO("Function getScriptEngine is not implemented")
	}

	override fun getOutputStatement(toDisplay: String?): String {
		TODO("Function getOutputStatement is not implemented")
	}

	override fun getExtensions(): MutableList<String> {
		TODO("Function getExtensions is not implemented")
	}

	override fun getMimeTypes(): MutableList<String> {
		TODO("Function getMimeTypes is not implemented")
	}

	override fun getLanguageName(): String = "Lice"

	override fun getParameter(key: String?): Any {
		TODO("Function getParameter is not implemented")
	}

	override fun getMethodCallSyntax(obj: String?, m: String?, vararg args: String?): String {
		TODO("Function getMethodCallSyntax is not implemented")
	}

	override fun getNames(): MutableList<String> = mutableListOf("lice", "Lice")

	override fun getProgram(vararg statements: String?): String {
		TODO("Function getProgram is not implemented")
	}

	override fun getEngineName(): String = "Lice"
}