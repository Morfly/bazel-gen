package org.morfly.example.template

import org.morfly.bazelgen.analyzer.PackageName
import org.morfly.bazelgen.generator.dsl.BUILD
import org.morfly.bazelgen.generator.dsl.Label
import org.morfly.bazelgen.generator.dsl.Name
import org.morfly.bazelgen.generator.dsl.function.android_binary
import org.morfly.bazelgen.generator.dsl.function.artifact


fun root_build_template(
    binaryName: Name,
    packageName: PackageName,
    internalDeps: List<Label>,
    externalDeps: List<String>
    /**
     *
     */
) = BUILD {

    load("@rules_jvm_external//:defs.bzl", "artifact")

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
}