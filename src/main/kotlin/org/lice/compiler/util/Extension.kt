/**
 * Created by ice1000 on 2017/2/16.
 *
 * @author ice1000
 */
@file:JvmName("Utilities")
@file:JvmMultifileClass

package org.lice.compiler.util

var DEBUGGING = true

var VERBOSE = true

fun <T> T.println(): T {
	println(this)
	return this
}

fun <T> T.debugOutput(): T {
	if (DEBUGGING) println(this)
	return this
}

inline fun <T> T.debugApply(block: T.() -> Unit): T {
	if (DEBUGGING) this.block()
	return this
}

inline fun debug(block: () -> Unit) {
	if (DEBUGGING) block()
}

inline fun verbose(block: () -> Unit) {
	if (VERBOSE) block()
}

fun <T> T.verboseOutput(): T {
	if (VERBOSE) println(this)
	return this
}

inline fun <T> T.verboseApply(block: T.() -> Unit): T {
	if (VERBOSE) this.block()
	return this
}

inline fun forceRun(block: () -> Unit) {
	try {
		block()
	} catch (e: Throwable) {
	}
}

fun serr(str: String) = System.err.println(str)

fun sout(str: String) = println(str)

fun Int.squared() = this * this
