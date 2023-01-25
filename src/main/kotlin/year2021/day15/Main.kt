package year2021.day15
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import Day15Display

@Composable
@Preview
fun App() {
    val day15 = remember { Day15() }
    Day15Display(
        day15.matrix,
        day15.path,
        day15.isActive,
        day15::start,
        day15::pause,
        day15::reset,
    )
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
