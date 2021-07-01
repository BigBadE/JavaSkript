# JavaSkript

JavaSkript is a WIP transcompiler, trying to compile 
English text to Java bytecode.

# What is done

Updating this with everything done would be tedious, the easiest way to check
what has been done is by looking at the integrated tests
located in 
```(module)/src/integrationTest```

At the time of writing, the parser is mostly done, the api is mostly finalized,
and the translator is a WIP. After the translator, the compiler needs to be rewritten
(a version is available earlier in git history).

# What this will look like

Hopefully, exactly the same as normal Skript.
Another goal in mind is independence from Bukkit, with
a bukkit module providing a loader for Bukkit, and a java
module providing a loader for Java.

# Will this break addons?

The current planned system will break current addons, but it will
make the Skript API a thousand times more usable (hopefully).

The only new restriction imposed is that all independent addons must be 
[purely functional](https://en.wikipedia.org/wiki/Purely_functional_programming), 
and cannot reference other classes in the addon unless it is in a purely functional way.

# Independent addons

It is important to note the differences between dependent and independent addons.
Dependent addons are addons that the script directly depends on.
The addon code in dependent addons doesn't have to be purely functional,
but the addon must be present at runtime.

In practice, dependent addons are only needed when adding Skript
support to another program which isn't necessarily there at runtime.

Independent addons, on the other hand, will be "shaded" into the 
compiled jar. They must be purely functional, but they can reference 
other purely functional methods/fields/etc... 
(which will be shaded in also).

# Additional features

Now addons can add string addons and literal addons.

String addons are detected in strings, and allow additional
string functionality (like expressions in strings, color codes
in strings, etc...).

Literal addons add custom literals, such as numbers or even
strings themselves. They are like expressions, except with
greater control over the parsing and checked last.

Addons can also override anything they can register. This is useful for stuff like
the Bukkit module overriding the Java module's "on script load" def.

Defs have to interact with key/values using transformers and headers.
Since defs are methods, headers are shaded in as the first instructions
in the method. Transformers control the class/method and method signature/modifiers.
