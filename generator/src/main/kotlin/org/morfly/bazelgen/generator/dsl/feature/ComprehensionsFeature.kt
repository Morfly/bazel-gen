package org.morfly.bazelgen.generator.dsl.feature

import org.morfly.bazelgen.generator.buildfile.BuildStatement
import org.morfly.bazelgen.generator.buildfile.ComprehensionStatement
import org.morfly.bazelgen.generator.buildfile.ExpressionStatement
import org.morfly.bazelgen.generator.buildfile.RawTextStatement
import org.morfly.bazelgen.generator.dsl.StarlarkLanguageFeature


/**
 *
 */
internal typealias DictComprehensionIdentifier = (() -> Unit)?


/**
 *
 */
internal interface ComprehensionsFeature : StarlarkLanguageFeature {

    /**
     *
     */
    operator fun CharSequence.invoke(
        `for`: String, `in`: Any, `if`: String? = null,
        dict: DictComprehensionIdentifier = null
    ): ComprehensionStatement =
        ExpressionStatement(this)(`for`, `in`, `if`, dict)


    /**
     *
     */
    operator fun BuildStatement.invoke(
        `for`: String, `in`: Any, `if`: String? = null,
        dict: DictComprehensionIdentifier = null
    ): ComprehensionStatement
}