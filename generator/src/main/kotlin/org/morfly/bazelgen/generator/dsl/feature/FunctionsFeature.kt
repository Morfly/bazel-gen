package org.morfly.bazelgen.generator.dsl.feature

import org.morfly.bazelgen.generator.buildfile.RuleStatement
import org.morfly.bazelgen.generator.dsl.StarlarkFeatureContext
import org.morfly.bazelgen.generator.dsl.StarlarkLanguageFeature
import org.morfly.bazelgen.generator.dsl.type.DictionaryConcatenatedValue
import org.morfly.bazelgen.generator.dsl.type.ListConcatenatedValue
import org.morfly.bazelgen.generator.dsl.type.StringConcatenatedValue
import org.morfly.bazelgen.generator.dsl.type.ValueAssignment


/**
 *
 */
internal interface FunctionsFeature : StarlarkLanguageFeature {

    /**
     *
     */
    operator fun String.invoke(body: FunctionCallContext.() -> Unit): RuleStatement
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


    override fun String.`=`(value: Any): ValueAssignment<Any> =
        ValueAssignment(this, value)
            .also { (name, value) -> kwargsHolder[name] = value }

    override fun <T> String.`=`(value: List<T>): ValueAssignment<List<T>> =
        ValueAssignment(this, value)
            .also { (name, value) -> kwargsHolder[name] = value }

    override fun String.`=`(body: DictionaryContext.() -> Unit): ValueAssignment<Map<String, Any?>> =
        ValueAssignment(
            name = this,
            value = DictionaryContext()
                .apply(body)
                .kwargs
                .also { kwargsHolder[this] = it }
        )


    override fun ValueAssignment<CharSequence>.`+`(other: CharSequence?): CharSequence =
        StringConcatenatedValue(value, "+", other)
            .also { kwargsHolder[name] = it }

    override fun <T> ValueAssignment<List<T>>.`+`(other: List<T>?): List<T> =
        ListConcatenatedValue<T>(value, "+", other)
            .also { kwargsHolder[name] = it }

    override fun ValueAssignment<Map<String, Any?>>.`+`(other: Map<String, Any?>?): Map<String, Any?> =
        DictionaryConcatenatedValue(value, "+", other)
            .also { kwargsHolder[name] = it }


    override fun StringConcatenatedValue.`+`(other: CharSequence?): CharSequence {
//        val (name, value) = kwargsHolder.entries.last()
//        return StringConcatenatedValue(value!!, "+", other)
//            .also { kwargsHolder[name] = it }
        return concatenate { StringConcatenatedValue(it, "+", other) }
    }

    override fun <T> ListConcatenatedValue<T>.`+`(other: List<T>?): List<T> {
//        val (name, value) = kwargsHolder.entries.last()
//        return ListConcatenatedValue<T>(value!!, "+", other)
//            .also { kwargsHolder[name] = it }
        return concatenate { ListConcatenatedValue(it, "+", other) }
    }

    override fun DictionaryConcatenatedValue.`+`(other: Map<String, Any?>?): Map<String, Any?> {
//        val (name, value) = kwargsHolder.entries.last()
//        return DictionaryConcatenatedValue(value!!, "+", other)
//            .also { kwargsHolder[name] = it }
        return concatenate { DictionaryConcatenatedValue(it, "+", other) }
    }

    private fun <V, T> V.concatenate(newValue: (oldValue: Any) -> T): T {
        val (name, value) = kwargsHolder.entries
            .lastOrNull()
            ?.takeIf { (_, value) -> value == this }
            ?.let { (name, value) -> name to value }
            ?: null to this

        return newValue(value!!)
            .also { if (name != null) kwargsHolder[name] = it }
    }
}