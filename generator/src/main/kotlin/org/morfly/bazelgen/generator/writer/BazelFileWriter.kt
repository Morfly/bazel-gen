@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package org.morfly.bazelgen.generator.writer

import org.morfly.bazelgen.generator.buildfile.BazelFile
import org.morfly.bazelgen.generator.formatter.BazelFileFormatter
import org.morfly.bazelgen.generator.formatter.newInstance
import java.io.File


/**
 * @param formatter
 * @param writer
 */
class BazelFileWriter(
    private val formatter: BazelFileFormatter,
    private val writer: FileWriter<String>
) : FileWriter<BazelFile> {

    /**
     * @param workspaceDir
     * @param content
     */
    override fun write(workspaceDir: File, content: BazelFile) = with(content) {
        val path = File("${workspaceDir.path}/$relativePath/$name")
        writer.write(path, formatter.format(content))
    }


    companion object {

        /**
         *
         */
        fun newInstance(
            formatter: BazelFileFormatter = BazelFileFormatter.newInstance(),
            writer: FileWriter<String> = DefaultFileWriter()
        ) = BazelFileWriter(formatter, writer)
    }
}