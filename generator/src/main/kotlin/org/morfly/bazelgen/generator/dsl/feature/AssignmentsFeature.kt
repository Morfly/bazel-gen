@file:Suppress("FunctionName")

package org.morfly.bazelgen.generator.dsl.feature

import org.morfly.bazelgen.generator.dsl.StarlarkDslFeature
import org.morfly.bazelgen.generator.dsl.core.element.Assignment


/**
 *
 */
internal interface AssignmentsFeature : StarlarkDslFeature {

    /**
     *
     */
    infix fun String.`=`(value: Any): Assignment<Any>

    /**
     *
     */
    infix fun <T> String.`=`(value: List<T>): Assignment<List<T>>

    /**
     *
     */
    infix fun String.`=`(body: DictionaryContext.() -> Unit): Assignment<Map<String, Any?>>
}