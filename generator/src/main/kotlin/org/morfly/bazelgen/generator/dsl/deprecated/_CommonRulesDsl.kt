@file:Suppress("PropertyName", "SpellCheckingInspection")

package org.morfly.bazelgen.generator.dsl.deprecated

import org.morfly.bazelgen.generator.dsl.Feature
import org.morfly.bazelgen.generator.dsl.Label
import org.morfly.bazelgen.generator.dsl.feature.FunctionCallContext
import org.morfly.bazelgen.generator.dsl.feature.VisibilityFeature

@Deprecated("")
open class BuildRuleContext : FunctionCallContext(), VisibilityFeature {
    var data: List<Label>? by kwargsHolder
    var visibility: List<Label>? by kwargsHolder
    var toolchains: List<Label>? by kwargsHolder
    var deps: List<Label>? by kwargsHolder
    var deprecation: String? by kwargsHolder
    var tags: List<String>? by kwargsHolder
    var testonly: Boolean? by kwargsHolder
    var features: List<Feature>? by kwargsHolder
    var licenses: List<String>? by kwargsHolder
    var compatible_with: List<Label>? by kwargsHolder
    var exec_properties: Map<String, String?>? by kwargsHolder
    var distribs: List<String>? by kwargsHolder
    var target_compatible_with: List<Label>? by kwargsHolder
    var exec_compatible_with: List<Label>? by kwargsHolder
    var restricted_to: List<Label>? by kwargsHolder
}