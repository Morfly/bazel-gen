package org.morfly.example.template

import org.morfly.bazelgen.generator.dsl.BUILD
import org.morfly.bazelgen.generator.dsl.function.java_import
import org.morfly.bazelgen.generator.dsl.function.java_plugin


fun tools_build_template(
    /**
     *
     */
) = BUILD("tools/android") {

    java_plugin {
        name = "compiler_annotation_processor"
        processor_class = "android.databinding.annotationprocessor.ProcessDataBinding"
        visibility = public
        generates_api = true
        deps = list(
            "@bazel_tools//src/tools/android/java/com/google/devtools/build/android:all_android_tools"
        )
    }

    java_import {
        name = "android_sdk"
        jars = list("@bazel_tools//tools/android:android_jar")
        neverlink = true
        visibility = public
    }
}