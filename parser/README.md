# Language Specification

The language specification is provided as a list of all features supported by JavaSkript. Every feature will have a
short example code, following the format of

```
on function:
    broadcast effect to console
```

These will also be implemented in an integration test, which can be found in the src/integrationTest/java folder.

Any features will also be marked as "V" (Vanilla).

Vanilla features are features that are also available in the original Skript plugin.

Any new features will be marked as "JS" (JavaSkript).

Any features added by add-ons will be marked by their letter, currently there is only
"B" for Bukkit

Features listed will have one of two prefixes:

*Prefix*|*Meaning*
--------|---------
&#10004;|Implemented in master branch of JavaSkript
&#10006;|Not implemented in JavaSkript
&#10006; *(PR)*|Not implemented in JavaSkript but has an open PR
&#10006; *(issue)*|Not implemented in JavaSkript but has an open issue
&#10004; *(major version.minor version)*|Implemented in given release
&#10004; *(branch)* *(optional: commit/PR)*|Implemented in given branch of JavaSkript

Index:

1. [&#10004; Terms and definitions (V)](#terms)
1. [&#10004; Lexical Structure (V)](#lexical-structure)
    1. [&#10004; Functions (V)](#functions)
    1. [&#10004; Comments (V)](#comments)
    1. [&#10006; Literals (V)](#literals)
        1. [&#10004; Strings (V)](#strings)
        1. [&#10006; Special Characters (V/JS/B)](#strings)
        1. [&#10006; String Interpolation (V/JS)](#string-interpolation)
        1. [&#10006; Numbers (V/JS)](#numbers)
1. [&#10004; Variables (V)](#variables)
    1. [&#10006; Lists (V)](#lists)
    1. [&#10006; Generics (JS)](#generics)
1. [&#10006; Built-in Functions](#built-in-functions)
    1. [&#10006; Configuration](#configuration)

## Terms

### User and developer

"Users" are the people running the scripts, "Developers" are the people coding the scripts

### Function

Functions are a data container. They contain a set of key-value pairs in YAML-like format

```
on function:
    key: my value
```

### Expressions

Expressions are a line of code, optionally requiring arguments as input.

```
on script load:
    boardcast "Expression!"
```

### Effects

Effects are expressions that return an output. Since they return a value, they aren't always a single line of code and
can be used as arguments to expressions or effects. Internally, all [Variables](#variables) are built-in effects, but
will not be referenced as such

```
on script load:
    boardcast 1 + 2
```

### Skript vs JavaSkript vs Scripts

Skript is the name of the language specification and the original plugin by Njol. Hereafter, all mentions of Skript will
be referencing the language specification and not the plugin.

JavaSkript is the name of the trans-compiler which compiles Skript to Java Bytecode.

Scripts are developer-written code intended to be compiled.

## Lexical Structure

This section will explain the lexical structure of Skript

### Functions

Functions are a container of information. The function decided by the player decides when the function itself is run.
Functions contain both single-line and multi-line key-value pairs in a YAML-like format.

```
on function:
    key: First value
    code:
        broadcast "This is my code!"
        broadcast "It can have multiple lines!"
    second: Another value
```

### Comments

Comments allow the developer to write without effecting the program itself. Usually used to document the code, they are
denoted by a # as the first non-spacing character.

```
# This is a comment!
#This does nothing!
                #And doesn't care about spacing!
```

### Literals

Literals allow developer-supplied values into the program

### Strings

Strings are a sequence of characters, usually used to send messages to a human

```
on script start:
    broadcast "This is a string!"
```

### Special Characters

Special characters are reserved and must be escaped in a String. % is reserved
for [String Interpolation](#string-interpolation), if you want to insert a % put "%%" instead. While the compiler might
detect any accidental %s, it is not guaranteed.

{variable} will act as outlined in [String Interpolation](#string-interpolation)

(B) Color codes are reserved, such as &1-9, &a-f, &k, &l, &m, &n, &o, &r
(B) <(color)> will have the same effect as a color code
(B) <#(hex code)> will have the same effect as a color code, but with hex

(JS) Variable names cannot be colored, because JS cannot do that

### String Interpolation

Variables and effects can be inserted into strings via one of two methods

Method 1: Special Characters
[Effects](#effects) can be inserted with "%(effect)%",
[Variables](#variables) can be inserted with "{variable}"

### Numbers

Numbers will be, by default, 64 bit in floating-point context (Java's doubles)

(JS) Other types of numbers can be used by adding the appropriate type symbol(s) after it. All number types are signed
and two's compliment

Symbol|Type|Size|Range|Format
------|----|----|-----|------
B|Byte|8 bits|[-128, 127]|Fixed Point
C|Char|16 bits|[\u0000, \uFFFF]|Unicode
S|Short|16 bits|[-32768, 32767]|Fixed Point
I|Integer|32 bits|[-2^31, 2^31-1]|Fixed Point
L|Long|64 bits|[-2^63, 2^63-1]|Fixed Point
F|Float|32 bits|[~-2^127, ~2^127]|Floating Point
D|Double|64 bits|[~-2^1023, ~2^1023]|Floating Point
BD|BigDecimal|Infinite|Infinite|Fixed Point
BI|BigInteger|Infinite|Infinite|Fixed Point

## Variables

Variables are used to store values for future use. Variables can have expressions in the name
using [String Interpolation](#string-interpolation)
Any words before spaces are ignored, and can be used for documentation
Variables can have different prefixes:

Prefix|Meaning
------|-------
_|Local Variable, can only be accessed from inside the code block
@|Configuration Variable, gotten from the [Configuration](#configuration) block

```
on script load:
    set {My variable test} to 12
    set {_Number testing.%test%} to {test} + 24

on my function:
    #Prints nothing because {_testing} is local
    broadcast {_testing.12}
```

### Lists
Lists are a subsets of variables, functionally similar to putting expressions in variables

```
on script load:
    set test to "Brian"
    set {ages::%test%} to 23
```

## Built-in Functions
Built-in functions come standard with every JavaSkript jar

## Configuration
Configuration values can be put in the configuration block to be
referenced from the [Variable](#variables) with the associated name.
These are read-only, they cannot be set.
Any comments above a configuration value will be saved in the config file given to the user.

```
config:
    key: My value

on skript load:
    broadcast {@key}
```
