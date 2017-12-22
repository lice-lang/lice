package org.lice.core

import org.lice.model.MetaData
import org.lice.model.Node

typealias Func = (MetaData, List<Node>) -> Node
typealias ProvidedFuncWithMeta = (MetaData, List<Any?>) -> Any?
typealias ProvidedFunc = (List<Any?>) -> Any?
