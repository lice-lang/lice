package org.lice

import org.junit.BeforeClass
import org.junit.Test
import org.lice.lang.Echoer
import org.lice.lang.Echoer.echo
import java.io.File

/**
 * Created by ice1000 on 2017/2/20.
 *
 * @author ice1000
 */

class StdTest {
	@Test(timeout = 1000)
	fun test2() {
		val file = File("sample/test2.lice")
		Lice.run(file)
	}

	@Test
	fun echo() {
		Echoer.closeOutput()
		Echoer.openOutput()
		Echoer.repl = true
		Echoer.echo("ass")
		echo("${"ass"}\n")
		Echoer.repl = false
		Echoer.openOutput()
	}

	@Test(timeout = 1000)
	fun test3() {
		/// @todo I don't know what ICE1000 wants.
		/*
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
		val objekt = obj as File
		Lice.run(objekt)
		*/
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
		val ls = listOf(1, 2, 3, 4, 5, 6, 7)
		for (i in (0..ls.size - 2) step 2) {
			println("${ls[i]}, ${ls[i + 1]}")
		}
		if (ls.size % 2 == 0) println("Ah♂fuck you")
		else println(ls.last())
	}

	@Test(timeout = 1000)
	fun test7() {
		"""
(extern "java.util.Objects" "equals")
(equals 1 1)
""" evalTo true
		"""
(extern "java.util.Objects" "equals")
(equals 1 2)
""" evalTo false
	}

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			StdTest().test2()
		}

		@BeforeClass
		@JvmStatic
		fun initFiles() {
			Echoer.openOutput()
			File("sample").mkdirs()
			File("sample/test2.lice").run {
				if (!exists()) createNewFile()
				writeText("""
123
""")
				File("sample/test3.lice").run {
					if (!exists()) createNewFile()
					writeText("""
(print (+ 1 1) "\n")
(print (- 10N 1.0) "\n")
(print (/ 10.2M 5) "\n")
(print (/ 10 5.0) "\n")
(print (* 10 5.2M) "\n")

(alias + plus)
(undef +)
(alias plus +)
(print (+ 1 2) "\n")

(type 23)

(-> ass 10)

(str->sym "ass")

(print (format "ass %s can", "we") "
")
""")
				}
			}
		}
	}
}
