@file:Suppress("LocalVariableName")

package org.morfly.example.template

import org.morfly.bazelgen.descriptor.PackageName
import org.morfly.bazelgen.descriptor.RelativePath
import org.morfly.bazelgen.generator.dsl.BUILD
import org.morfly.bazelgen.generator.dsl.Label
import org.morfly.bazelgen.generator.dsl.feature.ref
import org.morfly.bazelgen.generator.dsl.function.*


fun android_data_binding_library_build_template(
    relativePath: RelativePath,
    dataBindingLayouts: List<Label>,
    viewModelsWithRes: List<Label>,
    viewModels: List<Label>,
    bindingAdapters: List<Label>,
    targetName: String,
    packageName: PackageName,
    internalDeps: List<Label>,
    externalDeps: List<String>,
    exportAndroidManifest: Boolean
    /**
     *
     */
) = BUILD(relativePath) {
    val TARGET_NAME = targetName.toUpperCase()

    load("//tools/kotlin:kotlin.bzl", "kt_android_library")
    load("@rules_jvm_external//:defs.bzl", "artifact")

    `package`(default_visibility = public)

    "DATABINDING_LAYOUTS" `=` dataBindingLayouts

    "VIEW_MODELS_WITH_RES_IMPORTS" `=` viewModelsWithRes

    "VIEW_MODELS" `=` viewModels `+` ("modify_imports_in_" `+` "view_model_with_res_imports[0:-3]".ref())(
        `for` = "view_model_with_res_imports", `in` = "VIEW_MODELS_WITH_RES_IMPORTS".ref()
    )

    genrule {
        name = "modify_imports_in_" `+` "file[0:-3]".ref()
        srcs = list("file".ref())
        outs = list("file[0:-3]".strref `+` "_synthetic.kt")
        cmd = """
            cat $(SRCS) |
            sed 's/import $packageName.R/import $packageName.viewmodels.R/g' > $(OUTS)
         """.trimIndent()
    }(`for` = "file", `in` = "VIEW_MODELS_WITH_RES_IMPORTS".ref())

    "BINDING_ADAPTERS" `=` bindingAdapters

    "EXCLUDED_${TARGET_NAME}_FILES" `=` "VIEW_MODELS".ref() `+` "VIEW_MODELS_WITH_RES_IMPORTS".ref() `+` "BINDING_ADAPTERS".ref()

    "${TARGET_NAME}_FILES_WITH_RESOURCE_IMPORTS" `=` glob(
        "src/main/kotlin/**/*.kt",
        "src/main/kotlin/**/*.java",
        exclude = "EXCLUDED_${TARGET_NAME}_FILES".ref()
    )

    "${TARGET_NAME}_FILES" `=` list<String>() `+` ("modify_imports_in_" `+` "app_files_with_res_imports[0:-3]".ref())(
        `for` = "app_files_with_res_imports", `in` = "${TARGET_NAME}_FILES_WITH_RESOURCE_IMPORTS".ref()
    )

    genrule {
        name = "modify_imports_in_" `+` "file[0:-3]".ref()
        srcs = list("file".ref())
        outs = list("file[0:-3]".strref `+` "_synthetic.kt")
        cmd = """
            cat $(SRCS) |
            sed 's/import $packageName.databinding./import $packageName.databinding.databinding./g' > $(OUTS)
        """.trimIndent()
    }(`for` = "file", `in` = "${TARGET_NAME}_FILES_WITH_RESOURCE_IMPORTS".ref())

    android_library {
        name = "resources"
        custom_package = "$packageName.res"
        manifest = "src/main/AndroidManifest.xml"
        exports_manifest = true
        resource_files = glob(
            "src/main/res/**",
            exclude = "DATABINDING_LAYOUTS".ref()
        )
        deps = internalDeps + externalDeps.map { artifact(it) }
    }

    kt_android_library {
        name = "view_models"
        srcs = "VIEW_MODELS".ref()
        custom_package = "$packageName.viewmodels"
        manifest = "src/main/ViewModelsManifest.xml"
        enable_data_binding = true
        deps = list(
            ":resources",
            artifact("androidx.databinding:databinding-common"),
            artifact("androidx.databinding:databinding-adapters"),
            artifact("androidx.databinding:databinding-runtime"),
            artifact("androidx.annotation:annotation")
        )
    }

    android_library {
        name = "databinding_resources"
        srcs = "BINDING_ADAPTERS".ref()
        custom_package = "$packageName.databinding"
        enable_data_binding = true
        exports_manifest = true
        manifest = "src/main/DataBindingResourcesManifest.xml"
        resource_files = "DATABINDING_LAYOUTS".ref()
        deps = list(
            ":resources",
            ":view_models",
            artifact("androidx.databinding:databinding-adapters"),
            artifact("androidx.databinding:databinding-common"),
            artifact("androidx.databinding:databinding-runtime")
        )
    }

    kt_android_library {
        name = targetName
        srcs = "${TARGET_NAME}_FILES".ref()
        custom_package = packageName
        manifest = "src/main/AndroidManifest.xml"
        exports_manifest = true
        enable_data_binding = true
        visibility = public
        deps = list(
            ":resources",
            ":view_models",
            ":databinding_resources",
            artifact("androidx.databinding:databinding-common"),
            artifact("androidx.databinding:databinding-adapters"),
            artifact("androidx.databinding:databinding-runtime"),
            artifact("androidx.annotation:annotation"),
        )
    }

    if (exportAndroidManifest)
        exports_files(
            "src/main/AndroidManifest.xml",
            visibility = list("//:$__pkg__")
        )
}