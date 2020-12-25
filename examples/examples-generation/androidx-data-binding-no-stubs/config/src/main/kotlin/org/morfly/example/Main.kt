package org.morfly.example

import org.morfly.example.generator.ProjectGenerator


fun main() {
    val projectGenerator = ProjectGenerator()

    println("Generating project...")

    projectGenerator.generate(
        numOfModules = 2,
        disableStrictJavaDeps = true
    )

    println("Project successfully generated.")
}