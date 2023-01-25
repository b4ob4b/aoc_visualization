import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import utils.matrix.Matrix
import utils.matrix.Position

@Composable
fun Day15Display(
    matrix: Matrix<Int>,
    path: List<Position>,
    isActive: Boolean,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResetClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Matrix(matrix, path)
            Spacer(Modifier.height(16.dp))
            Row {
                val lightGray = ButtonDefaults.buttonColors(backgroundColor = Color.Gray.copy(0.2F))
                val width = Modifier.width(80.dp)
                if (isActive) {
                    Button(
                        onPauseClick, modifier = width, colors = lightGray
                    ) {
                        Text("Pause")
                    }
                } else {
                    Button(
                        onStartClick, modifier = width, colors = lightGray
                    ) {
                        Text("Start")
                    }
                }
                Spacer(Modifier.width(16.dp))
                Button(onResetClick, colors = lightGray) {
                    Text("Reset")
                }
            }

        }
    }
}

@Composable
fun Matrix(matrix: Matrix<Int>, path: List<Position>) {
    Row {
        matrix.rowIndices.forEach { row ->
            Column(
            ) {
                matrix.colIndices.forEach { col ->
                    val position = Position(row, col)
                    val cell = matrix[position]
                    val color = if (position in path) Color.Green else Color.Red
                    val boxModifier = Modifier
                        .background(color = color.copy(alpha = cell / 10.0F))
                        .height(20.dp)
                        .width(20.dp)
                    Box(
                        modifier = boxModifier
                    ) {
                        Text(
                            cell.toString(),
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }
            }
        }
    }
}