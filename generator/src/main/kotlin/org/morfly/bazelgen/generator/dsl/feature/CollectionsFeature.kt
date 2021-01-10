package org.morfly.bazelgen.generator.dsl.feature

import org.morfly.bazelgen.generator.dsl.StarlarkDslFeature


/**
 *
 */
internal interface CollectionsFeature : StarlarkDslFeature {

    /**
     *
     */
    fun <T> list(vararg args: T): List<T> = listOf(*args)

    /**
     *
     */
    fun dict(vararg kwargs: Pair<String, Any?>): Map<String, Any?> = kwargs.toMap()
}