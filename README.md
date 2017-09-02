![](./src/main/kotlin/org/lice/icon.jpg)

# Lice

CI|status
:---|:---:
Travis CI|[![Build Status](https://travis-ci.org/lice-lang/lice.svg?branch=master)](https://travis-ci.org/lice-lang/lice)
AppVeyor|[![Build status](https://ci.appveyor.com/api/projects/status/7d6lyinb0xr6hagn?svg=true)](https://ci.appveyor.com/project/ice1000/lice/branch/master)
CircleCI|[![CircleCI](https://circleci.com/gh/lice-lang/lice/tree/master.svg?style=svg)](https://circleci.com/gh/lice-lang/lice/tree/master)
CodeShip|[![Codeship Status for lice-lang/lice](https://app.codeship.com/projects/2e71d680-61fd-0135-bc9e-7aecbc4a3d79/status?branch=master)](https://app.codeship.com/projects/239723)

[![JitPack](https://jitpack.io/v/lice-lang/lice.svg)](https://jitpack.io/#lice-lang/lice)<br/>
[![Gitter](https://badges.gitter.im/lice-lang/lice.svg)](https://gitter.im/lice-lang/lice)<br/>
[![Dependency Status](https://www.versioneye.com/user/projects/58df5b1c24ef3e00425cf73f/badge.svg)](https://www.versioneye.com/user/projects/58df5b1c24ef3e00425cf73f)<br/>
[![codecov](https://codecov.io/gh/lice-lang/lice/branch/master/graph/badge.svg)](https://codecov.io/gh/lice-lang/lice)<br/>
[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)<br/>
[![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://github.com/KotlinBy/awesome-kotlin)<br/>

# About

This is the interpreter of Lice language, a dialect of Lisp, run on JVM platform.

It supports call-by-value, call-by-name, call-by-need(we sometimes call it lazy evaluation) at the same time.
Functions and values are treated as the same. Dynamic scoping, because I can't find a better scoping solution
for a interpreted language.

+ [About the Language](https://github.com/lice-lang/lice-reference)
+ [A simple Haskell implementation](./lice.hs)

See [FeatureTest](src/test/kotlin/org/lice/FeatureTest.kt) to learn more about the language's features.

# It looks like:

```lisp
; print a string
(print "Hello " "World" "\n")

; travel through a range
(for-each i (.. 1 10) (print i "\n"))

; define a call-by-name function
(defexpr fold ls init op
 (for-each index-var ls
   (-> init (op init index-var))))

; invoke the function defined above
(fold (.. 1 4) 0 +)

; passing a call-by-value lambda to a call-by-name lambda
((expr op (op 3 4)) (lambda a b (+ (* a a) (* b b))))

; to define a call-by-need lambda, use `lazy`.
```
# Script API 

```java
import javax.script.*;

public class LiceScriptEngineTest {
    public static void main() throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("lice");
        engine.eval("(def x 10)");
        engine.eval("(print x)");
    }
}
```

# Building

You can use `lice-lang` with Gradle by simply adding Jitpack into your repository; then add the lice-lang dependency

```groovy
allprojects {
  repositories {
    // ...
    maven { url 'https://jitpack.io' }
  }
}

dependencies {
  compile 'com.github.lice-lang:lice:v3.1.2'
}
```

If you use Scala, you can add it to your sbt dependency, by adding the lines below:

```sbtshell
resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies += "com.github.lice-lang" % "lice" % "v3.1.2"
```

And if you're a Clojure developer, why not try to build it with leiningen?

```leiningen
:repositories [["jitpack" "https://jitpack.io"]]

:dependencies [[com.github.lice-lang/lice "v3.1.2"]]
```

# Contributing

Pull requests are welcomed, but please follow our code style guide below:

## Code style

### compiler

0. Use actual tab character instead of spaces
0. Please use Java or Kotlin as a development language
0. Functional collection APIs are prefered.
0. Results of functions should be lazy evaluation.

### Lice language

0. Use symbols like `-\>`, `?` to represent `to`, `orNot`. It's clearer.
0. Use Lisp-style function names.

Example:

```lisp
; good
(int->str 1)
; bad
(intToStr 1)

; good
(null? ())
; bad
(nullOrNot ())
```
