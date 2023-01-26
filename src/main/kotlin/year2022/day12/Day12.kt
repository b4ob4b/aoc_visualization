package year2022.day12

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*
import utils.IO
import utils.matrix.Matrix
import utils.matrix.Position
import utils.print
import utils.toGrid
import utils.toMatrix

class Day12 {
    private val input = IO.readFile(2022, 12, IO.TYPE.INPUT)

    private fun Char.toHeight() = when (this) {
        'S' -> 0
        'E' -> 27
        else -> this.code - 'a'.code + 1
    }

    var isActive by mutableStateOf(false)
    var hasFinished by mutableStateOf(false)
    val matrix = input.toGrid() { it.single().toHeight() }.toMatrix().transpose()
    private val start = matrix.search(0).single()
    var path by mutableStateOf(listOf(start))

    init {
        initVariables()
    }

    private var coroutineScope = CoroutineScope(Dispatchers.Main)

    fun start() {
        if (isActive) return
        if (hasFinished) {
            hasFinished = false
            initVariables()
        }

        coroutineScope.launch {
            this@Day12.isActive = true
            val paths = matrix
                .findPaths()
                .chunked(20)
                .map { it.last() }
                .iterator()
            while (this@Day12.isActive && paths.hasNext()) {
                delay(1L)
                path = paths.next()
            }
        }
    }

    fun pause() {
        isActive = false
    }

    fun reset() {
        initVariables()
        coroutineScope.cancel()
        coroutineScope = CoroutineScope(Dispatchers.Main)
        isActive = false
    }

    private fun initVariables() {
        path = listOf(start)
    }

    private fun Matrix<Int>.findPaths() = sequence {
        val matrix = this@findPaths
        val queue = ArrayDeque<Triple<Position, Int, List<Position>>>()
        queue.add(Triple(start, 0, path))
        val goal = 'E'.toHeight()
        val visited = mutableSetOf<Position>()
        while (queue.isNotEmpty()) {
            val (pos, steps, path) = queue.removeFirst()
            if (visited.contains(pos)) continue
            visited.add(pos)
            yield(path)
            if (matrix[pos] == goal) break
            pos.get4Neighbours().forEach { neighbour ->
                val currentHeight = matrix[pos]
                if (neighbour.row in matrix.rowIndices && neighbour.col in matrix.colIndices) {
                    val neighbourHeight = matrix[neighbour]
                    if (neighbourHeight <= (1 + currentHeight)) {
                        queue.add(Triple(neighbour, (steps + 1), path + neighbour))
                    }
                }
            }
        }
    }

}