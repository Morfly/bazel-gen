package org.morfly.example.template

import org.morfly.bazelgen.generator.dsl.bazelrc


fun bazelrc_template(
    androidToolsLocation: String,
    disableStrictJavaDeps: Boolean
    /**
     *
     */
) = bazelrc {

    build {
        -"android_databinding_use_v3_4_args"
        -"experimental_android_databinding_v2"
        -"override_repository=android_tools=$androidToolsLocation"
        -"android_databinding_use_androidx"
        -"strategy=Desugar=sandboxed"
        -"java_header_compilation=false"
        -"verbose_failures"
        -"disk_cache=bazel-cache"
        -"define=android_incremental_dexing_tool=d8_dexbuilder"
        -"java_toolchain=@bazel_tools//tools/jdk:toolchain_java8"
        if (disableStrictJavaDeps) -"strict_java_deps=OFF"
    }

    `mobile-install` {
        -"android_databinding_use_v3_4_args"
        -"experimental_android_databinding_v2"
        -"override_repository=android_tools=$androidToolsLocation"
        -"android_databinding_use_androidx"
        -"strategy=Desugar=sandboxed"
        -"java_header_compilation=false"
        -"verbose_failures"
        -"disk_cache=bazel-cache"
        -"define=android_incremental_dexing_tool=d8_dexbuilder"
        -"java_toolchain=@bazel_tools//tools/jdk:toolchain_java8"
        if (disableStrictJavaDeps) -"strict_java_deps=OFF"

        -"start_app"
    }

    `try-import` { "local.bazelrc" }
}