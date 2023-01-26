package year2022.day14
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
    val day14 = remember { Day14() }
    Day14Display(
        day14.cave,
        day14.sand,
        day14.isActive,
        day14::start,
        day14::pause,
        day14::reset,
    )
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication
    ) {
        window.size = Dimension(600, 1000)
        window.title = "Regolith Reservoir"
        App()
    }
}
