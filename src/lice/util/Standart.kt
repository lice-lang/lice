/**
 * Created by ice1000 on 2017/2/25.
 *
 * @author ice1000
 */
package lice.util



class Pair<out A, out B>(
		val first: A,
		val second: B) {
	override fun toString(): String {
		return "[$first $second]"
	}
}

