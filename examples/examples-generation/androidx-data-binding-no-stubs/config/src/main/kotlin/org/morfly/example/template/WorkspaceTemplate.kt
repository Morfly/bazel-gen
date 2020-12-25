package org.morfly.example.template

import org.morfly.bazelgen.generator.dsl.WORKSPACE
import org.morfly.bazelgen.generator.dsl.feature.ref
import org.morfly.bazelgen.generator.dsl.function.*
import org.morfly.bazelgen.generator.dsl.rule.bind


fun workspace_template(
    workspaceName: String,
    artifactList: List<String>
    /**
     *
     */
) = WORKSPACE {

    workspace(name = workspaceName)

    android_sdk_repository {
        name = "androidsdk"
    }

    load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

    "RULES_JVM_EXTERNAL_TAG" `=` "3.3"
    "RULES_JVM_EXTERNAL_SHA" `=` "d85951a92c0908c80bd8551002d66cb23c3434409c814179c0ff026b53544dab"
    http_archive {
        name = "rules_jvm_external"
        strip_prefix = "rules_jvm_external-%s" `%` "RULES_JVM_EXTERNAL_TAG".ref()
        sha256 = "RULES_JVM_EXTERNAL_SHA".ref()
        url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" `%` "RULES_JVM_EXTERNAL_TAG".ref()
    }

    "DAGGER_TAG" `=` "2.28.1"
    "DAGGER_SHA" `=` "9e69ab2f9a47e0f74e71fe49098bea908c528aa02fa0c5995334447b310d0cdd"
    http_archive {
        name = "dagger"
        strip_prefix = "dagger-dagger-%s" `%` "DAGGER_TAG".ref()
        sha256 = "DAGGER_SHA".ref()
        urls = list("https://github.com/google/dagger/archive/dagger-%s.zip" `%` "DAGGER_TAG".ref())
    }
    load("@dagger//:workspace_defs.bzl", "DAGGER_ARTIFACTS".ref(), "DAGGER_REPOSITORIES".ref())

    load("@rules_jvm_external//:defs.bzl", "maven_install")
    maven_install {
        artifacts = artifactList `+` "DAGGER_ARTIFACTS".ref()
        repositories = list(
            "https://repo1.maven.org/maven2",
            "https://maven.google.com"
        ) `+` "DAGGER_REPOSITORIES".ref()
        jetify = true
        fail_on_missing_checksum = false
    }

    bind {
        name = "databinding_annotation_processor"
        actual = "//tools/android:compiler_annotation_processor"
    }

    load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

    "RULES_KOTLIN_VERSION" `=` "legacy-1.4.0-rc3"

    "RULES_KOTLIN_SHA" `=` "da0e6e1543fcc79e93d4d93c3333378f3bd5d29e82c1bc2518de0dbe048e6598"

    http_archive {
        name = "io_bazel_rules_kotlin"
        sha256 = "RULES_KOTLIN_SHA".ref()
        url =
            "https://github.com/bazelbuild/rules_kotlin/releases/download/%s/rules_kotlin_release.tgz" `%` "RULES_KOTLIN_VERSION".ref()
    }

    load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kotlin_repositories", "kt_register_toolchains")

    "KOTLIN_VERSION" `=` "1.3.72"

    "KOTLINC_RELEASE_SHA" `=` "ccd0db87981f1c0e3f209a1a4acb6778f14e63fe3e561a98948b5317e526cc6c"

    "KOTLINC_RELEASE" `=` {
        "urls" to list(
            "\"https://github.com/JetBrains/kotlin/releases/download/v{v}/kotlin-compiler-{v}.zip\".format(v = KOTLIN_VERSION)",
        )
        "sha256" to "KOTLINC_RELEASE_SHA".dictref
    }

    kotlin_repositories { compiler_release = "KOTLINC_RELEASE".ref() }

    kt_register_toolchains()
}