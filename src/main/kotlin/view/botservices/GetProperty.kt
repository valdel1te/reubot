package view.botservices

import java.io.File
import java.io.FileInputStream
import java.util.*

object GetProperty {
    private val file = File("src/main/resources/config.properties")
    private val properties = Properties()

    init {
        FileInputStream(file).use {
            properties.load(it)
        }
    }

    fun key(name: String): String {
        return properties.getProperty(name)
    }
}
