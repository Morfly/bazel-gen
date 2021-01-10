package org.morfly.bazelgen.generator.dsl.feature

import org.morfly.bazelgen.generator.dsl.StarlarkDslFeature
import org.morfly.bazelgen.generator.dsl.core.LoadStatement


/**
 *
 */
internal interface LoadFeature : StarlarkDslFeature {

    /**
     *
     */
    fun load(file: String, vararg rules: CharSequence): LoadStatement
}