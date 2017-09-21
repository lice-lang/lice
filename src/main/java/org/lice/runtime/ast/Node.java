package org.lice.runtime.ast;

import org.lice.runtime.MetaData;

import javax.script.Bindings;

/**
 * Created by Glavo on 17-9-21.
 *
 * @author Glavo
 * @since 4.0.0
 */
public abstract class Node {
	public MetaData metaData = null;

	public abstract Node eval(Bindings bindings);
}
