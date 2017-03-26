package lice.grepl

import lice.compiler.util.SymbolList
import java.util.*

/**
 * Created by glavo on 17-3-26.
 *
 * @author Glavo
 * @version 1.0.0
 */

@JvmOverloads
inline fun String.stripMargin(marginChar: Char = '|'): String {
    val buf = StringBuilder()
    val lines = this.lines()
    for (i in lines.indices) {
        val line = lines[i]
        val trim = line.trim()
        if (trim.startsWith(marginChar)) buf.append(trim.substring(1))
                .append(if (i == lines.size - 1) "" else "\n")
        else buf.append(line)
                .append(if (i == lines.size - 1) "" else "\n")
    }
    return buf.toString()
}

inline fun SymbolList.getSymbolList(): MutableList<String> {
    val list: MutableList<String> = ArrayList()
    list.addAll(this.functions.keys)
    list.addAll(this.variables.keys)
    return list
}

