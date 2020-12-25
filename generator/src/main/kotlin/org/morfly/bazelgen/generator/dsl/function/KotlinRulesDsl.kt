@file:Suppress("PropertyName", "FunctionName")

package org.morfly.bazelgen.generator.dsl.function

import org.morfly.bazelgen.generator.dsl.BuildContext
import org.morfly.bazelgen.generator.dsl.Label
import org.morfly.bazelgen.generator.dsl.Name
import org.morfly.bazelgen.generator.dsl.WorkspaceContext
import org.morfly.bazelgen.generator.dsl.feature.FunctionCallContext


/**
 *
 */
fun BuildContext.kt_compiler_plugin(body: KtCompilerPluginContext.() -> Unit) =
    functionCallStatement("kt_compiler_plugin", KtCompilerPluginContext(), body)

/**
 *
 */
fun BuildContext.kt_jvm_binary(body: KtJvmBinaryContext.() -> Unit) =
    functionCallStatement("kt_jvm_binary", KtJvmBinaryContext(), body)

/**
 *
 */
fun BuildContext.kt_jvm_import(body: KtJvmImportContext.() -> Unit) =
    functionCallStatement("kt_jvm_import", KtJvmImportContext(), body)

/**
 *
 */
fun BuildContext.kt_jvm_library(body: KtJvmLibraryContext.() -> Unit) =
    functionCallStatement("kt_jvm_library", KtJvmLibraryContext(), body)

/**
 *
 */
fun BuildContext.kt_android_library(body: KtAndroidLibrary.() -> Unit) =
    functionCallStatement("kt_android_library", KtAndroidLibrary(), body)

/**
 *
 */
fun WorkspaceContext.define_kt_toolchain(body: DefineKtToolchainContext.() -> Unit) =
    functionCallStatement("define_kt_toolchain", DefineKtToolchainContext(), body)

/**
 *
 */
fun WorkspaceContext.kt_register_toolchains() =
    functionCallStatement("kt_register_toolchains", FunctionCallContext(), {})

/**
 *
 */
fun WorkspaceContext.kotlin_repositories(body: KotlinRepositoriesContext.() -> Unit) =
    functionCallStatement("kotlin_repositories", KotlinRepositoriesContext(), body)


open class KtCompilerPluginContext : FunctionCallContext() {
    var name: Name by kwargsHolder
    var compile_phase: Boolean? by kwargsHolder
    var deps: List<Label?>? by kwargsHolder
    var id: String by kwargsHolder
    var options: Map<String, CharSequence?>? by kwargsHolder
    var stubs_phase: Boolean? by kwargsHolder
    var target_embedded_compiler: Boolean? by kwargsHolder

    var visibility: List<Label?>? by kwargsHolder
}


open class KtJvmBinaryContext : FunctionCallContext() {
    var name: Name by kwargsHolder
    var data: List<Label?>? by kwargsHolder
    var deps: List<Label?>? by kwargsHolder
    var jvm_flags: List<CharSequence?>? by kwargsHolder
    var main_class: String by kwargsHolder
    var module_name: CharSequence? by kwargsHolder
    var plugins: List<Label?>? by kwargsHolder
    var resource_jars: List<Label?>? by kwargsHolder
    var resource_strip_prefix: CharSequence? by kwargsHolder
    var resources: List<Label?>? by kwargsHolder
    var runtime_deps: List<Label?>? by kwargsHolder
    var srcs: List<Label?>? by kwargsHolder

    var visibility: List<Label?>? by kwargsHolder
}


open class KtJvmImportContext : FunctionCallContext() {
    var name: Name by kwargsHolder
    var deps: List<Label?>? by kwargsHolder
    var exported_compiler_plugins: List<Label?>? by kwargsHolder
    var exports: List<Label?>? by kwargsHolder
    var jar: Label? by kwargsHolder
    var jars: List<Label?>? by kwargsHolder
    var neverlink: Boolean? by kwargsHolder
    var runtime_deps: List<Label?>? by kwargsHolder
    var srcjar: Label? by kwargsHolder

    var visibility: List<Label?>? by kwargsHolder
}


open class KtJvmLibraryContext : FunctionCallContext() {
    var name: Name by kwargsHolder
    var data: List<Label?>? by kwargsHolder
    var deps: List<Label?>? by kwargsHolder
    var exported_compiler_plugins: List<Label?>? by kwargsHolder
    var exports: List<Label?>? by kwargsHolder
    var module_name: CharSequence? by kwargsHolder
    var neverlink: Boolean? by kwargsHolder
    var plugins: List<Label?>? by kwargsHolder
    var resource_jars: List<Label?>? by kwargsHolder
    var resource_strip_prefix: CharSequence? by kwargsHolder
    var resources: List<Label?>? by kwargsHolder
    var runtime_deps: List<Label?>? by kwargsHolder
    var srcs: List<Label?>? by kwargsHolder

    var visibility: List<Label?>? by kwargsHolder
}


open class KtAndroidLibrary : FunctionCallContext() {
    var name: Name by kwargsHolder
    var exports: List<Label?>? by kwargsHolder
    var visibility: List<Label?>? by kwargsHolder

    var srcs: List<Label?>? by kwargsHolder
    var deps: List<Label?>? by kwargsHolder
    var custom_package: CharSequence? by kwargsHolder
    var manifest: Label? by kwargsHolder
    var resource_files: List<Label?>? by kwargsHolder
    var enable_data_binding: Boolean? by kwargsHolder
    var exports_manifest: Boolean? by kwargsHolder
}


open class DefineKtToolchainContext : FunctionCallContext() {
    var name: Name by kwargsHolder
    var api_version: CharSequence? by kwargsHolder
    var jvm_target: CharSequence? by kwargsHolder
    var language_version: CharSequence? by kwargsHolder
    var experimental_use_abi_jars: Boolean? by kwargsHolder
    var javac_options: Map<String, CharSequence?>? by kwargsHolder
    var kotlinc_options: Map<String, CharSequence?>? by kwargsHolder
}

open class KotlinRepositoriesContext : FunctionCallContext() {
    var compiler_release: CharSequence by kwargsHolder
}