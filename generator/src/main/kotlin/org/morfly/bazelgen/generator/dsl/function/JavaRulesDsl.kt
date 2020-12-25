@file:Suppress("PropertyName", "FunctionName")

package org.morfly.bazelgen.generator.dsl.function

import org.morfly.bazelgen.generator.dsl.BuildContext
import org.morfly.bazelgen.generator.dsl.Label
import org.morfly.bazelgen.generator.dsl.Name
import org.morfly.bazelgen.generator.dsl.feature.FunctionCallContext


/**
 *
 */
fun BuildContext.java_library(body: JavaLibraryContext.() -> Unit) =
    functionCallStatement("java_library", JavaLibraryContext(), body)

/**
 *
 */
fun BuildContext.java_binary(body: JavaBinaryContext.() -> Unit) =
    functionCallStatement("java_binary", JavaBinaryContext(), body)

/**
 *
 */
fun BuildContext.java_import(body: JavaImportContext.() -> Unit) =
    functionCallStatement("java_import", JavaImportContext(), body)

/**
 *
 */
fun BuildContext.java_plugin(body: JavaPluginContext.() -> Unit) =
    functionCallStatement("java_plugin", JavaPluginContext(), body)


open class JavaLibraryContext : FunctionCallContext() {
    var name: Name by kwargsHolder
    var srcs: List<Label?>? by kwargsHolder
    var resources: List<Label?>? by kwargsHolder
    var exports: List<Label?>? by kwargsHolder
    var plugins: List<Label?>? by kwargsHolder

    var deps: List<Label?>? by kwargsHolder
    var visibility: List<Label?>? by kwargsHolder
}


open class JavaBinaryContext : FunctionCallContext() {
    var name: Name by kwargsHolder
    var srcs: List<Label?>? by kwargsHolder
    var resources: List<Label?>? by kwargsHolder
    var exports: List<Label?>? by kwargsHolder
    var plugins: List<Label?>? by kwargsHolder
    var main_class: CharSequence? by kwargsHolder

    var deps: List<Label?>? by kwargsHolder
    var visibility: List<Label?>? by kwargsHolder

    var args: List<CharSequence?>? by kwargsHolder
    var env: Map<String, CharSequence?>? by kwargsHolder
    var output_licenses: List<CharSequence?>? by kwargsHolder
}


open class JavaImportContext : FunctionCallContext() {
    var name: Name by kwargsHolder
    var jars: List<Label?>? by kwargsHolder
    var exports: List<Label?>? by kwargsHolder

    var deps: List<Label?>? by kwargsHolder
    var visibility: List<Label?>? by kwargsHolder
    var neverlink: Boolean? by kwargsHolder
}


open class JavaPluginContext : FunctionCallContext() {
    var name: Name by kwargsHolder
    var processor_class: CharSequence? by kwargsHolder
    var generates_api: Boolean? by kwargsHolder

    var deps: List<Label?>? by kwargsHolder
    var visibility: List<Label?>? by kwargsHolder
}