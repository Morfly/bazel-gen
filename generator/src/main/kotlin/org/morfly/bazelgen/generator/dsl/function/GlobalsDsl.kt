@file:Suppress("FunctionName")

package org.morfly.bazelgen.generator.dsl.function

import org.morfly.bazelgen.generator.dsl.Label
import org.morfly.bazelgen.generator.dsl.WorkspaceContext


/**
 *
 */
fun WorkspaceContext.register_toolchains(vararg toolchains: Label?) =
    functionCallStatement("register_toolchains", mapOf("" to toolchains))

/**
 *
 */
fun WorkspaceContext.workspace(name: String, managed_directories: Map<String, List<String>>? = null) =
    functionCallStatement(
        "workspace", mapOf(
            "name" to name,
            "managed_directories" to managed_directories
        )
    )