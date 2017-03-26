package lice.grepl

import jline.console.ConsoleReader
import jline.console.completer.StringsCompleter
import lice.compiler.util.SymbolList
import lice.compiler.util.println
import lice.compiler.util.serr
import lice.grepl.stripMargin
import lice.repl.Main
import lice.repl.Repl
import java.io.File
import java.util.*

/**
 * Created by glavo on 17-3-26.
 *
 * @author Glavo
 * @version 1.0.0
 */

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val console: ConsoleReader = ConsoleReader()
        val grepl = GRepl()

        GRepl.message.println()

        if (args.isEmpty()) {
            val sl = SymbolList(true)

            while (true) {
                console.addCompleter { s, i, list ->
                    if (s.isNotEmpty()) {
                        val arr = s.split(' ', '(', ')', ',')
                        if (arr.isNotEmpty()) {
                            list.addAll(sl.getSymbolList().filter {
                                it.startsWith(arr.last())
                            })

                            i - arr.last().length
                        } else i
                    } else i
                }
                grepl.handle(console.readLine("Lice >"), sl)
            }

        } else {
            Main.interpret(File(args[0]).apply {
                if (!exists()) serr("file not found: ${args[0]}")
            })
        }
    }
}