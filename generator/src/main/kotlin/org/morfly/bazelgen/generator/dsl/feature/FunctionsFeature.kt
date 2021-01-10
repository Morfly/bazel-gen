package org.morfly.bazelgen.generator.dsl.feature

import org.morfly.bazelgen.generator.dsl.StarlarkDslFeature
import org.morfly.bazelgen.generator.dsl.StarlarkFeatureContext
import org.morfly.bazelgen.generator.dsl.core.ExpressionStatement
import org.morfly.bazelgen.generator.dsl.core.element.*


/**
 *
 */
internal interface FunctionsFeature : StarlarkDslFeature {

    /**
     *
     */
    operator fun String.invoke(body: FunctionCallContext.() -> Unit): ExpressionStatement<FunctionCall>
}


/**
 *
 */
@StarlarkFeatureContext
open class FunctionCallContext : AssignmentsFeature, ArgumentConcatenationFeature {

    protected val kwargsHolder = linkedMapOf<String, Any?>()

    /**
     *
     */
    val kwargs: Map<String, Any?> get() = kwargsHolder


    override fun String.`=`(value: Any): ExpressionAssignment<Any> =
        ExpressionAssignment(this, value)
            .also { (name, value) -> kwargsHolder[name] = value }

    override fun <T> String.`=`(value: List<T>): ExpressionAssignment<List<T>> =
        ExpressionAssignment(this, value)
            .also { (name, value) -> kwargsHolder[name] = value }

    override fun String.`=`(body: DictionaryContext.() -> Unit): ExpressionAssignment<Map<String, Any?>> =
        ExpressionAssignment(
            name = this,
            value = DictionaryContext()
                .apply(body)
                .kwargs
                .also { kwargsHolder[this] = it }
        )


    override fun ExpressionAssignment<CharSequence>.`+`(other: CharSequence?): CharSequence =
        StringConcatenation(value, "+", other)
            .also { kwargsHolder[name] = it }

    override fun <T> ExpressionAssignment<List<T>>.`+`(other: List<T>?): List<T> =
        ListConcatenation(value, "+", other)
            .also { kwargsHolder[name] = it }

    override fun ExpressionAssignment<Map<String, Any?>>.`+`(other: Map<String, Any?>?): Map<String, Any?> =
        DictionaryConcatenation(value, "+", other)
            .also { kwargsHolder[name] = it }


    override fun StringConcatenation.`+`(other: CharSequence?): CharSequence =
        concatenate { StringConcatenation(it, "+", other) }

    override fun <T> ListConcatenation<T>.`+`(other: List<T>?): List<T> =
        concatenate { ListConcatenation(it, "+", other) }

    override fun DictionaryConcatenation.`+`(other: Map<String, Any?>?): Map<String, Any?> =
        concatenate { DictionaryConcatenation(it, "+", other) }


    private inline fun <V, reified T> V.concatenate(newValue: (oldValue: T) -> T): T {
        val (name, value) = kwargsHolder.entries
            .lastOrNull()
            ?.takeIf { (_, value) -> value == this }
            ?.let { (name, value) -> name to value }
            ?: null to this

        return newValue(value as T)
            .also { if (name != null) kwargsHolder[name] = it }
    }
}