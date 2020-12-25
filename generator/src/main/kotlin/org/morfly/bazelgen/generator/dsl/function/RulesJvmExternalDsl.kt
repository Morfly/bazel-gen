@file:Suppress("PropertyName", "FunctionName")

package org.morfly.bazelgen.generator.dsl.function

import org.morfly.bazelgen.generator.dsl.Name
import org.morfly.bazelgen.generator.dsl.StarlarkContext
import org.morfly.bazelgen.generator.dsl.feature.FunctionCallContext


/**
 *
 */
fun StarlarkContext.maven_install(body: MavenInstallContext.() -> Unit) =
    functionCallStatement("maven_install", MavenInstallContext(), body)

/**
 *
 */
fun StarlarkContext.artifact(a: CharSequence) = with(toString()) {
    if (startsWith("artifact")) this else "artifact(${a.quoted})"
}


open class MavenInstallContext : FunctionCallContext() {
    var name: Name by kwargsHolder
    var artifacts: List<CharSequence?>? by kwargsHolder
    var repositories: List<CharSequence?>? by kwargsHolder
    var fail_on_missing_checksum: Boolean? by kwargsHolder
    var fetch_sources: Boolean? by kwargsHolder
    var excluded_artifacts: List<CharSequence?>? by kwargsHolder
    var generate_compat_repositories: Boolean? by kwargsHolder
    var strict_visibility: Boolean? by kwargsHolder
    var jetify: Boolean? by kwargsHolder
    var jetify_include_list: List<CharSequence?>? by kwargsHolder
}