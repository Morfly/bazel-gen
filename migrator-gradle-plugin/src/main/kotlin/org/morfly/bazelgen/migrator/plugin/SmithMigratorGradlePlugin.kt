package org.morfly.bazelgen.migrator.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import org.morfly.bazelgen.migrator.plugin.task.MigrateToBazelTask


/**
 *
 */
class SmithMigratorGradlePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target.tasks) {
            register<MigrateToBazelTask>(MigrateToBazelTask.NAME)
        }
    }
}