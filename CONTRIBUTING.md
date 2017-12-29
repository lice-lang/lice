# Lice contributing guides

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
