package org.morfly.bazelgen.generator.dsl.feature

import org.morfly.bazelgen.generator.dsl.StarlarkLanguageFeature
import org.morfly.bazelgen.generator.dsl.type.AnyConcatenatedValue
import org.morfly.bazelgen.generator.dsl.type.DictionaryConcatenatedValue
import org.morfly.bazelgen.generator.dsl.type.ListConcatenatedValue
import org.morfly.bazelgen.generator.dsl.type.StringConcatenatedValue


/**
 *
 */
internal interface FormattingFeature : StarlarkLanguageFeature {

    /**
     *
     */
    infix fun CharSequence.`%`(other: Any?): StringConcatenatedValue =
        StringConcatenatedValue(left = this, operator = "%", right = other)

    /**
     *
     */
    infix fun <T> List<T>.`%`(other: Any?): ListConcatenatedValue<T> =
        ListConcatenatedValue(left = this, operator = "%", right = other)

    /**
     *
     */
    infix fun Map<String, Any?>.`%`(other: Any?): DictionaryConcatenatedValue =
        DictionaryConcatenatedValue(left = this, operator = "%", right = other)

    /**
     *
     */
    infix fun Any.`%`(other: Any?): AnyConcatenatedValue =
        AnyConcatenatedValue(left = this, operator = "%", right = other)
}