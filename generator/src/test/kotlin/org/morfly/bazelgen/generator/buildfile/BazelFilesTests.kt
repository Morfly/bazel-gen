package org.morfly.bazelgen.generator.buildfile

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe


class BazelFilesTests : ShouldSpec({

    val testBuildStatements = listOf(
        RawTextStatement("""load("test.bzl", "rule_1", "rule_2")"""),
        RawTextStatement("# comment")
    )

    context("WORKSPACE descriptor") {
        should("be created with '.bazel' extension") {
            val workspace = BazelWorkspace(hasExtension = true, statements = testBuildStatements)

            with(workspace) {
                name shouldBe "WORKSPACE.bazel"
                relativePath shouldBe ""
                statements shouldBe testBuildStatements
            }
        }
        should("be created without extensions") {
            val workspace = BazelWorkspace(hasExtension = false, statements = testBuildStatements)

            with(workspace) {
                name shouldBe "WORKSPACE"
                relativePath shouldBe ""
                statements shouldBe testBuildStatements
            }
        }
    }

    context("BUILD descriptor") {
        should("be created in root dir with '.bazel' extension") {
            val build = BazelBuild(hasExtension = true, relativePath = "", statements = testBuildStatements)

            with(build) {
                name shouldBe "BUILD.bazel"
                relativePath shouldBe ""
                statements shouldBe testBuildStatements
            }
        }

        should("be created in root dir without extensions") {
            val build = BazelBuild(hasExtension = false, relativePath = "", statements = testBuildStatements)

            with(build) {
                name shouldBe "BUILD"
                relativePath shouldBe ""
                statements shouldBe testBuildStatements
            }
        }

        should("be created with '.bazel' extension") {
            val build = BazelBuild(hasExtension = true, relativePath = "test", statements = testBuildStatements)

            with(build) {
                name shouldBe "BUILD.bazel"
                relativePath shouldBe "test"
                statements shouldBe testBuildStatements
            }
        }

        should("be created without extensions") {
            val build = BazelBuild(hasExtension = false, relativePath = "test", statements = testBuildStatements)

            with(build) {
                name shouldBe "BUILD"
                relativePath shouldBe "test"
                statements shouldBe testBuildStatements
            }
        }
    }

    context(".bazelversion descriptor") {
        should("be created") {
            val bazelversion = BazelVersion("1", "0", "1")

            with(bazelversion) {
                name shouldBe ".bazelversion"
                relativePath shouldBe ""
                statements shouldBe listOf(RawTextStatement("$major.$minor.$patch"))
            }
        }
    }

    context(".bazelrc descriptor") {
        val testBazelrcStatements = listOf(
            RawTextStatement("build --test-option-1"),
            RawTextStatement("build --test-option-2")
        )

        should("be created in root dir with '.bazelrc' name") {
            val bazelrc = BazelRc(testBazelrcStatements)

            with(bazelrc) {
                name shouldBe ".bazelrc"
                relativePath shouldBe ""
                statements shouldBe testBazelrcStatements
            }
        }

        should("be created with 'local.bazelrc' name") {
            val bazelrc = BazelRc(testBazelrcStatements, namePrefix = "local", relativePath = "test")

            with(bazelrc) {
                name shouldBe "local.bazelrc"
                relativePath shouldBe "test"
                statements shouldBe testBazelrcStatements
            }
        }
    }

    context("generic bazel file creator") {
        should("be created 'BUILD' file descriptor") {
            val build = GenericBazelFile(name = "BUILD", relativePath = "test", statements = testBuildStatements)

            with(build) {
                name shouldBe "BUILD"
                relativePath shouldBe "test"
                statements shouldBe testBuildStatements
            }
        }

        should("be created '.bzl' file descriptor") {
            val bzl = GenericBazelFile(name = "test.bzl", relativePath = "test", statements = testBuildStatements)

            with(bzl) {
                name shouldBe "test.bzl"
                relativePath shouldBe "test"
                statements shouldBe testBuildStatements
            }
        }
    }
})