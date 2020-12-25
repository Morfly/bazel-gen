package org.morfly.bazelgen.generator.dsl.type

import org.morfly.bazelgen.generator.buildfile.Concatenation


/**
 *
 */
sealed class ConcatenatedValue(
    override val left: Any,
    override val operator: String,
    override val right: Any?
) : Concatenation<Any>, StarlarkDslType

/**
 *
 */
data class StringConcatenatedValue(
    override val left: Any,
    override val operator: String,
    override val right: Any?
) : ConcatenatedValue(left, operator, right), CharSequence by "$left $operator $right"

/**
 *
 */
data class ListConcatenatedValue<T>(
    override val left: Any,
    override val operator: String,
    override val right: Any?
) : ConcatenatedValue(left, operator, right), List<T> by emptyList()

/**
 *
 */
data class DictionaryConcatenatedValue(
    override val left: Any,
    override val operator: String,
    override val right: Any?
) : ConcatenatedValue(left, operator, right), Map<String, Any?> by emptyMap()

/**
 *
 */
data class AnyConcatenatedValue(
    override val left: Any,
    override val operator: String,
    override val right: Any?
) : ConcatenatedValue(left, operator, right)