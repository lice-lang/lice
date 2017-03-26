![](./src/main/kotlin/org/lice/icon.jpg)

# Lice

[![Release](https://jitpack.io/v/lice-lang/lice.svg)]
(https://jitpack.io/#lice-lang/lice)

This is an interpreter for a dialect of lisp, running on JVM.

## [About the language](https://github.com/lice-lang/lice-reference)

## It looks like:

```lisp
(println "Hello " "World")
(for-each i (.. 1 10) (println i))
```

## Build

You can simply use gradle by adding JitPack to your repository set and add the dependency:

```groovy
allprojects {
  repositories {
    // ...
    maven { url 'https://jitpack.io' }
  }
}

dependencies {
  compile 'com.github.lice-lang:lice:v2.4'
}
```

But if you use Scala, you can add it to your sbt dependency, by adding the stuffs below:

```sbtshell
resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies += "com.github.lice-lang" % "lice" % "v2.4"
```

And if you're a Clojure developer, why not try build it with leiningen?

```leiningen
:repositories [["jitpack" "https://jitpack.io"]]

:dependencies [[com.github.lice-lang/lice "v2.4"]]
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
