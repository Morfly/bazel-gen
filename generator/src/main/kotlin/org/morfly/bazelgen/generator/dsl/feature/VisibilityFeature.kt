@file:Suppress("PropertyName")

package org.morfly.bazelgen.generator.dsl.feature

import org.morfly.bazelgen.generator.dsl.Label
import org.morfly.bazelgen.generator.dsl.StarlarkLanguageFeature


/**
 *
 */
internal interface VisibilityFeature : StarlarkLanguageFeature {

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