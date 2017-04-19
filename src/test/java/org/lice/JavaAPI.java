package org.lice;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import org.lice.compiler.model.MetaData;
import org.lice.compiler.model.Node;
import org.lice.compiler.model.ValueNode;
import org.lice.core.SymbolList;
import org.lice.compiler.util.Utilities;

import java.io.File;
import java.util.List;

import static org.lice.compiler.parse.Parse.createRootNode;

/**
 * Created by ice1000 on 2017/2/26.
 *
 * @author ice1000
 */
public class JavaAPI {
	public static void main(String[] args) {
		final SymbolList sl = new SymbolList();
		final int[] a = {0};
		sl.defineFunction("java-api-invoking", new Function2<MetaData, List<? extends Node>, Node>() {
			@Override
			public Node invoke(MetaData ln, List<? extends Node> ls) {
				return new ValueNode(a[0]++, ln);
			}
		});
		Utilities.forceRun(new Function0<Unit>() {
			@Override
			public Unit invoke() {
				createRootNode(new File("sample/test10.lice"), sl).eval();
				return Unit.INSTANCE;
			}
		});
	}
}
