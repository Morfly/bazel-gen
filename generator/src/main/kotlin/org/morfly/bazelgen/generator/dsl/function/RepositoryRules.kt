@file:Suppress("PropertyName", "FunctionName")

package org.morfly.bazelgen.generator.dsl.function

import org.morfly.bazelgen.generator.dsl.Label
import org.morfly.bazelgen.generator.dsl.Name
import org.morfly.bazelgen.generator.dsl.StarlarkContext
import org.morfly.bazelgen.generator.dsl.feature.FunctionCallContext


/**
 *
 */
fun StarlarkContext.http_archive(body: HttpArchiveContext.() -> Unit) =
    functionCallStatement("http_archive", HttpArchiveContext(), body)

/**
 *
 */
fun StarlarkContext.http_file(body: HttpFileContext.() -> Unit) =
    functionCallStatement("http_file", HttpFileContext(), body)

/**
 *
 */
fun StarlarkContext.http_jar(body: HttpJarContext.() -> Unit) =
    functionCallStatement("http_jar", HttpJarContext(), body)

/**
 *
 */
fun StarlarkContext.git_repository(body: GitRepositoryContext.() -> Unit) =
    functionCallStatement("git_repository", GitRepositoryContext(), body)

/**
 *
 */
fun StarlarkContext.new_git_repository(body: NewGitRepositoryContext.() -> Unit) =
    functionCallStatement("new_git_repository", NewGitRepositoryContext(), body)


open class HttpArchiveContext : FunctionCallContext() {
    var name: Name by kwargsHolder
    var build_file: CharSequence? by kwargsHolder
    var build_file_content: CharSequence? by kwargsHolder
    var auth_patterns: Map<String, CharSequence?>? by kwargsHolder
    var canonical_id: CharSequence? by kwargsHolder
    var netrc: CharSequence? by kwargsHolder
    var patch_args: List<CharSequence?>? by kwargsHolder
    var patch_cmds: List<CharSequence?>? by kwargsHolder
    var patch_cmds_win: List<CharSequence?>? by kwargsHolder
    var patch_tool: CharSequence? by kwargsHolder
    var patches: List<Label?>? by kwargsHolder
    var sha256: CharSequence? by kwargsHolder
    var strip_prefix: CharSequence? by kwargsHolder
    var type: CharSequence? by kwargsHolder
    var url: CharSequence? by kwargsHolder
    var urls: List<Label?>? by kwargsHolder
    var workspace_file: CharSequence? by kwargsHolder
    var workspace_file_content: CharSequence? by kwargsHolder
}


open class HttpFileContext : FunctionCallContext() {
    var name: Name by kwargsHolder
    var auth_patterns: Map<String, CharSequence?>? by kwargsHolder
    var canonical_id: CharSequence? by kwargsHolder
    var downloaded_file_path: CharSequence? by kwargsHolder
    var executable: Boolean? by kwargsHolder
    var netrc: CharSequence? by kwargsHolder
    var sha256: CharSequence? by kwargsHolder
    var urls: List<Label?>? by kwargsHolder
}


open class HttpJarContext : FunctionCallContext() {
    var name: Name by kwargsHolder
    var auth_patterns: Map<String, CharSequence?>? by kwargsHolder
    var canonical_id: CharSequence? by kwargsHolder
    var netrc: CharSequence? by kwargsHolder
    var sha256: CharSequence? by kwargsHolder
    var url: List<Label?>? by kwargsHolder
    var urls: List<Label?>? by kwargsHolder
}


open class GitRepositoryContext : FunctionCallContext() {

}


open class NewGitRepositoryContext : FunctionCallContext() {

}