/**
 * Created by ice1000 on 2017/3/6.
 *
 * @author ice1000
 */

package lice.tools

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel
import lice.Lice
import lice.compiler.model.*
import lice.compiler.parse.buildNode
import lice.compiler.parse.mapAst
import lice.repl.VERSION_CODE
import java.awt.BorderLayout
import java.io.File
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.filechooser.FileFilter
import javax.swing.tree.DefaultMutableTreeNode

/**
 * map the ast
 */
private fun rec(
		node: Node,
		viewRoot: DefaultMutableTreeNode
): DefaultMutableTreeNode {
	return when (node) {
		is ValueNode, is SymbolNode, is LambdaNode -> DefaultMutableTreeNode(node)
		is ExpressionNode -> viewRoot.apply {
			node.params.forEachIndexed { index, subNode ->
				insert(
						rec(subNode, DefaultMutableTreeNode(subNode)),
						index
				)
			}
		}
		else -> DefaultMutableTreeNode("unknown node")
	}
}

private fun createTreeFromFile(file: File): JTree {
	val ast = mapAst(buildNode(file.readText()))
	return JTree(rec(ast, DefaultMutableTreeNode(ast)))
}

/**
 * @author ice1000
 */
fun main(args: Array<String>) {
	UIManager.setLookAndFeel(WindowsLookAndFeel())
//	tree.isEditable = true
	val frame = JFrame("Lice language Semantic AST Viewer $VERSION_CODE")
	frame.iconImage = ImageIO.read(Lice::class.java.getResource("icon.jpg"))
	frame.layout = BorderLayout()
	frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
	frame.setLocation(80, 80)
	frame.setSize(480, 480)
	val f = JFileChooser()
	f.fileFilter = object : FileFilter() {
		override fun accept(f: File?): Boolean {
			if (f != null)
				return f.name.endsWith(".lice") or f.isDirectory
			return false
		}

		override fun getDescription() = "(.lice) lice source code"
	}
	f.showDialog(frame, "Parse")
	if (f.selectedFile != null) {
		frame.add(
				JScrollPane(createTreeFromFile(f.selectedFile)),
				BorderLayout.CENTER
		)
		frame.isVisible = true
	} else {
		System.exit(0)
	}
	print("")
//	frame.add(
//			JScrollPane(createTreeFromFile(File("sample/test9.lice"))),
//			BorderLayout.CENTER
//	)
//	val button = JButton("Open ...")
//	frame.add(button, BorderLayout.SOUTH)
//	button.addActionListener { _ ->
//		val f = JFileChooser()
//		f.showDialog(frame, "Parse")
//		f.selectedFile?.let {
//			frame.add(createTreeFromFile(f.selectedFile), BorderLayout.CENTER)
//		}
//	}
}
