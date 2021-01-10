package org.morfly.bazelgen.generator.writer

import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.morfly.bazelgen.generator.file.GenericBazelFile
import org.morfly.bazelgen.generator.formatter.BazelFileFormatter
import java.io.File


class BazelFileWriterTests : ShouldSpec({

    val formatter = mockk<BazelFileFormatter>()
    val writer = mockk<FileWriter<String>>()

    val formattedText = """
        load("test.bzl", "rule_1", "rule_2")
        
        rule_1(
            name = "test",
            srcs = [],
            deps = [],
        )
    """.trimIndent()

    val bazelFileWriter = BazelFileWriter(formatter, writer)

    val testBazelFile = GenericBazelFile("BUILD.bazel", "test", emptyList())

    every { formatter.format(testBazelFile) } returns formattedText
    justRun { writer.write(path = File("projectRootDir/test/BUILD.bazel"), content = formattedText) }

    should("write 'BazelFile' correctly") {
        bazelFileWriter.write(workspaceDir = File("projectRootDir"), content = testBazelFile)

        verify { writer.write(path = File("projectRootDir/test/BUILD.bazel"), content = formattedText) }
    }
})