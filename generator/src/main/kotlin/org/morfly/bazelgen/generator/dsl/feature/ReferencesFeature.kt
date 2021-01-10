@file:Suppress("SpellCheckingInspection")

package org.morfly.bazelgen.generator.dsl.feature

import org.morfly.bazelgen.generator.dsl.Label
import org.morfly.bazelgen.generator.dsl.StarlarkDslFeature
import org.morfly.bazelgen.generator.dsl.core.element.AnyReference
import org.morfly.bazelgen.generator.dsl.core.element.DictionaryReference
import org.morfly.bazelgen.generator.dsl.core.element.ListReference
import org.morfly.bazelgen.generator.dsl.core.element.StringReference


/**
 *
 */
interface ReferencesFeature : StarlarkDslFeature {

    /**
     *
     */
    fun <T> String.listref(): List<T> =
        ListReference(this)

    /**
     *
     */
    val String.listref: List<Label>
        get() = ListReference(this)

    /**
     *
     */
    val String.dictref: Map<String, Any?>
        get() = DictionaryReference(this)

    /**
     *
     */
    val String.strref: CharSequence
        get() = StringReference(this)
}


/**
 * @receiver
 * @param T
 * @return
 */
inline fun <reified T : Any> String.ref(): T =
    when (T::class) {
        CharSequence::class -> StringReference(this) as T
        List::class -> ListReference<Any?>(this) as T
        Map::class -> DictionaryReference(this) as T
        else -> AnyReference(this) as T
    }