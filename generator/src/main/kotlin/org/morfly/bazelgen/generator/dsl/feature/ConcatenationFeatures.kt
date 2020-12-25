@file:Suppress("FunctionName")

package org.morfly.bazelgen.generator.dsl.feature

import org.morfly.bazelgen.generator.buildfile.AssignmentStatement
import org.morfly.bazelgen.generator.buildfile.ConcatenationStatement
import org.morfly.bazelgen.generator.dsl.StarlarkLanguageFeature
import org.morfly.bazelgen.generator.dsl.type.DictionaryConcatenatedValue
import org.morfly.bazelgen.generator.dsl.type.ListConcatenatedValue
import org.morfly.bazelgen.generator.dsl.type.StringConcatenatedValue
import org.morfly.bazelgen.generator.dsl.type.ValueAssignment


/**
 *
 */
internal interface BaseConcatenationFeature : StarlarkLanguageFeature {

    /**
     *
     */
    infix fun CharSequence.`+`(other: CharSequence?): CharSequence =
        StringConcatenatedValue(this, "+", other)

    /**
     *
     */
    infix fun <T> List<T>.`+`(other: List<T>?): List<T> =
        ListConcatenatedValue(this, "+", other)

    /**
     *
     */
    infix fun Map<String, Any?>.`+`(other: Map<String, Any?>?): Map<String, Any?> =
        DictionaryConcatenatedValue(this, "+", other)


    /**
     *
     */
    infix fun StringConcatenatedValue.`+`(other: CharSequence?): CharSequence =
        StringConcatenatedValue(this, "+", other)

    /**
     *
     */
    infix fun <T> ListConcatenatedValue<T>.`+`(other: List<T>?): List<T> =
        ListConcatenatedValue(this, "+", other)

    /**
     *
     */
    infix fun DictionaryConcatenatedValue.`+`(other: Map<String, Any?>?): Map<String, Any?> =
        DictionaryConcatenatedValue(this, "+", other)
}


/**
 *
 */
internal interface StatementConcatenationFeature : BaseConcatenationFeature {


    /**
     *
     */
    infix fun <T> AssignmentStatement<T>.`+`(other: Any?): ConcatenationStatement

    /**
     *
     */
    infix fun ConcatenationStatement.`+`(other: Any?): ConcatenationStatement
}

/**
 *
 */
internal interface ArgumentConcatenationFeature : BaseConcatenationFeature {

    /**
     *
     */
    infix fun ValueAssignment<CharSequence>.`+`(other: CharSequence?): CharSequence

    /**
     *
     */
    infix fun <T> ValueAssignment<List<T>>.`+`(other: List<T>?): List<T>

    /**
     *
     */
    infix fun ValueAssignment<Map<String, Any?>>.`+`(other: Map<String, Any?>?): Map<String, Any?>
}