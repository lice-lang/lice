package org.lice

import org.junit.BeforeClass
import org.junit.Test
import org.lice.lang.Echoer
import org.lice.lang.Echoer.echo
import org.lice.repl.main
import org.lice.util.forceRun
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertFails

/**
 * Created by ice1000 on 2017/2/20.
 *
 * @author ice1000
 */

class StdTest {
	@Test(timeout = 1000)
	fun test2() {
		val file = Paths.get("sample/test2.lice")
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
		arrayOf(objekt.absolutePath).main()
	}

	@Test(timeout = 1000)
	fun test5() {
		val one: Int? = 1
		(Integer::class
				.java
				.getMethod("equals", Object::class.java))(one, 1)
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
		//language=Lice
		"""
(extern "java.util.Objects" "equals")
(equals 1 1)
""" evalTo true
		//language=Lice
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
			Files.createDirectories(Paths.get("sample"))
			Paths.get("sample/test2.lice").let {
				if (!Files.exists(it)) Files.createFile(it)
				Files.write(it, """
123
""".toByteArray())
				Paths.get("sample/test3.lice").let {
					if (!Files.exists(it)) Files.createFile(it)
					//language=Lice
					Files.write(it, """
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
(sym->str ass)

(println (format "ass %s can", "we"))
(join->str (list "ass %s can", "we"))

(| 1 2)
(& 1 2)
(~ 1 2)
(^ 1 (->int (rand)))

(println 233)

(.. (str->int "1") 5)
(.. (str->int "0x1") 5)
(.. (str->int "0b1") 5)
([| ([|] 1 2))
(|] ([|] 1 2))
(->chars "2333")
(print "Hello " "World" "\n")

; travel through a range
(for-each i (array->list (array 1 2 3 4 5)) (print i "\n"))
(for-each i (list->array (list 1 2 3 4 5)) (print i "\n"))
(for-each i (.. 1 5) (print i "\n"))

; define a call-by-name function
(defexpr fold ls init op
 (for-each index-var ls
   (-> init (op init index-var))))

; invoke the function defined above
(fold (.. 1 4) 0 +)

; passing a call-by-value lambda to a call-by-value lambda
((lambda op (op 3 4)) (lambda a b (+ (* a a) (* b b))))
""".toByteArray())
				}
			}
		}
	}
}
