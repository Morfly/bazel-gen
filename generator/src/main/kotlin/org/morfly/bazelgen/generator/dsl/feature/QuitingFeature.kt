package org.morfly.bazelgen.generator.dsl.feature

import org.morfly.bazelgen.generator.dsl.StarlarkLanguageFeature
import org.morfly.bazelgen.generator.formatter.quote
import org.morfly.bazelgen.generator.formatter.unquote


/**
 *
 */
internal interface QuotingFeature : StarlarkLanguageFeature {

    /**
     *
     */
    val CharSequence.quoted: CharSequence get() = toString().quote()

    /**
     *
     */
    val CharSequence.q: CharSequence get() = quoted

    /**
     *
     */
    val CharSequence.unquoted: CharSequence get() = toString().unquote()

    /**
     *
     */
    val CharSequence.uq: CharSequence get() = toString().unquoted
}