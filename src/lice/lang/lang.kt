/**
 * Created by ice1000 on 2017/2/27.
 *
 * @author ice1000
 */
@file:JvmMultifileClass
@file:JvmName("Lang")

package lice.lang


class Pair<out A, out B>(
		val first: A,
		val second: B) {
	override fun toString(): String {
		return "[$first $second]"
	}
}

class Symbol(val name: String) {
	override fun toString() = name

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other?.javaClass != javaClass) return false
		other as Symbol
		if (name != other.name) return false
		return true
	}

	override fun hashCode() =
			name.hashCode()
}


