package org.morfly.bazelgen.generator.formatter

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe


class BaseTextFormattersTests : ShouldSpec({

    context("Any?.format") {
        should("format null value") {
            null.format() shouldBe "None"
        }

        should("format boolean value") {
            true.format() shouldBe "True"
            false.format() shouldBe "False"
        }

        should("return string representation of objects") {
            Pair("arg1", "arg2").format() shouldBe "(arg1, arg2)"
        }

        should("return string representation of numbers") {
            val int = 42
            val float = 42f
            val double = 42.0

            int.format() shouldBe "42"
            float.format() shouldBe "42.0"
            double.format() shouldBe "42.0"
        }

        should("not format string values") {
            "string".format() shouldBe "string"
        }
    }

    context("Any?.isLiteral") {
        should("return true for null") {
            null.isLiteral() shouldBe true
        }
        should("return true for number") {
            42.isLiteral() shouldBe true
            42f.isLiteral() shouldBe true
            42.0.isLiteral() shouldBe true
            42L.isLiteral() shouldBe true
            0b101.isLiteral() shouldBe true
        }

        should("return true for boolean") {
            true.isLiteral() shouldBe true
            false.isLiteral() shouldBe true
        }

        should("return false for string") {
            "string".isLiteral() shouldBe false
        }

        should("return false for char") {
            'c'.isLiteral() shouldBe false
        }

        should("return false for object") {
            Pair("arg1", "arg2").isLiteral() shouldBe false
        }
    }

    context("String.quote") {
        // TODO()
    }

    context("String.isQuoted") {
        // TODO()
    }

    context("String.containsQuotedSubstring") {
        // TODO()
    }

    context("String.unquote") {
        // TODO()
    }
})