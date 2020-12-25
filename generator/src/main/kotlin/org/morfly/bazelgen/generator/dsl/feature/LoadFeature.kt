package org.morfly.bazelgen.generator.dsl.feature

import org.morfly.bazelgen.generator.dsl.StarlarkLanguageFeature


/**
 *
 */
internal interface LoadFeature : StarlarkLanguageFeature {

    /**
     *
     */
    fun load(file: String, vararg rules: CharSequence)
}