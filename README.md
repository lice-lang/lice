# lice

It's entirely working in progress....

This is an interpreter for a dialect of lisp, running on JVM.

this language is very naive, I write it to practise programming, and it can do some scripting jobs.

## Language design

+ Strict syntax
+ Lisp-style comments

```lisp
(233) ; Error: 233 isn't a function

(print 233) ; OK: 233 is a java.lang.Integer
```

+ Basic functions

```lisp
(+ 2 3 4 (* 2 5)) ; result: 19

(sqrt 100) ; result: 10.0

; also '||'
(&& (> 3 2 1) (>= 3 3 1)) ; result: true

(print 1) ; 1
(type 1) ; java.lang.Integer

```

+ Literals

```lisp
() ; null
null ; null
true ; true
false ; false
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

(to-str (file "deep")) ; result: "deep"
```

+ Lazy evaluation

```lisp
(print (if (>= 1 2)
    (read-file (file "out")) ; will not be read
    (read-file (file "in"))
))
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

Lice > (if true 1 2)
1 => java.lang.Integer

Lice > (set "file" (file "fuck_you"))
fuck_you => java.io.File

Lice > (write-file (get "file") (str-con "deep dark fantasy"))
deep dark fantasy => java.lang.String

Lice > (read-file (get "file"))
deep dark fantasy => java.lang.String
```

## Code style

### compiler

0. It's recommended to use 2-spaces tab actual(please DO USE tabs).
0. It's not recommended to use any language but Java/Kotlin.
0. It's recommended to use functional collection API.
0. Functions' results should be lazy evaluated.

### Lice language

0. Use symbols like '-\>', '?' to represent 'to', 'orNot'.
0. Use lisp-style function names.

