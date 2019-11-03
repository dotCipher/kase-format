# KaseFormat
[ ![Download](https://api.bintray.com/packages/dotcipher/maven/kase-format/images/download.svg) ](https://bintray.com/dotcipher/maven/kase-format/_latestVersion)
[![CircleCI](https://circleci.com/gh/dotCipher/kase-format/tree/master.svg?style=shield)](https://circleci.com/gh/dotCipher/kase-format/tree/master)

Multiplatform kotlin string case conversion and detection library.

_(Inspired by [Guava's `CaseFormat`](https://github.com/google/guava/wiki/StringsExplained#caseformat))_

## Setup

### Declare repository
Include the following in your `respositories` block within the `build.gradle.kts`:
```kotlin
repositories {
    maven("https://dl.bintray.com/dotcipher/maven")
}
```
**Or** in groovy syntax for a `build.gradle`:
```groovy
repositories {
    maven {
        url 'https://dl.bintray.com/dotcipher/maven'
    }   
}
```
### Include dependency

Include the following in your dependencies block (replacing `<version>` 
with the latest release above:
```kotlin
dependencies {
        // JVM implementation
        implementation("io.dotcipher.kase:kase-format-jvm:<version>")

        // Native / JS implementations coming soon
}
```

## Usage

`KaseFormat` is the main entrypoint for library usage, and it's defined as an `enum` of the following formats:
- `LOWER_HYPHEN` (ie. `hello-world`)
- `CAPITAL_HYPHEN` (ie. `Hello-World`)
- `UPPER_HYPHEN` (ie. `HELLO-WORLD`)
- `LOWER_UNDERSCORE` (ie. `hello_world`)
- `CAPITAL_UNDERSCORE` (ie. `Hello_World`)
- `UPPER_UNDERSCORE` (ie. `HELLO_WORLD`)
- `LOWER_CAMEL` (ie. `helloWorld`)
- `CAPITAL_CAMEL` (ie. `HelloWorld`)

### Conversion

The most efficient way to convert is if the source format is known before conversion.
For example, if the intent is to convert from `LOWER_UNDERSCORE` (ie. `hello_world`) to
`CAPITAL_CAMEL` (ie. `HelloWorld`) then you could use the following syntax:
```kotlin
val str = "hello_world"
val output = KaseFormat.LOWER_UNDERSCORE.convert(KaseFormat.CAPITAL_CAMEL, str)
// output == "HelloWorld"
```

If the source format isn't known before conversion, then a `KaseConverter` needs to be
created targeting the destination format.  The `KaseConverter` can be reused and is thread-safe.
For example if the intent is to convert into the `LOWER_HYPHEN` format from any unknown format then
you can use the following syntax:
```kotlin
val strings = listOf("string_one", "STRING-TWO", "stringThree", "StringFour")
val converter = KaseFormat.LOWER_HYPHEN.converter()

val firstOutput = converter.convert(strings[0])
// firstOutput == "string-one"

val listOutput = converter.convert(strings.sublist(1, strings.size))
// listOutput == listOf("string-two", "string-three", "string-four")
```

### Detection

Detection of what format a string might be (by best guess), can be accomplished using the
following syntax:
```kotlin
val str = "someString"
val format = KaseFormat.determine(str)
// format == LOWER_CAMEL
```
