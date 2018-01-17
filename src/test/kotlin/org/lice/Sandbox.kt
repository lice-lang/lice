package org.lice

import org.junit.Test

/**
 * Created by ice1000 on 2017/4/23.
 *
 * @author ice1000
 */

class Sandbox {
	@Test
	fun sandbox() {
		//language=Lice
		Lice.run("""
(extern "org.junit.Assert" "fail")
(force|> (fail)) ; assertion fail
""")
	}
}