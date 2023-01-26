package year2022.day14

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*
import utils.*
import utils.matrix.Matrix
import utils.matrix.Position

fun main() {
    Day14().p()
}

class Day14 {

    fun p() {
        val s = SandReservoir(input)
        s.rocks.let {
            it.maxOf { it.x }.print()
            it.minOf { it.x }.print()
        }
        s.reservoirBottom.print()
    }

    private val input = IO.readFile(2022, 14, IO.TYPE.INPUT)
    private var sandReservoir = SandReservoir(input)

    var isActive by mutableStateOf(false)
    var hasFinished by mutableStateOf(false)
    var sand by mutableStateOf(emptyList<utils.Position>())
    val matrix = input.toGrid().toMatrix()
    val cave = sandReservoir.cave

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
            this@Day14.isActive = true
            val reservoir = sandReservoir.dropsSand().iterator()
            while (this@Day14.isActive && reservoir.hasNext()) {
                delay(1L)
                reservoir.next()
                sand = sandReservoir.getSand()
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
        sand = emptyList()
        sandReservoir = SandReservoir(input)
    }

    private class SandReservoir(wallInformation: String) {
        val rocks = wallInformation.splitLines().flatMap {
            val corners = it.split(" -> ").map { it.toPosition() }
            corners.zipWithNext().flatMap { it.getLine() }
        }.toSet()

        private val deepestRock = rocks.maxOf { it.y }
        private val source = utils.Position(500, 0)
        val reservoirBottom = deepestRock + 2
        private val sandPositions = mutableSetOf<utils.Position>()
        private val sand = Sand(source, State.Falling)
        private val down = utils.Position(0, 1)
        private val downLeft = utils.Position(-1, 1)
        private val downRight = utils.Position(1, 1)
        val cave = createCave()

        private fun createCave(): Field<String> {
            val x0 = rocks.minOf { it.x } - 10
            val x1 = rocks.maxOf { it.x } + 10
            val y0 = 0
            val y1 = deepestRock

            val cave = Field(x1 - x0, y1 - y0) { "." }
            val positionToRock = rocks
                .map { it - utils.Position(x0, y0) }
                .associateWith { "#" }
            return cave.insertAt(positionToRock)
        }
        
        fun getSand(): List<utils.Position> {
            val x0 = rocks.minOf { it.x } - 10
            return sandPositions.map { it - utils.Position(x0, 0) }
        }

        private fun reset() {
            sand.position = source
            sand.state = State.Falling
        }


        fun dropsSand() = sequence {
            while (true) {
                when (sand.state) {
                    State.Falling -> sandFalls()
                    State.Rolling -> sandRolls()
                    State.Lost -> sand.state = State.Rolling
                    else -> {}
                }
            }
        }

        private suspend fun SequenceScope<State>.sandFalls() {
            val hitPosition = sand.position.getPathThrough(down).takeWhile { it.touchesObstacle() }.last()
            if (hitPosition.y >= deepestRock) yield(State.Lost)
            sand.position = hitPosition
            sand.state = State.Rolling
        }

        private fun utils.Position.touchesObstacle() = rocks.contains(this).not() && sandPositions.contains(this).not() && this.y < reservoirBottom

        private fun Sand.hitsObstacle(position: utils.Position): Boolean {
            return (this.position + position) in sandPositions || (this.position + position) in rocks || (this.position + position).y == reservoirBottom
        }

        private suspend fun SequenceScope<State>.sandRolls() {
            if (sand.hitsObstacle(downLeft).not()) {
                sandLooksLeft()
            } else if (sand.hitsObstacle(downRight).not()) {
                sandLooksRight()
            } else {
                sandFindsRestingPosition()
            }
        }

        private fun sandLooksLeft() {
            sand.position = sand.position + downLeft
            sand.state = State.Falling
        }

        private fun sandLooksRight() {
            sand.position = sand.position + downRight
            sand.state = State.Falling
        }

        private suspend fun SequenceScope<State>.sandFindsRestingPosition() {
            sandPositions.add(sand.position)
            yield(State.Resting)
            if (sand.position == source) {
                yield(State.Full)
            }
            reset()
        }

        private data class Sand(var position: utils.Position, var state: State)

        enum class State {
            Falling, Rolling, Resting, Lost, Full
        }
    }
}