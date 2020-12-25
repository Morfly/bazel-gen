package org.morfly.bazelgen.generator.formatter

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe


class TestIndentedTextFormatter<T>(indentSize: Int) : IndentedTextFormatter<T>(indentSize) {

    override fun format(arg: T, indentIndex: Int, mode: IndentMode) =
        throw RuntimeException("Function 'format' should be mocked.")
}


class IndentedTextFormatterTests : ShouldSpec({

    should("throw an exception when creating 'IndentedTextFormatter' with negative 'indentSize'") {
        shouldThrow<IllegalArgumentException> {
            TestIndentedTextFormatter<Any>(indentSize = -1)
        }
    }

    should("'DEFAULT_INDENT_SIZE' be 4") {
        DEFAULT_INDENT_SIZE shouldBe 4
    }
})