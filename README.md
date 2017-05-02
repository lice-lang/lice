![](./src/main/kotlin/org/lice/icon.jpg)

# Lice

CI|status
:---|:---:
Travis CI|[![Build Status](https://travis-ci.org/lice-lang/lice.svg?branch=master)](https://travis-ci.org/lice-lang/lice)
AppVeyor|[![Build status](https://ci.appveyor.com/api/projects/status/7d6lyinb0xr6hagn?svg=true)](https://ci.appveyor.com/project/ice1000/lice/branch/master)
CircleCI|[![CircleCI](https://circleci.com/gh/lice-lang/lice/tree/master.svg?style=svg)](https://circleci.com/gh/lice-lang/lice/tree/master)

[![JitPack](https://jitpack.io/v/lice-lang/lice.svg)](https://jitpack.io/#lice-lang/lice)<br/>
[![Gitter](https://badges.gitter.im/lice-lang/lice.svg)](https://gitter.im/lice-lang/lice)<br/>
[![Dependency Status](https://www.versioneye.com/user/projects/58df5b1c24ef3e00425cf73f/badge.svg?style=square)](https://www.versioneye.com/user/projects/58df5b1c24ef3e00425cf73f)<br/>
[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)<br/>
[![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://github.com/KotlinBy/awesome-kotlin)<br/>

This is an interpreter for a dialect of lisp, running on JVM.

## [About the language](https://github.com/lice-lang/lice-reference)

See [FeatureTest](src/test/kotlin/org/lice/FeatureTest.kt) to learn more about the language
feature.

## It looks like:

```lisp
(print "Hello " "World" "\n")
(for-each i (.. 1 10) (print i "\n"))
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
  compile 'com.github.lice-lang:lice:v3.1.1'
}
```

But if you use Scala, you can add it to your sbt dependency, by adding the stuffs below:

```sbtshell
resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies += "com.github.lice-lang" % "lice" % "v3.1.1"
```

And if you're a Clojure developer, why not try build it with leiningen?

```leiningen
:repositories [["jitpack" "https://jitpack.io"]]

:dependencies [[com.github.lice-lang/lice "v3.1.1"]]
```

## Contribute

Pull requests are welcomed, but please DO follow:

## Code style

### compiler

0. It's recommended to use 2-spaces tab actual(please DO USE tabs).
0. It's not recommended to use any language but Java/Kotlin.
0. It's recommended to use functional collection API.
0. Functions' results should be lazy evaluated.

### Lice language

0. Use symbols like '-\>', '?' to represent 'to', 'orNot'. It's clearer.
0. Use lisp-style function names.
