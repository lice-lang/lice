/**
 * Created by ice1000 on 2017/2/25.
 *
 * @author ice1000
 */
@file:Suppress("NOTHING_TO_INLINE")
@file:JvmName("Standard")
@file:JvmMultifileClass

package lice.core

import lice.compiler.model.ValueNode
import lice.compiler.util.InterpretException
import lice.compiler.util.SymbolList
import java.io.File
import java.net.URL

inline fun SymbolList.addFileFunctions() {
	defineFunction("file", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is String -> ValueNode(File(a.o)
					.apply { if (!exists()) createNewFile() }, ln)
			else -> InterpretException.typeMisMatch("String", a, ln)
		}
	})
	defineFunction("directory", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is String -> ValueNode(File(a.o)
					.apply { if (!exists()) mkdirs() }, ln)
			else -> InterpretException.typeMisMatch("String", a, ln)
		}
	})
	defineFunction("file-exists?", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is String -> ValueNode(File(a.o).exists(), ln)
			else -> InterpretException.typeMisMatch("String", a, ln)
		}
	})
	defineFunction("read-file", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is File -> ValueNode(a.o.readText(), ln)
			else -> InterpretException.typeMisMatch("File", a, ln)
		}
	})
	defineFunction("url", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is String -> ValueNode(URL(a.o), ln)
			else -> InterpretException.typeMisMatch("String", a, ln)
		}
	})
	defineFunction("read-url", { ln, ls ->
		val a = ls[0].eval()
		when (a.o) {
			is URL -> ValueNode(a.o.readText(), ln)
			else -> InterpretException.typeMisMatch("URL", a, ln)
		}
	})
	defineFunction("write-file", { ln, ls ->
		val a = ls[0].eval()
		val b = ls[1].eval()
		when (a.o) {
			is File -> a.o.writeText(b.o.toString())
			else -> InterpretException.typeMisMatch("File", a, ln)
		}
		ValueNode(b.o.toString(), ln)
	})
}

