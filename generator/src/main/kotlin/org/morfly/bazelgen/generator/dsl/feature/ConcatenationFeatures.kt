@file:Suppress("FunctionName")

package org.morfly.bazelgen.generator.dsl.feature

import org.morfly.bazelgen.generator.dsl.StarlarkDslFeature
import org.morfly.bazelgen.generator.dsl.core.AssignmentStatement
import org.morfly.bazelgen.generator.dsl.core.ExpressionStatement
import org.morfly.bazelgen.generator.dsl.core.element.*


/**
 *
 */
internal interface BaseConcatenationFeature : StarlarkDslFeature {

    /**
     *
     */
    infix fun CharSequence.`+`(other: CharSequence?): CharSequence =
        StringConcatenation(this, "+", other)

    /**
     *
     */
    infix fun <T> List<T>.`+`(other: List<T>?): List<T> =
        ListConcatenation(this, "+", other)

    /**
     *
     */
    infix fun Map<String, Any?>.`+`(other: Map<String, Any?>?): Map<String, Any?> =
        DictionaryConcatenation(this, "+", other)


    /**
     *
     */
    infix fun StringConcatenation.`+`(other: CharSequence?): CharSequence =
        StringConcatenation(this, "+", other)

    /**
     *
     */
    infix fun <T> ListConcatenation<T>.`+`(other: List<T>?): List<T> =
        ListConcatenation(this, "+", other)

    /**
     *
     */
    infix fun DictionaryConcatenation.`+`(other: Map<String, Any?>?): Map<String, Any?> =
        DictionaryConcatenation(this, "+", other)
}


/**
 *
 */
internal interface StatementConcatenationFeature : BaseConcatenationFeature {

    /**
     *
     */
    infix fun AssignmentStatement<CharSequence>.`+`(other: CharSequence?): AssignmentStatement<CharSequence>

    /**
     *
     */
    infix fun <T> AssignmentStatement<List<T>>.`+`(other: List<T>?): AssignmentStatement<List<T>>

    /**
     *
     */
    infix fun AssignmentStatement<Map<String, Any?>>.`+`(other: Map<String, Any?>?): AssignmentStatement<Map<String, Any?>>

    /**
     *
     */
    infix fun <T> AssignmentStatement<T>.`+`(other: ExpressionStatement<T>): AssignmentStatement<Concatenation<T?, T?>>
}

/**
 *
 */
internal interface ArgumentConcatenationFeature : BaseConcatenationFeature {

    /**
     *
     */
    infix fun ExpressionAssignment<CharSequence>.`+`(other: CharSequence?): CharSequence

    /**
     *
     */
    infix fun <T> ExpressionAssignment<List<T>>.`+`(other: List<T>?): List<T>

    /**
     *
     */
    infix fun ExpressionAssignment<Map<String, Any?>>.`+`(other: Map<String, Any?>?): Map<String, Any?>
}