package org.lice.api.scripting

import java.lang.UnsupportedOperationException
import java.util.*
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
		return LiceScriptEngine()
	}

	override fun getOutputStatement(toDisplay: String?): String =
			"(print $toDisplay)"


	override fun getExtensions(): List<String> =
			listOf("lice")

	override fun getMimeTypes(): List<String> =
			listOf("application/lice", "text/lice")

	override fun getLanguageName(): String = "Lice"

	override fun getParameter(key: String?): String? = when (key) {
		"javax.script.engine_version", "javax.script.language_version" -> org.lice.VERSION
		"javax.script.engine", "javax.script.language", "javax.script.name" -> "lice"
		else -> null
	}

	override fun getMethodCallSyntax(obj: String?, m: String?, vararg args: String?): String =
			throw UnsupportedOperationException("Can't invoke method!")

	override fun getNames(): MutableList<String> = mutableListOf("lice", "Lice", "LiceScript")

	override fun getProgram(vararg statements: String?): String =
			statements.map { "($it)" }.joinToString()

	override fun getEngineName(): String = "Lice"
}