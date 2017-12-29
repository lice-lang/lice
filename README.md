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

Also, as the main repo for the Lice language, this repo will not be updated very frequently.  
Instead, I do language feature experiments in [The tiny version of Lice](https://github.com/lice-lang/lice-tiny), which is more actively updated and not guarenteed be backward capable.  
Once a feature is finished and tested, and not considered harmful, I'll copy the codes here and publish releases.

## It looks like

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

; passing a call-by-value lambda to a call-by-value lambda
((lambda op (op 3 4)) (lambda a b (+ (* a a) (* b b))))

; to define a call-by-need lambda, use `lazy`.
```

# Building

To use Lice with build tools, see [JitPack instruction](https://jitpack.io/#lice-lang/lice).

Alternatively, you can download the nightly jar for the newest commit on [AppVeyor](https://ci.appveyor.com/project/ice1000/lice/branch/master/artifacts).

# Script API 

```java
import javax.script.*;

public class LiceScriptEngineTest {
    public static void main() throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("lice");
        engine.eval("(-> x 10)");
        engine.eval("(print x)");
    }
}
```

## Lice performance

Code to run:

```lisp
; loops
(def loop count block (|>
    (-> i 0)
    (while (< i count) (|> (block i)
    (-> i (+ i 1))))))

; invoking the function
(loop 200000 (lambda i (|>
    (defexpr let x y block (|>
        (-> x y) ; this is actually an issue of lice.
        (block)
        (undef x)))
    (let reimu 100 (lambda (|> x))))))

(print "loop count: " i)
```

Condition|Time
:---:|:---:
Lice call Java using `extern`|350ms
Lice call Java using Lice API|295ms
Pure Java|13ms
Pure Lice|897ms
Java call Lice using Lice API|629ms

## Lice invoking Java

Lice has handy APIs for interacting with Java.

```lisp
; declare an extern function
; must be a static Java function
(extern "java.util.Objects" "equals")

; calling the extern function
(equals 1 1)
```

## Java invoking Lice

This project provides handy APIs for running Lice codes from Java.

```java
// Running Lice
System.out.println(Lice.run("(+ 1 1)")); // prints 2
System.out.println(Lice.run(new File("example.lice"))); // run codes in a file

// Lice API
SymbolList env = new SymbolList();
Lice.run("(def blablabla a (+ a a)) (-> myVar 233)", env);
env.extractLiceFunction("blablabla").invoke(233); // result: 466
int var = ((Number) env.extractLiceVariable("myVar")).intValue(); // result: 233
```
