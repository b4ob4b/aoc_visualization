package year2022.day12
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import Day15Display
import java.awt.Dimension

@Composable
@Preview
fun App() {
    val day12 = remember { Day12() }
    Day12Display(
        day12.matrix,
        day12.path,
        day12.isActive,
        day12::start,
        day12::pause,
        day12::reset,
    )
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication
    ) {
        window.size = Dimension(1000, 400)
        App()
    }
}
