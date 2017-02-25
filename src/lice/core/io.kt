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
import lice.compiler.parse.createAst
import lice.compiler.util.InterpretException
import lice.compiler.util.SymbolList
import java.io.File
import java.net.URL

inline fun SymbolList.addFileFunctions() {
	addFunction("file", { ls ->
		val a = ls[0].eval()
		when (a.o) {
			is String -> ValueNode(File(a.o)
					.apply { if (!exists()) createNewFile() })
			else -> InterpretException.typeMisMatch("String", a)
		}
	})
	addFunction("file-exists?", { ls ->
		val a = ls[0].eval()
		when (a.o) {
			is String -> ValueNode(File(a.o).exists())
			else -> InterpretException.typeMisMatch("String", a)
		}
	})
	addFunction("read-file", { ls ->
		val a = ls[0].eval()
		when (a.o) {
			is File -> ValueNode(a.o.readText())
			else -> InterpretException.typeMisMatch("File", a)
		}
	})
	addFunction("url", { ls ->
		val a = ls[0].eval()
		when (a.o) {
			is String -> ValueNode(URL(a.o))
			else -> InterpretException.typeMisMatch("String", a)
		}
	})
	addFunction("read-url", { ls ->
		val a = ls[0].eval()
		when (a.o) {
			is URL -> ValueNode(a.o.readText())
			else -> InterpretException.typeMisMatch("URL", a)
		}
	})
	addFunction("load-file", { ls ->
		val o = ls[0].eval()
		when (o.o) {
			is File -> ValueNode(createAst(o.o).root.eval())
			else -> InterpretException.typeMisMatch("File", o)
		}
	})
	addFunction("write-file", { ls ->
		val a = ls[0].eval()
		val b = ls[1].eval()
		when (a.o) {
			is File -> a.o.writeText(b.o.toString())
			else -> InterpretException.typeMisMatch("File", a)
		}
		ValueNode(b.o.toString())
	})
}

