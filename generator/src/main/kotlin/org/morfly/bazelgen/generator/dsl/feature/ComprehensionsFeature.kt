package org.morfly.bazelgen.generator.dsl.feature


import org.morfly.bazelgen.generator.dsl.StarlarkDslFeature
import org.morfly.bazelgen.generator.dsl.core.BuildStatement
import org.morfly.bazelgen.generator.dsl.core.ExpressionStatement
import org.morfly.bazelgen.generator.dsl.core.element.Comprehension


/**
 *
 */
internal typealias DictComprehensionIdentifier = (() -> Unit)?


/**
 *
 */
internal interface ComprehensionsFeature : StarlarkDslFeature {

    /**
     *
     */
    operator fun CharSequence.invoke(
        `for`: String, `in`: Any, `if`: String? = null,
        dict: DictComprehensionIdentifier = null
    ) = ExpressionStatement(this)(`for`, `in`, `if`, dict)


    /**
     *
     */
    operator fun BuildStatement.invoke(
        `for`: String, `in`: Any, `if`: String? = null,
        dict: DictComprehensionIdentifier = null
    ): ExpressionStatement<Comprehension>
}