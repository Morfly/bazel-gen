load("//tools/kotlin:kotlin.bzl", "kt_android_library")
load("@rules_jvm_external//:defs.bzl", "artifact")

package(
    default_visibility = ["//visibility:public"],
)

DATABINDING_LAYOUTS = ["src/main/res/layout/layout_app.xml"]

VIEW_MODELS_WITH_RES_IMPORTS = []

VIEW_MODELS = ["src/main/kotlin/org/morfly/bazelgen/example/app/AppViewModel.kt"] + [
    "modify_imports_in_" + view_model_with_res_imports[0:-3]
    for view_model_with_res_imports in VIEW_MODELS_WITH_RES_IMPORTS
]

[
    genrule(
        name = "modify_imports_in_" + file[0:-3],
        srcs = [file],
        outs = [file[0:-3] + "_synthetic.kt"],
        cmd = """
        cat $(SRCS) |
        sed 's/import org.morfly.bazelgen.example.app.R/import org.morfly.bazelgen.example.app.viewmodels.R/g' > $(OUTS)
        """,
    )
    for file in VIEW_MODELS_WITH_RES_IMPORTS
]

BINDING_ADAPTERS = ["src/main/kotlin/org/morfly/bazelgen/example/app/BindingAdapters.java"]

EXCLUDED_APP_FILES = VIEW_MODELS + VIEW_MODELS_WITH_RES_IMPORTS + BINDING_ADAPTERS

APP_FILES_WITH_RESOURCE_IMPORTS = glob(
    [
        "src/main/kotlin/**/*.kt",
        "src/main/kotlin/**/*.java",
    ],
    exclude = EXCLUDED_APP_FILES,
)

APP_FILES = [] + [
    "modify_imports_in_" + app_files_with_res_imports[0:-3]
    for app_files_with_res_imports in APP_FILES_WITH_RESOURCE_IMPORTS
]

[
    genrule(
        name = "modify_imports_in_" + file[0:-3],
        srcs = [file],
        outs = [file[0:-3] + "_synthetic.kt"],
        cmd = """
        cat $(SRCS) |
        sed 's/import org.morfly.bazelgen.example.app.databinding./import org.morfly.bazelgen.example.app.databinding.databinding./g' > $(OUTS)
        """,
    )
    for file in APP_FILES_WITH_RESOURCE_IMPORTS
]

android_library(
    name = "resources",
    custom_package = "org.morfly.bazelgen.example.app.res",
    manifest = "src/main/AndroidManifest.xml",
    exports_manifest = True,
    resource_files = glob(
        ["src/main/res/**"],
        exclude = DATABINDING_LAYOUTS,
    ),
    deps = ["//lib1"],
)

kt_android_library(
    name = "view_models",
    srcs = VIEW_MODELS,
    custom_package = "org.morfly.bazelgen.example.app.viewmodels",
    manifest = "src/main/ViewModelsManifest.xml",
    enable_data_binding = True,
    deps = [
        ":resources",
        artifact("androidx.databinding:databinding-common"),
        artifact("androidx.databinding:databinding-adapters"),
        artifact("androidx.databinding:databinding-runtime"),
        artifact("androidx.annotation:annotation"),
    ],
)

android_library(
    name = "databinding_resources",
    srcs = BINDING_ADAPTERS,
    custom_package = "org.morfly.bazelgen.example.app.databinding",
    enable_data_binding = True,
    exports_manifest = True,
    manifest = "src/main/DataBindingResourcesManifest.xml",
    resource_files = DATABINDING_LAYOUTS,
    deps = [
        ":resources",
        ":view_models",
        artifact("androidx.databinding:databinding-adapters"),
        artifact("androidx.databinding:databinding-common"),
        artifact("androidx.databinding:databinding-runtime"),
    ],
)

kt_android_library(
    name = "app",
    srcs = APP_FILES,
    custom_package = "org.morfly.bazelgen.example.app",
    manifest = "src/main/AndroidManifest.xml",
    exports_manifest = True,
    enable_data_binding = True,
    visibility = ["//visibility:public"],
    deps = [
        ":resources",
        ":view_models",
        ":databinding_resources",
        artifact("androidx.databinding:databinding-common"),
        artifact("androidx.databinding:databinding-adapters"),
        artifact("androidx.databinding:databinding-runtime"),
        artifact("androidx.annotation:annotation"),
    ],
)

exports_files(
    ["src/main/AndroidManifest.xml"],
    visibility = ["//:__pkg__"],
)