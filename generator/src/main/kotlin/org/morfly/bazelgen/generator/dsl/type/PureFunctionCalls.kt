package org.morfly.bazelgen.generator.dsl.type

import org.morfly.bazelgen.generator.buildfile.FunctionCall
import org.morfly.bazelgen.generator.dsl.StarlarkContext


/**
 *
 */
sealed class PureFunctionCall(
    override val name: String,
    args: Map<String, Any?>
) : FunctionCall, StarlarkDslType {
    override val args = args.filterNotNullValues()
}

/**
 *
 */
@Suppress("UNCHECKED_CAST")
fun Map<String, Any?>.filterNotNullValues(): Map<String, Any> =
    this.filterValues { it != null }
        .mapValues { (_, value) ->
            when (value) {
                is StarlarkDslType -> value
                is List<*> -> value.filterNotNull()
                else -> value
            }
        } as Map<String, Any>

/**
 *
 */
class ListFunctionCall<T>(
    name: String,
    args: Map<String, Any?>
) : PureFunctionCall(name, args), List<T> by emptyList()

/**
 *
 */
class DictionaryFunctionCall(
    name: String,
    args: Map<String, Any?>
) : PureFunctionCall(name, args), Map<String, Any> by emptyMap()

/**
 *
 */
class StringFunctionCall(
    name: String,
    args: Map<String, Any?>
) : PureFunctionCall(name, args), CharSequence by name

/**
 *
 */
class AnyFunctionCall(
    name: String,
    args: Map<String, Any?>
) : PureFunctionCall(name, args)


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