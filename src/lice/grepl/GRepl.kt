package lice.grepl

import lice.compiler.model.Ast
import lice.compiler.parse.buildNode
import lice.compiler.parse.mapAst
import lice.compiler.util.SymbolList
import lice.compiler.util.println
import lice.compiler.util.serr
import lice.repl.Repl
import lice.repl.VERSION_CODE
import java.util.*

/**
 * Created by glavo on 17-3-26.
 *
 * @author Glavo
 * @version 1.0.0
 */

class GRepl {

    val stackTrace: Deque<Throwable> = LinkedList()

    @JvmOverloads
    fun handle(
            str: String,
            symbolList: SymbolList = SymbolList(true)) {
        when (str) {
            "exit" -> {
                "Good bye!".println()
                System.exit(0)
            }
            "pst" ->
                if (stackTrace.isNotEmpty()) stackTrace.peek().printStackTrace()
                else "No stack trace.".println()
            "help" -> """
                |This is the repl for lice language.

                |You have 4 special commands which you cannot use in the language but the repl:

                |exit: exit the repl
                |pst: print the most recent stack trace
                |help: print this doc
                |version: check the version""".stripMargin()
                    .println()
            "version" -> """
                |Lice language interpreter $VERSION_CODE
                |GRepl $Version
                |by ice1000""".stripMargin().println()
            else -> try {
                val ast = Ast(mapAst(
                        node = buildNode(str),
                        symbolList = symbolList)
                ).root
                ast.eval()
            } catch(e: Throwable) {
                stackTrace.push(e)
                serr(e.message ?: "")
            }
        }
    }

    companion object {

        val Version: String = "v1.0.0 beta"
        val message: String = """Glavo's Lice language repl $Version
            |see: https://github.com/ice1000/lice

            |for help please input: help
        """.stripMargin()

    }
}