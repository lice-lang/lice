![](./GUI/icon.jpg)

# Lice

This is an interpreter for a dialect of lisp, running on JVM.

this language is very naive, I write it to practise programming, and it can do some scripting jobs.

## [About the language](./FEATURES.md)

## It looks like:

```lisp
(println "Hello " "World")
(for-each i (.. 1 10) (print i))
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

