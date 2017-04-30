package org.lice

import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Created by ice1000 on 2017/5/1.
 *
 * @author ice1000
 */

infix fun String.shouldBe(any: Any) = assertEquals(any, Lice.run(this))

fun String.shouldBeTrue() = assertTrue(true == Lice.run(this))
fun String.shouldBeFalse() = assertTrue(true != Lice.run(this))
fun String.shouldBeNull() = assertNull(Lice.run(this))
