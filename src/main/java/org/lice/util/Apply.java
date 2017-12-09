package org.lice.util;

import org.lice.runtime.LiceFunction;
import org.lice.runtime.NameError;

/**
 * Created by Glavo on 17-9-22.
 *
 * @author Glavo
 * @since 4.0.0
 */
public final class Apply {
	private Apply() {
	}

	Object apply(Object fun, Object... args) {
		if (!(fun instanceof LiceFunction))
			throw new NameError(fun + " is not callable");


		return null;
	}
}
