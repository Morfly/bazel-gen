package org.morfly.bazelgen.generator.formatter

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class TextFormattersTests : ShouldSpec({

    context("Any?.formatted") {

        should("return 'None' for null values") {
            null.format() shouldBe "None"
        }

        should("return 'True' for Boolean true value") {
            true.format() shouldBe "True"
        }

        should("return 'False' for Boolean false value") {
            false.format() shouldBe "False"
        }

        should("return String representations for other object types") {
            listOf(1, 2, 3).format() shouldBe "[1, 2, 3]"
            "test string".format() shouldBe "test string"

            data class TestType(val param1: String, val param2: Double)
            TestType("1", 2.0).format() shouldBe "TestType(param1=1, param2=2.0)"
        }
    }
})