@file:Suppress("FunctionName", "PropertyName")

package org.morfly.bazelgen.generator.dsl.function

import org.morfly.bazelgen.generator.dsl.*
import org.morfly.bazelgen.generator.dsl.Label
import org.morfly.bazelgen.generator.dsl.Name
import org.morfly.bazelgen.generator.dsl.feature.FunctionCallContext


/**
 *
 */
fun BuildContext.android_library(body: AndroidLibraryContext.() -> Unit) =
    functionCallStatement("android_library", AndroidLibraryContext(), body)

/**
 *
 */
fun BuildContext.android_binary(body: AndroidBinaryContext.() -> Unit) =
    functionCallStatement("android_binary", AndroidBinaryContext(), body)

/**
 *
 */
fun BuildContext.aar_import(body: AarImportContext.() -> Unit) =
    functionCallStatement("aar_import", AarImportContext(), body)

/**
 *
 */
fun WorkspaceContext.android_sdk_repository(body: AndroidSdkRepositoryContext.() -> Unit) =
    functionCallStatement("android_sdk_repository", AndroidSdkRepositoryContext(), body)

/**
 *
 */
fun WorkspaceContext.android_ndk_repository(body: AndroidNdkRepositoryContext.() -> Unit) =
    functionCallStatement("android_ndk_repository", AndroidNdkRepositoryContext(), body)


open class AndroidLibraryContext : FunctionCallContext() {
    var name: Name by kwargsHolder
    var custom_package: CharSequence? by kwargsHolder
    var manifest: Label? by kwargsHolder
    var exports_manifest: Boolean? by kwargsHolder
    var resource_files: List<Label?>? by kwargsHolder
    var srcs: List<Label?>? by kwargsHolder
    var enable_data_binding: Boolean? by kwargsHolder
    var plugins: List<Label?>? by kwargsHolder

    var deps: List<Label?>? by kwargsHolder
    var visibility: List<Label?>? by kwargsHolder
}


open class AndroidBinaryContext : FunctionCallContext() {
    var name: Name by kwargsHolder
    var custom_package: CharSequence? by kwargsHolder
    var manifest: Label? by kwargsHolder
    var manifest_values: Map<String, CharSequence?>? by kwargsHolder
    var debug_key: Label? by kwargsHolder
    var enable_data_binding: Boolean? by kwargsHolder
    var multidex: CharSequence? by kwargsHolder
    var incremental_dexing: Int? by kwargsHolder
    var crunch_png: Boolean? by kwargsHolder
    var dex_shards: Int? by kwargsHolder
    var resource_files: List<Label?>? by kwargsHolder
    var srcs: List<Label?>? by kwargsHolder
    var plugins: List<Label?>? by kwargsHolder

    var deps: List<Label?>? by kwargsHolder
    var visibility: List<Label?>? by kwargsHolder

    var args: List<CharSequence>? by kwargsHolder
    var env: Map<String, CharSequence?>? by kwargsHolder
    var output_licenses: List<CharSequence?>? by kwargsHolder
}


open class AarImportContext : FunctionCallContext() {
    var name: Name by kwargsHolder
    var aar: Label by kwargsHolder
    var exports: List<Label?>? by kwargsHolder
    var srcjar: Label? by kwargsHolder

    var deps: List<Label?>? by kwargsHolder
    var visibility: List<Label?>? by kwargsHolder
}


open class AndroidSdkRepositoryContext : FunctionCallContext() {
    var name: Name by kwargsHolder
    var api_level: Int? by kwargsHolder
    var build_tools_version: CharSequence? by kwargsHolder
    var path: CharSequence? by kwargsHolder
    var repo_mapping: Map<String, CharSequence?>? by kwargsHolder
}


open class AndroidNdkRepositoryContext : FunctionCallContext() {
    var name: Name by kwargsHolder
    var api_level: Int? by kwargsHolder
    var path: CharSequence? by kwargsHolder
    var repo_mapping: Map<String, CharSequence?>? by kwargsHolder
}