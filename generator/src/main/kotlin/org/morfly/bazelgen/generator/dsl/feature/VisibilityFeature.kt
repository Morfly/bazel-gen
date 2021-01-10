@file:Suppress("PropertyName")

package org.morfly.bazelgen.generator.dsl.feature

import org.morfly.bazelgen.generator.dsl.Label
import org.morfly.bazelgen.generator.dsl.StarlarkDslFeature


/**
 *
 */
internal interface VisibilityFeature : StarlarkDslFeature {

    /**
     *
     */
    val public: List<Label>
        get() = listOf("//visibility:public")

    /**
     *
     */
    val private: List<Label>
        get() = listOf("//visibility:private")

    /**
     *
     */
    val __pkg__: Label
        get() = "__pkg__"

    /**
     *
     */
    val __subpackages__: Label
        get() = "__subpackages__"
}