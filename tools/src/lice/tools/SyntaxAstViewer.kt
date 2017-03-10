/**
 * Created by ice1000 on 2017/3/6.
 *
 * @author ice1000
 */
package lice.tools

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel
import lice.Lice
import lice.compiler.model.StringLeafNode
import lice.compiler.model.StringMiddleNode
import lice.compiler.model.StringNode
import lice.compiler.parse.buildNode
import lice.repl.VERSION_CODE
import java.awt.BorderLayout
import java.io.File
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.WindowConstants.EXIT_ON_CLOSE
import javax.swing.filechooser.FileFilter
import javax.swing.tree.DefaultMutableTreeNode

typealias UINode = DefaultMutableTreeNode

/**
 * map the ast
 */
private fun mapAst2Display(
		node: StringNode,
		viewRoot: UINode
): UINode = when (node) {
	is StringLeafNode -> UINode(node)
	is StringMiddleNode -> viewRoot.apply {
		node.list
				.subList(1, node.list.size)
				.forEach { add(mapAst2Display(it, UINode(it))) }
	}
	else -> UINode("null")
}

/**
 * map the ast
 */
private fun mapDisplay2Ast(
		node: UINode,
		gen: StringBuilder,
		numOfIndents: Int = 0) {
	if (numOfIndents == 0) gen.append("\n")
	when {
		node.isLeaf -> gen
				.append(" ")
				.append(node.userObject.toString())
				.append("")
		else -> {
			gen
					.append("\n")
					.append("  ".repeat(numOfIndents))
					.append("(")
					.append(node.userObject.toString())
			node.children()
					.toList()
//					.map { it as UINode }
					.forEach {
						mapDisplay2Ast(
								it as UINode,
								gen,
								numOfIndents + 1
						)
					}
			gen.append(")")
		}
	}
}

private fun createTreeRootFromFile(file: File): UINode {
	val ast = buildNode(file.readText())
	return mapAst2Display(ast, UINode(ast))
}

/**
 * @author ice1000
 */
fun main(args: Array<String>) {
	UIManager.setLookAndFeel(WindowsLookAndFeel())
//	tree.isEditable = true
	val frame = JFrame("Lice language Syntax Tree Viewer $VERSION_CODE")
	frame.iconImage = ImageIO.read(Lice::class.java.getResource("icon.jpg"))
	frame.layout = BorderLayout()
	frame.defaultCloseOperation = EXIT_ON_CLOSE
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
	var fileCount = 0
	fun File.neighbour() = "$parent/$name-edited-${fileCount++}.lice"
	if (f.selectedFile != null) {
		val root = createTreeRootFromFile(f.selectedFile)
		frame.add(
				JScrollPane(JTree(root).apply { isEditable = true }),
				BorderLayout.CENTER
		)
		frame.isVisible = true
		val button = JButton("Export Lice Code")
		button.addActionListener {
			val sb = StringBuilder()
			root.children()
					.toList()
					.forEach {
						mapDisplay2Ast(it as UINode, sb)
					}
			File(f.selectedFile.neighbour())
					.apply { if (!exists()) createNewFile() }
					.writeText(sb.toString())
		}
		frame.add(button, BorderLayout.SOUTH)
	} else {
		System.exit(0)
	}
//	frame.add(
//			JScrollPane(createTreeRootFromFile(File("sample/test9.lice"))),
//			BorderLayout.CENTER
//	)
//	val button = JButton("Open ...")
//	frame.add(button, BorderLayout.SOUTH)
//	button.addActionListener { _ ->
//		val f = JFileChooser()
//		f.showDialog(frame, "Parse")
//		f.selectedFile?.let {
//			frame.add(createTreeRootFromFile(f.selectedFile), BorderLayout.CENTER)
//		}
//	}
}