package org.morfly.bazelgen.generator.writer

import java.io.File


/**
 *
 */
interface FileWriter<C> {

    /**
     *
     */
    fun write(path: File, content: C)
}