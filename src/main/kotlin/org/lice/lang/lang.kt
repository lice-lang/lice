/**
 * Created by ice1000 on 2017/2/27.
 *
 * @author ice1000
 * @since 1.0.0
 */
@file:JvmMultifileClass
@file:JvmName("Lang")

package org.lice.lang


class Pair<out A, out B>(
		val first: A,
		val second: B) {
	override fun toString(): String {
		return "[$first $second]"
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other?.javaClass != javaClass) return false

		other as Pair<*, *>

		if (first != other.first) return false
		if (second != other.second) return false

		return true
	}

	override fun hashCode(): Int {
		var result = first?.hashCode() ?: 0
		result = 31 * result + (second?.hashCode() ?: 0)
		return result
	}
}

class DefineResult(val res: String) {
	override fun toString() = res
}

class NullptrType private constructor()
