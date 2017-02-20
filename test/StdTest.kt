import lice.compiler.util.forceRun
import lice.compiler.util.println
import org.junit.Test

/**
 * Created by ice1000 on 2017/2/20.
 *
 * @author ice1000
 */

class StdTest {
	@Test(timeout = 1000)
	fun test1() {
		forceRun {
			"0xffff"
					.toInt()
					.println()
		}
	}
}
