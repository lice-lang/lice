package lice

import org.junit.Test
import org.lice.compiler.parse.createRootNode
import org.lice.compiler.util.forceRun
import org.lice.compiler.util.println
import java.io.File

/**
 * Created by ice1000 on 2017/2/20.
 *
 * @author ice1000
 */

class StdTest {
	@Test(timeout = 1000)
	fun test1() {
		forceRun {
			"0xffff"
					.toInt()
					.println()
		}
	}

	@Test(timeout = 1000)
	fun test2() {
		forceRun {
			val clazz = Class.forName("java.io.File")
			val file = clazz
					.getConstructor(String::class.java)
					.newInstance("sample/test2.lice")
			createRootNode(file as File).eval()
		}
	}

	@Test(timeout = 1000)
	fun test3() {
		forceRun {
			val ls = listOf("sample/test3.lice")
			var obj: Any? = null
			Class
					.forName("java.io.File")
					.constructors
					.forEach {
						if (obj == null) forceRun {
							obj = it.newInstance(*ls.toTypedArray())
						}
					}
			createRootNode(obj as File).eval()
		}
	}

	@Test(timeout = 1000)
	fun test4() {
		val file = File("out")
		file
				.javaClass
				.getMethod("toString")
				.invoke(file)
	}

	@Test(timeout = 1000)
	fun test5() {
		val one: Int? = 1
		Integer::class
				.java
				.getMethod("equals", Object::class.java)
				.invoke(one, 1)
				.println()
	}

	@Test(timeout = 1000)
	fun test6() {
//		for (i in 0..5 step 2) println(i)
//		for (i in 0..5) println(i)
		val ls = listOf(1, 2, 3, 4, 5, 6, 7)
		for (i in (0..ls.size - 2) step 2) {
			println("${ls[i]}, ${ls[i + 1]}")
		}
		if (ls.size % 2 == 0) println("Ah♂fuck you")
		else println(ls.last())
	}

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			StdTest().test1()
		}
	}
}
