package org.lice.module;


import javax.script.Bindings;

/**
 * Created by Glavo on 17-9-22.
 *
 * @author Glavo
 * @since 4.0.0
 */
public interface Module {
	String getModuleName();

	void usingModule(Bindings bindings);
}
