package org.morfly.bazelgen.generator.writer

import java.io.File


/**
 *
 */
class FilePrinter : FileWriter<String> {

    override fun write(path: File, content: String) {
        println(
            """
                File: $path
                
                $content
            """.trimIndent()
        )
    }
}