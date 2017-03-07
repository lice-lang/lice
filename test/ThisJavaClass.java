import kotlin.jvm.Synchronized;
import kotlin.jvm.Transient;
import lice.Lice;
import lice.compiler.model.ValueNode;
import lice.compiler.util.SymbolList;

import java.io.File;

/**
 * @author ice1000
 */
class TheJavaClass {
	@Transient
	private transient int a = 1;
	@Synchronized
	public static void main(String[] args) {
		// 创建一个符号表，可以往里面添加方法。
		// 构造函数调用默认的就行了
		SymbolList sl = new SymbolList();
		// 定义一个方法
		sl.defineFunction(
				// 方法的名字
				"java-api-invoking",
				// 方法体，一个Lambda
				// 参数说明：
				// ln是这段代码在源码中的行号，类型是int，可以在报错的时候给出恰当的信息
				// ls是List<Node>，每个Node都是传入的参数，按顺序放好了的。
				// 有一个eval()方法，用于给这个参数进行求值。只要不是循环结构，一般只求值一次。
				// 然后再返回一个Node
				(ln, ls) -> {
					System.out.println("");
					return new ValueNode(100, ln);
				}
		);
		Lice.run(new File("test.lice"), sl);
	}
}

class TheJavaClass2 {
	public static void main(String[] args) {
		SymbolList sl = new SymbolList();
		final int[] a = new int[1];
		a[0] = 0;
		sl.defineFunction(
				"java-api-invoking",
				(ln, ls) -> {
					a[0]++;
					return new ValueNode(a[0], ln);
				}
		);
		Lice.run(new File("test.lice"), sl);
	}
}

class TheJavaClass3 {
	public static void main(String[] args) {
		SymbolList sl = new SymbolList();
		sl.defineFunction(
				"quote",
				(meta, ls) -> new ValueNode("\"", meta)
		);
		Lice.run(new File("test.lice"), sl);
	}
}
