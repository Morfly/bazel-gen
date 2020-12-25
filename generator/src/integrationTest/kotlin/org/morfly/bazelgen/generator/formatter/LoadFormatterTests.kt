package org.morfly.bazelgen.generator.formatter

import io.kotest.core.spec.style.ShouldSpec


fun newLoadFormatter(indentSize: Int): LoadFormatter {
    val quoteFormatter = QuoteFormatter()
    return LoadFormatter(quoteFormatter, indentSize)
}


class LoadFormatterTests : ShouldSpec({
//    TODO()
})