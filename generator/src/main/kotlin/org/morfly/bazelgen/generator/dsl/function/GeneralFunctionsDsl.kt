@file:Suppress("FunctionName")

package org.morfly.bazelgen.generator.dsl.function

import org.morfly.bazelgen.generator.dsl.Label
import org.morfly.bazelgen.generator.dsl.StarlarkContext
import org.morfly.bazelgen.generator.dsl.core.element.pureFunctionCall


/**
 *
 */
fun StarlarkContext.glob(
    vararg include: Label?,
    exclude: List<Label>? = null,
    exclude_directories: Int? = null,
    allow_empty: Boolean? = null
) = glob(include.toList(), exclude, exclude_directories, allow_empty)

/**
 *
 */
fun StarlarkContext.glob(
    include: List<Label?>,
    exclude: List<Label>? = null,
    exclude_directories: Int? = null,
    allow_empty: Boolean? = null
) = pureFunctionCall<List<Label>>(
    name = "glob",
    args = mapOf(
        "" to include,
        "exclude" to exclude,
        "exclude_directories" to exclude_directories,
        "allow_empty" to allow_empty
    )
)

/**
 *
 */
fun StarlarkContext.`package`(
    default_visibility: List<Label>? = null,
    default_deprecation: CharSequence? = null,
    default_testonly: Boolean? = null,
    features: List<String>? = null
) = functionCallStatement(
    name = "package",
    args = mapOf(
        "default_visibility" to default_visibility,
        "default_deprecation" to default_deprecation,
        "default_testonly" to default_testonly,
        "features" to features
    )
)

/**
 *
 */
fun StarlarkContext.exports_files(
    vararg exports_files: Label,
    visibility: List<Label>? = null,
    licences: List<CharSequence>? = null
) = exports_files(exports_files.toList(), visibility, licences)

/**
 *
 */
fun StarlarkContext.exports_files(
    exports_files: List<Label>,
    visibility: List<Label>? = null,
    licences: List<CharSequence>? = null
) = functionCallStatement(
    name = "exports_files",
    args = mapOf(
        "" to exports_files,
        "visibility" to visibility,
        "licences" to licences,
    )
)

/**
 *
 */
inline fun <reified T> StarlarkContext.select(
    select: Map<String, Any?>,
    no_match_error: CharSequence? = null
) = pureFunctionCall<T>(
    name = "select",
    args = mapOf(
        "" to select,
        "no_match_error" to no_match_error
    )
)