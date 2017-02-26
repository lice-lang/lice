
## Language design

+ Strict syntax
+ Lisp-style comments

```lisp
(import "lice.io")

(233) ; Error: 233 isn't a function

(print 233) ; OK: 233 is a java.lang.Integer
```

+ Basic functions

```lisp
(import "lice.io")

(+ 2 3 4 (* 2 5)) ; result: 19

(sqrt 100) ; result: 10.0

; also '||'
(&& (> 3 2 1) (>= 3 3 1)) ; result: true

(print 1) ; 1
(type 1) ; java.lang.Integer

(format "%d %d" 233 233)
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
"deep" ; "deep"
```

+ File/URL APIs

```lisp
(import "lice.io")

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
(import "lice.io")

(print (if (>= 1 2)
    (read-file (file "out")) ; will not be read
    (read-file (file "in"))
))
```

+ A global variable map to store values

```lisp
(-> "var" "value") ; put "value" into "var"

(<- "var") ; returns "value"

(<-> "var" "darkholm")
; if (<- "var") is null,
; (-> "var" "darkholm"),
; then return "darkholm".
; if not, return (<- "var").
```

+ List processing

```lisp
(for-each "i" (.. 1 10) (print (<- "i")))
; prints: from 1 to 10
```

+ Loop

```lisp
(import "lice.io")

(while (> 10 (<-> "i" 0))
       (|> (print (<- "i"))
           (-> "i" (+ 1 (<- "i")))
       )
)
; |> means to evaluate every parameters given
; this prints from 0 to 9
```

+ Invoking Java

```java
// java
class Main {
	public static void main(String[] args){
		SymbolList sl = new SymbolList();
		sl.addFunction(
				"java-api-invoking",
				ls -> new ValueNode(100)
		);
		createAst(new File("sample/test10.lice"), sl)
				.getRoot().eval();
	}
}
```

```lisp
; Lice
(import "lice.io")

(print (java-api-invoking))
```

## Repl

The repl has two versions, a GUI one based on swing, and a CUI one.

Here are some examples.

```lisp
Lice > (import "lice.io")
true => java.lang.Boolean

Lice > (import "lice.gui")
true => java.lang.Boolean

Lice > (import "lice.gui")
false => java.lang.Boolean

Lice > (+ 1 1)
2 => java.lang.Integer

Lice> ()
null => java.lang.Object

Lice > (eval "(+ 1 1)")
2 => java.lang.Integer
2 => java.lang.Integer

Lice > (eval (str-con "(+ " "1 " "1)"))
2 => java.lang.Integer
2 => java.lang.Integer

Lice > ([] 1 11 1)
[1, 11, 1] => java.util.ArrayList

Lice > (eval (read-file (file "sample/test3.lice")))
16769025 => java.lang.Integer
My name is Van, I'm an artist => java.lang.String
My name is Van, I'm an artist => java.lang.String

Lice > (if (> 2 1) 1 2)
1 => java.lang.Integer

Lice > (-> "file" (file "fuck_you"))
fuck_you => java.io.File

Lice > (write-file (<- "file") (str-con "deep" " dark fantasy"))
deep dark fantasy => java.lang.String

Lice > (read-file (<- "file"))
deep dark fantasy => java.lang.String

Lice > ([|] 1 2 3 4 5)
[1 [2 [3 [4 [5 null]]]]] => lice.core.Pair

Lice > (-> "i" ([|] 1 2 3 4 5 6 7))
[1 [2 [3 [4 [5 [6 [7 null]]]]]]] => lice.core.Pair

Lice > (tail (tail (<- "i")))
[3 [4 [5 [6 [7 null]]]]] => lice.core.Pair
```
