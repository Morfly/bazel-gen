@file:Suppress("PropertyName")

package org.morfly.bazelgen.generator.dsl.function

import org.morfly.bazelgen.analyzer.RelativePath
import org.morfly.bazelgen.generator.dsl.BuildContext
import org.morfly.bazelgen.generator.dsl.Feature
import org.morfly.bazelgen.generator.dsl.Label
import org.morfly.bazelgen.generator.dsl.Name
import org.morfly.bazelgen.generator.dsl.feature.FunctionCallContext


/**
 *
 */
fun BuildContext.alias(body: AliasContext.() -> Unit) =
    functionCallStatement("alias", AliasContext(), body)

/**
 *
 */
fun BuildContext.filegroup(body: FilegroupContext.() -> Unit) =
    functionCallStatement("filegroup", FilegroupContext(), body)

/**
 *
 */
fun BuildContext.genrule(body: GenruleContext.() -> Unit) =
    functionCallStatement("genrule", GenruleContext(), body)


open class AliasContext : FunctionCallContext() {
    var name: Name by kwargsHolder
    var actual: Label by kwargsHolder

    var compatible_with: List<Label>? by kwargsHolder
    var deprecation: CharSequence? by kwargsHolder
    var features: List<Feature>? by kwargsHolder
    var restricted_to: List<Label>? by kwargsHolder
    var tags: List<String>? by kwargsHolder
    var testonly: Boolean? by kwargsHolder
    var visibility: List<Label>? by kwargsHolder
}


open class FilegroupContext : FunctionCallContext() {
    var name: Name by kwargsHolder
    var srcs: List<Label?>? by kwargsHolder
    var data: List<Label?>? by kwargsHolder
    var output_group: CharSequence? by kwargsHolder

    var compatible_with: List<Label?>? by kwargsHolder
    var deprecation: CharSequence? by kwargsHolder
    var distribs: List<CharSequence?>? by kwargsHolder
    var features: List<Feature?>? by kwargsHolder
    var licenses: List<CharSequence?>? by kwargsHolder
    var restricted_to: List<Label?>? by kwargsHolder
    var tags: List<CharSequence?>? by kwargsHolder
    var testonly: Boolean? by kwargsHolder
    var visibility: List<Label?>? by kwargsHolder
}


open class GenruleContext : FunctionCallContext() {
    var name: Name by kwargsHolder
    var srcs: List<Label?>? by kwargsHolder
    var outs: List<RelativePath?>? by kwargsHolder
    var cmd: CharSequence? by kwargsHolder
    var cmd_bash: CharSequence? by kwargsHolder
    var cmd_bat: CharSequence? by kwargsHolder
    var cmd_ps: CharSequence? by kwargsHolder
    var exec_tools: List<Label?>? by kwargsHolder
    var executable: Boolean? by kwargsHolder
    var local: Boolean? by kwargsHolder
    var message: CharSequence? by kwargsHolder
    var output_licenses: List<CharSequence?>? by kwargsHolder
    var output_to_bindir: Boolean? by kwargsHolder
    var tools: List<Label?>? by kwargsHolder

    var visibility: List<Label?>? by kwargsHolder
}