package org.morfly.bazelgen.migrator.plugin.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction


open class MigrateToBazelTask : DefaultTask() {

    @TaskAction
    fun migrateToBazel() {
        // TODO
    }

    companion object {
        const val NAME = "migrateToBazel"
    }
}