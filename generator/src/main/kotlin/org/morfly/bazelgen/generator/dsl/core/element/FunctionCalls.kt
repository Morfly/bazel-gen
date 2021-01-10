package org.morfly.bazelgen.generator.dsl.core.element

import org.morfly.bazelgen.generator.dsl.StarlarkContext


/**
 *
 */
sealed class FunctionCall(
    val name: String,
    args: Map<String, Any?>
) : Expression(), StarlarkDslElement {
    val args = args.filterNotNullValues()
}

/**
 *
 */
@Suppress("UNCHECKED_CAST")
fun Map<String, Any?>.filterNotNullValues(): Map<String, Any> =
    this.filterValues { it != null }
        .mapValues { (_, value) ->
            when (value) {
                is StarlarkDslElement -> value
                is List<*> -> value.filterNotNull()
                else -> value
            }
        } as Map<String, Any>

/**
 *
 */
class StringFunctionCall(
    name: String,
    args: Map<String, Any?>
) : FunctionCall(name, args), CharSequence by name


/**
 *
 */
class ListFunctionCall<T>(
    name: String,
    args: Map<String, Any?>
) : FunctionCall(name, args), List<T> by emptyList()

/**
 *
 */
class DictionaryFunctionCall(
    name: String,
    args: Map<String, Any?>
) : FunctionCall(name, args), Map<String, Any?> by emptyMap()

/**
 *
 */
class AnyFunctionCall(
    name: String,
    args: Map<String, Any?>
) : FunctionCall(name, args)

/**
 *
 */
typealias VoidFunctionCall = AnyFunctionCall

/**
 *
 */
inline fun <reified T> StarlarkContext.pureFunctionCall(name: String, args: Map<String, Any?>): T =
    when {
        CharSequence::class.java.isAssignableFrom(T::class.java) -> StringFunctionCall(name, args) as T
        List::class.java.isAssignableFrom(T::class.java) -> ListFunctionCall<Any?>(name, args) as T
        Map::class.java.isAssignableFrom(T::class.java) -> DictionaryFunctionCall(name, args) as T
        else -> AnyFunctionCall(name, args) as T
    }