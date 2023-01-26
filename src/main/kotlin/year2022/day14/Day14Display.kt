package year2022.day14

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.unit.dp
import utils.Field
import utils.Position

@Composable
fun Day14Display(
    cave: Field<String>,
    sand: List<Position>,
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
            Spacer(Modifier.height(16.dp))
            Field(cave, sand)
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

@OptIn(ExperimentalGraphicsApi::class)
@Composable
fun Field(field: Field<String>, positions: List<Position>) {
    Row {
        field.colIndices.forEach { x ->
            Column(
            ) {
                field.rowIndices.forEach { y ->
                    val position = Position(x, y)
                    val color = when {
                        position in positions -> Color.hsl(37F, 0.58F, 0.5F)
                        field[position] == "#" -> Color.Gray
                        else -> Color.White
                    }
                    val boxModifier = Modifier
                        .background(color = color)
                        .height(5.dp)
                        .width(5.dp)
                    Box(modifier = boxModifier)
                }
            }
        }
    }
}