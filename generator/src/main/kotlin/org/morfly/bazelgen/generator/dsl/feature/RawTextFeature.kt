package org.morfly.bazelgen.generator.dsl.feature

import org.morfly.bazelgen.generator.dsl.StarlarkLanguageFeature


/**
 *
 */
internal interface RawTextFeature : StarlarkLanguageFeature {

    /**
     *
     */
    val String.txt: Unit

    /**
     *
     */
    operator fun String.unaryMinus()
}