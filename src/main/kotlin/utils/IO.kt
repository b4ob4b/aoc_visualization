package utils

import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.absolute

object IO {

    private val basePath = Path("").absolute()
    private val resourcesPath = "$basePath/src/main/resources"

    enum class TYPE(val path: String) {
        SAMPLE("sample"), INPUT("input"), SAMPLE2("sample2")
    }

    fun readFile(year: Int, day: Int, type: TYPE = TYPE.INPUT): String {
        val filePath = "$resourcesPath/year$year/$day/${type.path}.txt"
        return File(filePath).readText()
    }
}
