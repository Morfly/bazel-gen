package org.morfly.bazelgen.generator.writer

import java.io.File


/**
 *
 */
class DefaultFileWriter : FileWriter<String> {

    override fun write(path: File, content: String) = with(path) {
        try {
            parentFile.mkdirs()
            createNewFile()
            bufferedWriter().use { out -> out.write(content) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}