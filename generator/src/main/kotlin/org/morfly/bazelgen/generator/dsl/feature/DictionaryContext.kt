package org.morfly.bazelgen.generator.dsl.feature

import org.morfly.bazelgen.generator.dsl.StarlarkFeatureContext


///**
// *
// */
//@StarlarkFeatureContext
//class DictionaryContext : MappingFeature {
//
//    private val kwargsHolder = linkedMapOf<String, Any>()
//
//    /**
//     *
//     */
//    val kwargs: Map<String, Any> get() = kwargsHolder
//
//
//    override infix fun String.to(value: Any): Pair<String, Any> =
//        Pair(this, value)
//            .also { kwargsHolder[this] = it }
//
//    override infix fun <T> String.to(value: List<T>): Pair<String, List<T>> =
//        Pair(this, value)
//            .also { kwargsHolder[this] = it }
//
//    override infix fun String.to(body: DictionaryContext.() -> Unit): Pair<String, Map<String, Any>> =
//        Pair(
//            first = this,
//            second = DictionaryContext()
//                .apply(body)
//                .kwargs
//                .also { kwargsHolder[this] = it }
//        )
//}