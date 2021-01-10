package org.morfly.bazelgen.generator.formatter

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.morfly.bazelgen.generator.dsl.core.LoadStatement


class LoadFormatterTests : ShouldSpec({
    val quoteFormatter = mockk<QuoteFormatter>()
    val loadFormatter = LoadFormatter(quoteFormatter)

    context("single line") {
        should("format with no rules") {
//            TODO()
        }

        should("format with one rule") {
            every { quoteFormatter("//:test.bzl") } returns "\"//:test.bzl\""
            every { quoteFormatter("rule1") } returns "\"rule1\""

            val load = LoadStatement("//:test.bzl", mapOf("rule1" to null))

            val expectedResult = """
                load("//:test.bzl", "rule1")
            """.trimIndent()

            loadFormatter.format(load, indentIndex = 0, mode = IndentMode.NEW_LINE) shouldBe expectedResult
        }

        should("format with multiple rules") {
            every { quoteFormatter("//:test.bzl") } returns "\"//:test.bzl\""
            every { quoteFormatter("rule1") } returns "\"rule1\""
            every { quoteFormatter("rule2") } returns "\"rule2\""

            val load = LoadStatement("//:test.bzl", mapOf("rule1" to null, "rule2" to null))

            val expectedResult = """
                load("//:test.bzl", "rule1", "rule2")
            """.trimIndent()

            loadFormatter.format(load, indentIndex = 0, mode = IndentMode.NEW_LINE) shouldBe expectedResult
        }
    }

    context("multiline") {
//        TODO()
    }

    context("aliases") {
//        TODO()
    }

})