/**
 * Created by ice1000 on 2017/2/25.
 *
 * @author ice1000
 * @since 1.0.0
 */
@file:Suppress("NOTHING_TO_INLINE")
@file:JvmName("Standard")
@file:JvmMultifileClass

package org.lice.core

import org.lice.compiler.model.MetaData
import org.lice.compiler.util.InterpretException.Factory.typeMisMatch
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
		{ meta: MetaData, ls: List<Any?> ->
			val image = ls[0]
			if (image !is BufferedImage) typeMisMatch("BufferedImage", image, meta)
			val x1 = ls[1]
			val y1 = ls[2]
			if (x1 !is Number) typeMisMatch("Number", x1, meta)
			if (y1 !is Number) typeMisMatch("Number", y1, meta)
			val x2 = ls[3]
			val y2 = ls[4]
			if (x2 !is Number) typeMisMatch("Number", x2, meta)
			if (y2 !is Number) typeMisMatch("Number", y2, meta)
			draw(image.graphics as Graphics2D, x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt())
			image
		}

inline fun SymbolList.addGUIFunctions() {
	provideFunction("frame", {
		JFrame().apply {
			if (it.isNotEmpty()) title = it.first().toString()
			defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
			isVisible = true
		}
	})
	provideFunctionWithMeta("image", { meta, ls ->
		val o = ls.first()
		when (o) {
			is File -> ImageIO.read(o)
			is URL -> ImageIO.read(o)
			is Number -> {
				val q = if (ls.size >= 2) ls[1] else o
				if (q !is Number) typeMisMatch("Number", q, meta)
				BufferedImage(o.toInt(), q.toInt(), BufferedImage.TYPE_INT_ARGB)
			}
			else -> typeMisMatch("File or URL", o, meta)
		}
	})
	provideFunction("show-image", {
		val o = it.first()
		when (o) {
			is BufferedImage -> JFrame().apply {
				setSize(o.width + 8, o.height + 8)
				title = "width: ${o.width}, height: ${o.height}"
				layout = BorderLayout()
				add(object : JPanel() {
					override fun paintComponent(g: Graphics?) {
						@Suppress("PROTECTED_CALL_FROM_PUBLIC_INLINE")
						super.paintComponent(g)
						g?.drawImage(o, 0, 0, this)
					}
				}, BorderLayout.CENTER)
				defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
				isVisible = true
			}
			else -> null
		}
	})

	provideFunctionWithMeta("draw-line", { meta, ls ->
		make2DDrawer { x1, y1, x2, y2 ->
			color = Color.BLUE
			drawLine(x1, y1, x2, y2)
		}(meta, ls)
	})
	provideFunctionWithMeta("draw-point", { meta, ls ->
		val image = ls.first()
		if (image !is BufferedImage) typeMisMatch("BufferedImage", image, meta)
		val x1 = ls[1]
		val y1 = ls[2]
		if (x1 !is Number) typeMisMatch("Number", x1, meta)
		if (y1 !is Number) typeMisMatch("Number", y1, meta)
		image.graphics.run {
			color = Color.BLUE
			drawRect(x1.toInt(), y1.toInt(), 1, 1)
		}
		image
	})
	provideFunctionWithMeta("draw-rect", { meta, ls ->
		make2DDrawer { x1, y1, x2, y2 ->
			color = Color.BLUE
			drawRect(x1, y1, x2, y2)
		}(meta, ls)
	})
	provideFunctionWithMeta("draw-oval", { meta, ls ->
		make2DDrawer { x1, y1, x2, y2 ->
			color = Color.BLUE
			drawOval(x1, y1, x2, y2)
		}(meta, ls)
	})
	provideFunctionWithMeta("draw-filled-rect", { meta, ls ->
		make2DDrawer { x1, y1, x2, y2 ->
			color = Color.BLUE
			fillRect(x1, y1, x2, y2)
		}(meta, ls)
	})
	provideFunctionWithMeta("draw-filled-oval", { meta, ls ->
		make2DDrawer { x1, y1, x2, y2 ->
			color = Color.BLUE
			fillOval(x1, y1, x2, y2)
		}(meta, ls)
	})
}

