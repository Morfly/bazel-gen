@file:Suppress("FunctionName", "PropertyName")

package org.morfly.bazelgen.generator.dsl.rule

import org.morfly.bazelgen.generator.dsl.Label
import org.morfly.bazelgen.generator.dsl.Name
import org.morfly.bazelgen.generator.dsl.WorkspaceContext
import org.morfly.bazelgen.generator.dsl.feature.FunctionCallContext


/**
 *
 */
fun WorkspaceContext.bind(body: BindContext.() -> Unit) =
    functionCallStatement("bind", BindContext(), body)

/**
 *
 */
fun WorkspaceContext.local_repository(body: LocalRepositoryContext.() -> Unit) =
    functionCallStatement("local_repository", LocalRepositoryContext(), body)

/**
 *
 */
fun WorkspaceContext.new_local_repository(body: NewLocalRepositoryContext.() -> Unit) =
    functionCallStatement("new_local_repository", NewLocalRepositoryContext(), body)


open class BindContext : FunctionCallContext() {
    var name: Name by kwargsHolder
    var actual: Label? by kwargsHolder

    var visibility: List<Label?>? by kwargsHolder
}


open class LocalRepositoryContext : FunctionCallContext() {
    var name: Name by kwargsHolder
    var path: String by kwargsHolder
    var repo_mapping: Map<String, CharSequence?>? by kwargsHolder
}


open class NewLocalRepositoryContext : FunctionCallContext() {
    var name: Name by kwargsHolder
    var build_file: CharSequence? by kwargsHolder
    var build_file_content: CharSequence? by kwargsHolder
    var path: CharSequence? by kwargsHolder
    var repo_mapping: Map<String, CharSequence?>? by kwargsHolder
    var workspace_file: CharSequence? by kwargsHolder
    var workspace_file_content: CharSequence? by kwargsHolder
}