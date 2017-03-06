package lice.android

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import lice.repl.Repl
import lice.repl.Repl.Companion.HINT
import lice.repl.VERSION_CODE
import java.io.OutputStream
import java.io.PrintStream

class MainActivity : Activity() {

	override fun onCreate(savedInstanceState: Bundle) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		val out = PrintStream(object : OutputStream() {
			override fun write(b: Int) =
					code_output.append(b.toChar().toString())

			override fun write(b: ByteArray) =
					code_output.append(String(b))
		})
		System.setOut(out)
		System.setErr(out)
		code_output.append("This is the Android repl $VERSION_CODE for Lice language.\n")
		val repl = Repl()
		clear_screen.setOnClickListener {
			code_output.text = HINT
		}
		submit.setOnClickListener {
			val txt = code_input.text.toString()
			code_input.text.clear()
			code_output.append(txt)
			repl.handle(txt)
		}
	}
}
