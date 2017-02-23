# lice

It's entirely working in progress....

This is an interpreter for a dialect of lisp, running on JVM

this language is very naive, I write it to practise programming, and it can do some scripting jobs.

## Language design

+ Strict syntax:

```lisp
(233)
```

Error: 233 isn't a function

```lisp
233
```

OK

+ Basic functions:

```lisp
(+ 2 3 4 (* 2 5))
```

## Repl

```lisp
Lice > (+ 1 1)
2 => java.lang.Integer

Lice > (* 2 2)
4 => java.lang.Integer

Lice> ()
null => java.lang.Object

Lice > (eval "(+ 1 1)")
2 => java.lang.Integer
lice.compiler.model.Value@448139f0 => lice.compiler.model.Value

Lice > (eval (str-con "(+ " "1 " "1)"))
2 => java.lang.Integer
lice.compiler.model.Value@7cca494b => lice.compiler.model.Value

Lice > ([] 1 11 1)
[1, 11, 1] => java.util.ArrayList

Lice > ([] "boy" "next" "door")
[boy, next, door] => java.util.ArrayList

Lice > (eval (read-file (file "sample/test3.lice")))
16769025 => java.lang.Integer
My name is Van, I'm an artist => java.lang.String
lice.compiler.model.Value@1dd0a5f7 => lice.compiler.model.Value

Lice > (if true 1 2)
1 => java.lang.Integer

Lice > (if false 1 2)
2 => java.lang.Integer
```

## Contribution

0. It's recommended to use 2-spaces tab actual(please DO USE tabs).




