package org.morfly.bazelgen.generator.dsl

import org.morfly.bazelgen.generator.file.BazelVersion


/**
 *
 */
fun bazelversion(major: Int, minor: Int, patch: Int): BazelVersion =
    bazelversion(major.toString(), minor.toString(), patch.toString())

/**
 *
 */
fun bazelversion(major: String, minor: String, patch: String): BazelVersion =
    BazelVersion(major, minor, patch)