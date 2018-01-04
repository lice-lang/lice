package org.lice;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.ObjIntConsumer;

public class LiceBenchmarkJava {
	private Map<String, Object> map = new HashMap<>();

	private interface TripleFunction<A, B, C, D> {
		D fun(A a, B b, C c);
	}

	private void java() {
		TripleFunction<String, Object, Consumer<Map<String, Object>>, Void> function = (name, value, mapConsumer) -> {
			map.put(name, value);
			mapConsumer.accept(map);
			map.remove(name);
			return null;
		};
		function.fun("reimu", 100, it -> Integer.valueOf(233 + ((Integer) it.get("reimu"))));
	}

	@Test
	public void main() {
		long l = System.currentTimeMillis();
		ObjIntConsumer<IntConsumer> loop = (f, count) -> {
			int i = 0;
			while (i < count) {
				f.accept(i);
				i = i + 1;
			}
		};
		loop.accept(i -> java(), Benchmark.cnt);
		System.out.println(System.currentTimeMillis() - l);
	}
}
