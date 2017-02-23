package lice

import lice.compiler.parse.createAst
import lice.compiler.util.forceRun
import lice.compiler.util.println
import org.junit.Test
import java.io.Console
import java.io.File
import java.util.*

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

	@Test
	fun test2() {
		val clazz = Class.forName("java.io.File")
		val file = clazz
				.getConstructor(String::class.java)
				.newInstance("sample/test2.lice")
		createAst(file as File).root.eval()
	}

	@Test
	fun test3() {
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
		createAst(obj as File).root.eval()
	}

	@Test
	fun test4() {
		val file = File("out")
		file
				.javaClass
				.getMethod("toString")
				.invoke(file)
	}

	@Test
	fun test5() {
		val one: Int? = 1
		Integer::class
				.java
				.getMethod("equals", Object::class.java)
				.invoke(one, 1)
				.println()
	}

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			StdTest().test1()
		}
	}
}
