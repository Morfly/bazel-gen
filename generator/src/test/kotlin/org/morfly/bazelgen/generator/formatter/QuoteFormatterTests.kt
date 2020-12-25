package org.morfly.bazelgen.generator.formatter

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe


class QuoteFormatterTests : ShouldSpec({
    val formatter = QuoteFormatter()

    context("single-line strings") {

        should("wrap single-line string with quotes") {
            val string = "single-line string"

            formatter.format(string) shouldBe "\"single-line string\""
        }

        should("ignore quoted single-line string") {
            val string = "\"single-line string\""

            formatter.format(string) shouldBe string
        }

        should("ignore single-line string containing quoted substring") {
            val string = "single-line \"string\""

            formatter.format(string) shouldBe string
        }
    }

    context("multiline strings") {

        should("wrap multiline string with quotes") {
            val string = """
                multiline
                string
            """.trimIndent()

            val quote = "\"\"\""
            val expectedResult = """
                $quote
                multiline
                string
                $quote
            """.trimIndent()

            formatter.format(string) shouldBe expectedResult
        }

        should("ignore quoted multiline string") {
            val quote = "\"\"\""
            val string = """
                $quote
                multiline
                string
                $quote
            """.trimIndent()

            formatter.format(string) shouldBe string
        }

        should("ignore multiline string containing quoted multiline substring") {
            val quote = "\"\"\""
            val string = """
                multiline
                $quote
                string
                $quote
            """.trimIndent()

            formatter.format(string) shouldBe string
        }

        should("format multiline string containing quoted single-line substring") {
            val quote = "\"\"\""
            val string = """
                multiline
                "string"
            """.trimIndent()

            val expectedResult = """
                $quote
                multiline
                "string"
                $quote
            """.trimIndent()

            formatter.format(string) shouldBe expectedResult
        }
    }
})