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
	setFunction("file", { ls ->
		val a = ls[0].eval()
		when (a.o) {
			is String -> ValueNode(File(a.o)
					.apply { if (!exists()) createNewFile() })
			else -> InterpretException.typeMisMatch("String", a)
		}
	})
	setFunction("directory", { ls ->
		val a = ls[0].eval()
		when (a.o) {
			is String -> ValueNode(File(a.o)
					.apply { if (!exists()) mkdirs() })
			else -> InterpretException.typeMisMatch("String", a)
		}
	})
	setFunction("file-exists?", { ls ->
		val a = ls[0].eval()
		when (a.o) {
			is String -> ValueNode(File(a.o).exists())
			else -> InterpretException.typeMisMatch("String", a)
		}
	})
	setFunction("read-file", { ls ->
		val a = ls[0].eval()
		when (a.o) {
			is File -> ValueNode(a.o.readText())
			else -> InterpretException.typeMisMatch("File", a)
		}
	})
	setFunction("url", { ls ->
		val a = ls[0].eval()
		when (a.o) {
			is String -> ValueNode(URL(a.o))
			else -> InterpretException.typeMisMatch("String", a)
		}
	})
	setFunction("read-url", { ls ->
		val a = ls[0].eval()
		when (a.o) {
			is URL -> ValueNode(a.o.readText())
			else -> InterpretException.typeMisMatch("URL", a)
		}
	})
	setFunction("write-file", { ls ->
		val a = ls[0].eval()
		val b = ls[1].eval()
		when (a.o) {
			is File -> a.o.writeText(b.o.toString())
			else -> InterpretException.typeMisMatch("File", a)
		}
		ValueNode(b.o.toString())
	})
}

