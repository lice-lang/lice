/**
 * Created by ice1000 on 2017/2/25.
 *
 * @author ice1000
 */
@file:Suppress("NOTHING_TO_INLINE")
@file:JvmName("Standard")
@file:JvmMultifileClass

package lice.core

import lice.compiler.model.EmptyNode
import lice.compiler.model.ValueNode
import lice.compiler.util.InterpretException.Factory.typeMisMatch
import lice.compiler.util.SymbolList
import java.awt.BorderLayout
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

inline fun SymbolList.addGUIFunctions() {
	addFunction("frame", { ls ->
		ValueNode(JFrame().apply {
			if (ls.isNotEmpty()) title = ls[0].eval().o.toString()
			defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
			isVisible = true
		})
	})
	addFunction("image", { ls ->
		val o = ls[0].eval()
		when (o.o) {
			is File -> ValueNode(ImageIO.read(o.o))
			is URL -> ValueNode(ImageIO.read(o.o))
			is Int -> {
				val q = if (ls.size >= 2) ls[1].eval() else o
				ValueNode(BufferedImage(o.o, q.o as Int, BufferedImage.TYPE_INT_ARGB))
			}
			else -> typeMisMatch("File or URL", o)
		}
	})
	addFunction("show-image", { ls ->
		val o = ls[0].eval()
		when (o.o) {
			is BufferedImage -> {
				ValueNode(JFrame().apply {
					setSize(o.o.width + 8, o.o.height + 8)
					title = "width: ${o.o.width}, height: ${o.o.height}"
					layout = BorderLayout()
					add(object : JPanel() {
						override fun paintComponent(g: Graphics?) {
							@Suppress("PROTECTED_CALL_FROM_PUBLIC_INLINE")
							super.paintComponent(g)
							g?.drawImage(o.o, 0, 0, this)
						}
					}, BorderLayout.CENTER)
					defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
//					isResizable = false
					isVisible = true
				})
			}
			else -> EmptyNode
		}
	})
}

