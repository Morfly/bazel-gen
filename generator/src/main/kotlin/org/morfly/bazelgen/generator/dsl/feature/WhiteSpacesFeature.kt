package org.morfly.bazelgen.generator.dsl.feature

import org.morfly.bazelgen.generator.dsl.StarlarkDslFeature
import org.morfly.bazelgen.generator.dsl.core.WhiteSpaceStatement


/**
 *
 */
interface WhiteSpacesFeature : StarlarkDslFeature {

    /**
     *
     */
    val space: WhiteSpaceStatement
}