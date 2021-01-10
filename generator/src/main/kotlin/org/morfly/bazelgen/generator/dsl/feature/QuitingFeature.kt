package org.morfly.bazelgen.generator.dsl.feature

import org.morfly.bazelgen.generator.dsl.StarlarkDslFeature
import org.morfly.bazelgen.generator.formatter.quote


/**
 *
 */
internal interface QuotingFeature : StarlarkDslFeature {

    /**
     *
     */
    val CharSequence.quoted: CharSequence get() = toString().quote()

    /**
     *
     */
    val CharSequence.q: CharSequence get() = quoted
}