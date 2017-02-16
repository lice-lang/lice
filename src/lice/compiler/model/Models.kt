/**
 * Created by ice1000 on 2017/2/16.
 *
 * @author ice1000
 */
package lice.compiler.model

interface StringNode

class StringMiddleNode(val list: List<StringNode>) : StringNode

class StringLeafNode(val str: String) : StringNode

object EmptyStringNode : StringNode
