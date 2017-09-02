/**
 * Created by ice1000 on 2017/2/27.
 *
 * @author ice1000
 * @since 1.0.0
 */
@file:JvmMultifileClass
@file:JvmName("Lang")

package org.lice.lang


data class Pair<out A, out B>(
		val first: A,
		val second: B) {
	override fun toString(): String {
		return "[$first $second]"
	}
}

class DefineResult(private val res: String) {
	override fun toString() = res
}
