# bazel-gen

A Kotlin tool that allows to:

- Generate Bazel projects
- Migrate Gradle projects to Bazel (coming soon)

## Generating Starlark code

A powerful [Kotlin DSL]() that closely resembles [Starlark]() helps to describe Bazel build files in a type-safe manner.

The following example shows how Bazel build scripts that involve list concatenation can be described in Kotlin.

```kotlin
android_binary {
    name = binaryName
    custom_package = packageName
    manifest = "//app:src/main/AndroidManifest.xml"
    "manifest_values" `=` {
        "minSdkVersion" to "23"
        "targetSdkVersion" to "29"
    }
    enable_data_binding = true
    multidex = "native"
    incremental_dexing = 0
    dex_shards = 5
    deps = internalDeps + list(
        artifact("androidx.databinding:databinding-runtime")
    )
}
```

After describing the build script in Kotlin DSL, the appropriate Starlark code will be generated:

```python
android_binary(
    name = "example_app",
    custom_package = "org.morfly.bazelgen.example",
    manifest = "//app:src/main/AndroidManifest.xml",
    manifest_values = {
        "minSdkVersion": "23",
        "targetSdkVersion": "29",
    },
    enable_data_binding = True,
    multidex = "native",
    incremental_dexing = 0,
    dex_shards = 5,
    deps = [
        "//app",
        artifact("androidx.databinding:databinding-runtime"),
    ],
)
```

## Getting started

### Generating Bazel projects

...
## Examples

Check out the directory with [example projects](examples/examples-generation) for more details.