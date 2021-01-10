package org.morfly.bazelgen.generator.dsl.feature

import org.morfly.bazelgen.generator.dsl.StarlarkDslFeature
import org.morfly.bazelgen.generator.dsl.core.element.*


/**
 *
 */
internal interface FormattingFeature : StarlarkDslFeature {

    /**
     *
     */
    infix fun CharSequence.`%`(other: CharSequence?): StringConcatenation =
        StringConcatenation(left = this, operator = "%", right = other)

    /**
     *
     */
    infix fun <T> List<T>.`%`(other: List<T>?): ListConcatenation<T> =
        ListConcatenation(left = this, operator = "%", right = other)

    /**
     *
     */
    infix fun Map<String, Any?>.`%`(other: Map<String, Any?>?): DictionaryConcatenation =
        DictionaryConcatenation(left = this, operator = "%", right = other)

    /**
     *
     */
    infix fun Any.`%`(other: Any?): AnyConcatenation<Expression, Expression> =
        AnyConcatenation(left = this, operator = "%", right = other)
}