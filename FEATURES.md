
## Language design

+ Strict syntax
+ Lisp-style comments

```lisp
(require lice.io)

(233) ; Error: 233 isn't a function

233 ; OK: 233 is a value

(print 233) ; OK: 233 is a java.lang.Integer
```

+ Variables

```lisp
Lice > 12903
12903 => java.lang.Integer

Lice > (-> fuck 9320)
9320 => java.lang.Integer

Lice > fuck
9320 => java.lang.Integer
```

+ Basic functions

```lisp
(+ 2 3 4 (* 2 5)) ; result: 19

(sqrt 100) ; result: 10.0

; also '||'
(&& (> 3 2 1) (>= 3 3 1)) ; result: true

(print 1) ; 1
(type 1) ; java.lang.Integer

(format "%d %d" 233 233)
```

+ Compare

```lisp
(== 1 1) ; number comparison,
         ; is actually "1 == 1"
(== 1 1N) ; true
(== 1 (int->double 1)) ; true
(=== a b) ; is actually a.equals(b)
(=== 1 1N) ; false

; also != !==
```

+ Literals

```lisp
() ; null
null ; null
true ; true
false ; false
10 ; 10
010 ; 8
0x10 : 16
0b10 ; 2
233N ; BigInteger("233")
"deep" ; "deep"
```

+ Primitives

```lisp
Lice > (- 1N 10)
-9 => java.math.BigInteger

Lice > (- 10N 1)
9 => java.math.BigInteger

Lice > (+ 1 1)
2 => java.lang.Integer

Lice > (+ 9999999999999999999999999999999999999N 9999999999)
10000000000000000000000000009999999998 => java.math.BigInteger

Lice > (- 9999999999999999999999999999999999999N 9999999999)
9999999999999999999999999990000000000 => java.math.BigInteger

(+ 1 1)
2 => java.lang.Integer

Lice > (+ 1 1.0)
2.0 => java.lang.Float

Lice > (+ 1 1.1287391873917392379372193792137198237189237291)
2.128739187391739 => java.lang.Double
```

+ File/URL APIs

```lisp
(if (! (file-exists? "save"))
    (run (write-file (file "save") "0")
         (print "fuck"))
    (print "shit")
)

(print (read-url (url "http://ice1000.tech")))

(print (read-file (file "src/lice/compiler/util/SymbolList.kt")))

(write-file (file "output")
            (str-con "deep " "dark" "fantasy"))
```

+ Casts

```lisp
(str->int "12345678") ; dicimal
(str->int "0xFFF") ; hex
(str->int "02333") ; octal
(str->int "0b10100101") ; binary

(int->hex 12345678) ; result: 0xbc614e
(int->oct 12345678) ; result: 057060516
(int->bin 12345678) ; result: 0b101111000110000101001110

(->str (file "deep")) ; result: "deep"
```

+ Lazy evaluation

```lisp
(print (if (>= 1 2)
    (read-file (file "out")) ; will not be read
    (read-file (file "in"))
))
```

+ A global variable map to store values

```lisp
(-> var "actual") ; put "actual" into "var"

(print var) ; prints "actual"

(<-> var "darkholm")
; if var is null,
; (-> var "darkholm"),
; then return "darkholm".
; if not, return var.
```

+ List processing

```lisp
(for-each i (.. 1 10) (print i))
; prints: from 1 to 10
```

+ Loop

```lisp
(while (> 10 (<-> i 0))
       (|> (print i)
           (-> i (+ 1 i))
       )
)
; |> means to evaluate every parameters given
; just like 'run' in clojure, 'begin' in scheme
; this prints from 0 to 9
```

+ Invoking Java

```java
// java
import lice.Lice;
import lice.compiler.model.ValueNode;
import lice.compiler.util.SymbolList;

import java.io.File;

class Main {
	public static void main(String[] args){
		SymbolList sl = new SymbolList();
		sl.defineFunction(
				"java-api-invoking",
				ls -> new ValueNode(100)
		);
		Lice.run(new File("sample/test10.lice"), sl);
	}
}
```

```lisp
; Lice

(print (java-api-invoking))
```

## Repl

The repl has two versions, a GUI one based on swing, and a CUI one.

Here are some examples.

```lisp
Lice > (+ 1 1)
2 => java.lang.Integer

Lice> ()
null => java.lang.Object

Lice > (eval "(+ 1 1)")
2 => java.lang.Integer
null => java.lang.Object

Lice > (eval (str-con "(+ " "1 " "1)"))
2 => java.lang.Integer
null => java.lang.Object

Lice > (cons 1 11 1)
[1, 11, 1] => java.util.ArrayList

Lice > (eval (read-file (file "sample/tests/test3.lice")))
16769025 => java.lang.Integer
My name is Van, I'm an artist => java.lang.String
My name is Van, I'm an artist => java.lang.String

Lice > (if (> 2 1) 1 2)
1 => java.lang.Integer

Lice > (-> file (file "fuck_you"))
fuck_you => java.io.File

Lice > (write-file file (str-con "deep" " dark fantasy"))
deep dark fantasy => java.lang.String

Lice > (read-file file)
deep dark fantasy => java.lang.String

Lice > ([|] 1 2 3 4 5)
[1 [2 [3 [4 [5 null]]]]] => lice.core.Pair

Lice > (-> i ([|] 1 2 3 4 5 6 7))
[1 [2 [3 [4 [5 [6 [7 null]]]]]]] => lice.core.Pair

Lice > (tail (tail i))
[3 [4 [5 [6 [7 null]]]]] => lice.core.Pair
```
