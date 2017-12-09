package org.lice.api.scripting;

import org.lice.runtime.Undefined;

import javax.script.Bindings;
import java.util.HashMap;

/**
 * Created by Glavo on 17-9-22.
 *
 * @author Glavo
 * @since 4.0.0
 */
public class LiceBindings extends HashMap<String, Object> implements Bindings {
	public static final LiceBindings global = new LiceBindings();

	public static void setParent(Bindings self, Bindings parent) {
		self.put("__parent__", parent);
	}

	public static Bindings getParent(Bindings self) {
		return (Bindings) self.getOrDefault("__parent__", null);
	}

	public static boolean hasParent(Bindings self) {
		return self.get("__parent__") != null;
	}

	public static Object getOrUndefined(Bindings self, String key) {
		Object ans;

		if ((ans = self.get(key)) != null) {
			return ans;
		} else if (hasParent(self)) {
			return getOrUndefined(getParent(self), key);
		} else
			return Undefined.undefined;
	}


}
