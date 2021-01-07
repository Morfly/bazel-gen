# bazel-gen

A Kotlin tool that allows to:

- Generate Bazel projects
- Migrate Gradle projects to Bazel (coming soon)

## Generating Starlark code

A [Kotlin DSL]() that closely resembles [Starlark]() helps to describe Bazel build files in a type-safe manner.

### Declaring Bazel files

All Bazel files can be described inside builder function. For example, in order to create a `BUILD` there should be used
the following function:

```kotlin
BUILD {
    ...
}
```

Bazel also supports files with `.bazel` extension, that can be described using the following function:

```kotlin
BUILD.bazel {
    ...
}
```

It is also possible to specify the exact `BUILD` file location by setting the `relativePath` argument:

```kotlin
BUILD(relativePath = "app") {
    ...
}
```

In order to declare a Bazel `WORKSPACE` file there should be used functions `WORKSPACE` or `WORKSPACE.bazel`.

It is also possible to use functions `bazelrc` and `bazelversion` to describe the appropriate files.

### Rules

The `bazel-gen` Kotlin DSL has a library of commonly used Bazel rules. In order to declare a target in Bazel file there
should be used the following function

```kotlin
java_binary {
    name = "application"
    srcs = glob("src/main/java/**/*.java")
}
```

If for some reason the DSL does not contain the needed param, it an be declared dynamically with using string name

```kotlin
android_binary {
    name = "application"
    "manifest_values" `=` {
        "minSdkVersion" to "23"
    }
}
```

The same approach can be used for custom rules

```kotlin
"custom_rule" {
    "name" `=` "application"
}
```

### Assignments

The following snippet shows how to declare an assignment statement

```kotlin
"DEPENDENCIES" `=` list("//lib")
```

In order to refer to the declared variable, there should be used `ref()` extension function. This helps to assign a
string variable reference to the filed that expects list as a param.

```kotlin
android_binary {
    name = "application"
    deps = "DEPENDENCIES".ref()
}
```

### Comprehensions

Comprehensions are powerful construct in Starlark language that, for instance, can help to declare multiple targets for
each list item.

The following list comprehension, described in Kotlin DSL

```kotlin
"${PACKAGE_NAME}_FILES" `=` list(...)

genrule {
    name = "generate_" `+` "file".ref()
    ...
}(`for` = "file", `in` = "${TARGET_NAME}_FILES".ref())
```

will generate the corresponding Starlark statement

```python
APP_FILES = [...]

[
    genrule(
        name = "generate_" + file
        ...
    ) 
    for file in APP_FILES
]
```

### `.bazelrc` files

In order to describe `.bazelrc` files, the same approach is used

```kotlin
bazelrc {

    build {
        -"java_header_compilation=false"
    }

    "mobile-install" {
        -"java_header_compilation=false"
        -"start_app"
    }

    `try-import` { "local.bazelrc" }
}
```

It is possible to customize the file name using the following approach.

```kotlin
"local".bazelrc {
    ...
}
```

or

```kotlin
object local : bazelrc

...

local.bazelrc {
    ...
}
```

### `.bazelversion` files

```kotlin
bazelversion(4, 0, 0)
```

### Writing declared files

```kotlin
fun main() {
    val sourceRoot = "src/main/java"

    val build = BUILD.bazel("app") {
        java_binary {
            name = "app"
            srcs = glob("$sourceRoot/**/*.java")
        }
    }
    val writer = BazelFileWriter()
    writer.write(workspaceDir = "root/project/dir", file = build)
}
```

This will create a Bazel `BUILD` file under the following location `root/project/dir/app/BUILD.bazel`.

## Getting started

### Generating Bazel projects

#### Gradle

```groovy
// build.gradle

dependencies {
    implementation 'org.morfly.bazelgen:generator:<version>'
}
```

Note: _to be uploaded to artifactory_

```groovy
// settings.gradle

includeBuild 'path/to/bazel-gen'
```

#### Gradle Kotlin DSL

```kotlin
// build.gradle.kts

dependencies {
    implementation("org.morfly.bazelgen:generator:<version>")
}
```

Note: _to be uploaded to artifactory_

```kotlin
// settings.gradle.kts

includeBuild("path/to/bazel-gen")
```

### Migrating Gradle projects to Bazel

_Coming soon_

## Examples

Check out the directory with [example projects](examples) for more details.