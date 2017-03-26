/**
 * Created by ice1000 on 2017/2/25.
 *
 * @author ice1000
 */
@file:Suppress("NOTHING_TO_INLINE")
@file:JvmName("Standard")
@file:JvmMultifileClass

package org.lice.core

import org.lice.compiler.model.MetaData
import org.lice.compiler.model.Node
import org.lice.compiler.model.Node.Objects.getNullNode
import org.lice.compiler.model.ValueNode
import org.lice.compiler.util.InterpretException.Factory.typeMisMatch
import org.lice.compiler.util.SymbolList
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

inline fun make2DDrawer(
		crossinline draw: Graphics2D.(Int, Int, Int, Int) -> Unit) =
		{ meta: MetaData, ls: List<Node> ->
			val image = ls[0].eval()
			if (image.o !is BufferedImage)
				typeMisMatch("BufferedImage", image, meta)
			val x1 = ls[1].eval()
			val y1 = ls[2].eval()
			if (x1.o !is Number) typeMisMatch("Number", x1, meta)
			if (y1.o !is Number) typeMisMatch("Number", y1, meta)
			val x2 = ls[3].eval()
			val y2 = ls[4].eval()
			if (x2.o !is Number) typeMisMatch("Number", x2, meta)
			if (y2.o !is Number) typeMisMatch("Number", y2, meta)
			draw(
					image.o.graphics as Graphics2D,
					x1.o.toInt(),
					y1.o.toInt(),
					x2.o.toInt(),
					y2.o.toInt()
			)
			ValueNode(image.o)
		}

inline fun SymbolList.addGUIFunctions() {
	defineFunction("frame", { ln, ls ->
		ValueNode(JFrame().apply {
			if (ls.isNotEmpty()) title = ls[0].eval().o.toString()
			defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
			isVisible = true
		}, ln)
	})
	defineFunction("image", { ln, ls ->
		val o = ls[0].eval()
		when (o.o) {
			is File -> ValueNode(ImageIO.read(o.o), ln)
			is URL -> ValueNode(ImageIO.read(o.o), ln)
			is Number -> {
				val q = if (ls.size >= 2) ls[1].eval() else o
				if (q.o !is Number) typeMisMatch("Number", q, ln)
				ValueNode(BufferedImage(
						o.o.toInt(),
						q.o.toInt(),
						BufferedImage.TYPE_INT_ARGB
				), ln)
			}
			else -> typeMisMatch("File or URL", o, ln)
		}
	})
	defineFunction("show-image", { ln, ls ->
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
				}, ln)
			}
			else -> getNullNode(ln)
		}
	})

	defineFunction("draw-line", { meta, ls ->
		make2DDrawer { x1, y1, x2, y2 ->
			color = Color.BLUE
			drawLine(x1, y1, x2, y2)
		}(meta, ls)
	})
	defineFunction("draw-point", { meta, ls ->
		val image = ls[0].eval()
		if (image.o !is BufferedImage) typeMisMatch("BufferedImage", image, meta)
		val x1 = ls[1].eval()
		val y1 = ls[2].eval()
		if (x1.o !is Number) typeMisMatch("Number", x1, meta)
		if (y1.o !is Number) typeMisMatch("Number", y1, meta)
		image.o.graphics.run {
			color = Color.BLUE
			drawRect(x1.o.toInt(), y1.o.toInt(), 1, 1)
		}
		ValueNode(image.o)
	})
	defineFunction("draw-rect", { meta, ls ->
		make2DDrawer { x1, y1, x2, y2 ->
			color = Color.BLUE
			drawRect(x1, y1, x2, y2)
		}(meta, ls)
	})
	defineFunction("draw-oval", { meta, ls ->
		make2DDrawer { x1, y1, x2, y2 ->
			color = Color.BLUE
			drawOval(x1, y1, x2, y2)
		}(meta, ls)
	})
	defineFunction("draw-filled-rect", { meta, ls ->
		make2DDrawer { x1, y1, x2, y2 ->
			color = Color.BLUE
			fillRect(x1, y1, x2, y2)
		}(meta, ls)
	})
	defineFunction("draw-filled-oval", { meta, ls ->
		make2DDrawer { x1, y1, x2, y2 ->
			color = Color.BLUE
			fillOval(x1, y1, x2, y2)
		}(meta, ls)
	})
}

