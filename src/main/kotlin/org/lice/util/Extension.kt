/**
 * Created by ice1000 on 2017/2/16.
 *
 * @author ice1000
 * @since 1.0.0
 */
@file:JvmName("Utilities")
@file:JvmMultifileClass

package org.lice.util

inline fun forceRun(block: () -> Unit) {
	try {
		block()
	} catch (e: Throwable) {
	}
}

fun Any?.className(): String = if (null != this) this.javaClass.name else "NullType"

@Suppress("UNCHECKED_CAST")
inline fun <reified R> cast(any: Any?) =
		any as? R ?: throw InterpretException("$any is not ${R::class.java.name}")
