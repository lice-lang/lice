package org.lice.api.scripting

import java.io.Reader
import javax.script.AbstractScriptEngine
import javax.script.Bindings
import javax.script.ScriptContext
import javax.script.ScriptEngineFactory

/**
 * Created by Glavo on 17-6-8.
 *
 * @author Glavo
 * @since 0.1.0
 */
class LiceScriptEngine(n: Bindings) : AbstractScriptEngine(n) {
    override fun eval(reader: Reader?, context: ScriptContext?): Any {
        TODO("")
    }

    override fun createBindings(): Bindings {
        TODO("not implemented")
    }

    override fun getFactory(): ScriptEngineFactory {
        TODO("not implemented")
    }

    override fun eval(script: String?, context: ScriptContext?): Any {
        TODO("not implemented")
    }
}