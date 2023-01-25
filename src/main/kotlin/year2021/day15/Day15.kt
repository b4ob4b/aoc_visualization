package year2021.day15

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*
import utils.IO
import utils.matrix.Matrix
import utils.matrix.Position
import utils.toGrid
import utils.toMatrix

class Day15 {
    private val input = IO.readFile(2021, 15, IO.TYPE.SAMPLE)

    var path by mutableStateOf(listOf(Position.origin))
    var isActive by mutableStateOf(false)
    var hasFinished by mutableStateOf(false)
    val matrix = input.toGrid() { it.toInt() }.toMatrix()

    private var coroutineScope = CoroutineScope(Dispatchers.Main)
    private val queue: MutableSet<Triple<Position, Int, List<Position>>> = mutableSetOf()
    private val field = Field(matrix)
    private val seen = mutableMapOf<Position, Int>()

    init {
        initVariables()
    }

    fun start() {
        if (isActive) return
        if (hasFinished) {
            hasFinished = false
            initVariables()
        }

        coroutineScope.launch {
            this@Day15.isActive = true
            while (this@Day15.isActive) {
                delay(10L)
                val triple = queue.toSortedSet(compareBy { (position, distance, _) ->
                    distance + (position - field.goal).manhattenDistance
                }).first()
                queue.remove(triple)

                val (position, distance, path) = triple
                if (seen.containsKey(position) && seen[position]!! > distance) continue
                seen[position] = distance

                this@Day15.path = path

                if (position == field.goal) {
                    hasFinished = true
                    this@Day15.isActive = false
                }

                position.get4Neighbours()
                    .filter { field contains it }
                    .map { pos -> Triple(pos, distance + field[pos], path + pos) }
                    .filter { (position, distance) ->
                        (seen[position] ?: Int.MAX_VALUE) > distance
                    }.forEach {
                        queue.add(it)
                    }
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
        path = listOf(Position.origin)
        queue.clear()
        queue.add(Triple(Position.origin, 0, listOf(Position.origin)))
        seen.clear()
    }

    data class Field(val field: Matrix<Int>, val size: Int = 1) {
        val goal = Position(field.numberOfRows * size - 1, field.numberOfCols * size - 1)

        operator fun get(position: Position) = field[position]

        infix fun contains(position: Position): Boolean {
            return field contains position
        }
    }
}