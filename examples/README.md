# Examples

## Generating Bazel projects

- [simple java:]() _Coming soon_
- [simple kotlin:]() _Coming soon_
- [simple android:]() _Coming soon_
- [androidx data binding (no stubs):](examples-generation/androidx-data-binding-no-stubs) Example of generating an
  Android project that uses Kotlin language, Data Binding and AndroidX libraries. The generated project does not
  use `kotlin-kapt` stub generation for data binding.

### Example projects structure

Each generation example consists of 2 directories:

- `config` - contains the configuration for the project to be generated. Entry point is `Main.kt`
  file.
- `generated-project` - contains generated Bazel project.

## Migrating Gradle projects to Bazel

- [simple android:]() _Coming soon_
- [android with dagger:]() _Coming soon_
- [androidx data binding (no stubs):]() _Coming soon_

### Example projects structure

Each example directory is a Gradle project that can be migrated to Bazel.

In order to migrate the project to Bazel the following command should be used:

```shell
$ ./gradlew migrateToBazel
```

During the migration the current project directory will be populated with the corresponding Bazel files.