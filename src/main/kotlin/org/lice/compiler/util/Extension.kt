/**
 * Created by ice1000 on 2017/2/16.
 *
 * @author ice1000
 * @since 1.0.0
 */
@file:JvmName("Utilities")
@file:JvmMultifileClass

package org.lice.compiler.util

import org.lice.lang.Echoer

var DEBUGGING = true

var VERBOSE = true

fun <T> T.println(): T {
	Echoer.echoln(this)
	return this
}

fun <T> T.debugOutput(): T {
	if (DEBUGGING) Echoer.echoln(this)
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
	if (VERBOSE) Echoer.echoln(this)
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
