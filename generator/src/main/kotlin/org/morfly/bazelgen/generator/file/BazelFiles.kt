package org.morfly.bazelgen.generator.file

import org.morfly.bazelgen.analyzer.RelativePath
import org.morfly.bazelgen.generator.dsl.core.BuildStatement
import org.morfly.bazelgen.generator.dsl.core.RawTextStatement


/**
 *
 */
sealed class BazelFile(
    val name: String,
    val relativePath: RelativePath,
    val statements: List<BuildStatement>
)

/**
 *
 */
class BazelWorkspace(
    hasExtension: Boolean,
    statements: List<BuildStatement>
) : BazelFile(
    name = "WORKSPACE" + if (hasExtension) ".bazel" else "",
    relativePath = "",
    statements
)

/**
 *
 */
class BazelBuild(
    hasExtension: Boolean,
    relativePath: RelativePath,
    statements: List<BuildStatement>
) : BazelFile(
    name = "BUILD" + if (hasExtension) ".bazel" else "",
    relativePath,
    statements
)

/**
 *
 */
data class BazelVersion(
    val major: String,
    val minor: String,
    val patch: String
) : BazelFile(
    name = ".bazelversion",
    relativePath = "",
    statements = listOf(RawTextStatement("$major.$minor.$patch"))
)

/**
 *
 */
class BazelRc(
    statements: List<BuildStatement>,
    namePrefix: String = "",
    relativePath: RelativePath = ""
) : BazelFile(
    name = "$namePrefix.bazelrc",
    relativePath = relativePath,
    statements
)

/**
 *
 */
class GenericBazelFile(
    name: String,
    relativePath: RelativePath,
    statements: List<BuildStatement>
) : BazelFile(name, relativePath, statements)