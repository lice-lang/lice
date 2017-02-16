/**
 * Created by ice1000 on 2017/2/16.
 *
 * @author ice1000
 */
package lice.compiler.util

val DEBUGGING = true

fun <T> T.println(): T {
	println(this)
	return this
}

fun <T> T.debugOutput(): T {
	if (DEBUGGING) println(this)
	return this
}

fun <T> T.debugApply(block: T.() -> Unit): T {
	if (DEBUGGING) this.block()
	return this
}


