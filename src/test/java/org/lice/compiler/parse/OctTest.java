package org.lice.compiler.parse;

import org.junit.Test;

import static org.lice.compiler.parse.Parse.toOctInt;
import static org.junit.Assert.assertEquals;

/**
 * 我也是没办法才用 Jawa 的，我觉得这不怪我
 * 不要说了，我其实不知道到底该不该支持 8 进制
 * <p>
 * Created by ice1000 on 2017/2/24.
 *
 * @author ice1000
 */
public class OctTest {
	@SuppressWarnings("OctalInteger")
	@Test
	public void toOctTest() {
		assertEquals(01010, toOctInt("01010"));
		assertEquals(01267120, toOctInt("01267120"));
		assertEquals(01100010, toOctInt("01100010"));
	}
}
