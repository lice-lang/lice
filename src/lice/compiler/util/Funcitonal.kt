/**
 * Created by ice1000 on 2017/2/19.
 *
 * @author ice1000
 */
package lice.compiler.util


interface Option<T>

object None : Option<Nothing>

open class Some<T> : Option<T>


interface Either<L, R>

open class Left<T> : Either<T, Nothing>

open class Right<T> : Either<Nothing, T>


