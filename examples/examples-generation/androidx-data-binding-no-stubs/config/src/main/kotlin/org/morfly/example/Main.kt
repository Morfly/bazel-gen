package org.morfly.example

import org.morfly.example.generator.ProjectGenerator


fun main() {
    val projectGenerator = ProjectGenerator()

    println("Generating project...")

    projectGenerator.generate(
        numOfModules = 5,
        depsOverlap = 2
    )

    println("Project successfully generated.")
}