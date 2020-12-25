package org.morfly.bazelgen.generator.formatter

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe


class TextFormatterTests : ShouldSpec({

    should("'lineSeparator' be correct") {
        LINE_SEPARATOR shouldBe System.getProperty("line.separator")
    }
})