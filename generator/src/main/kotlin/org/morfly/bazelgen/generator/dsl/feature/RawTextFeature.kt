package org.morfly.bazelgen.generator.dsl.feature

import org.morfly.bazelgen.generator.dsl.StarlarkDslFeature


/**
 *
 */
internal interface RawTextFeature : StarlarkDslFeature {

    /**
     *
     */
    val String.txt: Unit

    /**
     *
     */
    operator fun String.unaryMinus()
}