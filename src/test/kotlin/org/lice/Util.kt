package org.lice

import org.lice.lang.Echoer.echo
import kotlin.test.*

/**
 * Created by ice1000 on 2017/5/1.
 *
 * @author ice1000
 */

infix fun String.evalTo(any: Any?) = assertEquals(any, Lice.runBarely(this))

fun String.shouldBeTrue() = assertTrue(true == Lice.runBarely(this))
fun String.shouldBeFalse() = assertTrue(true != Lice.runBarely(this))
fun String.shouldBeNull() = assertNull(Lice.runBarely(this))

fun <T> T.println(): T {
	echo("${this}\n")
	return this
}
