import androidx.compose.desktop.Window
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageAsset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

fun main() = Window(title = "Compose for Desktop", size = IntSize(250, 500)) {
    val count = remember { mutableStateOf(0) }
    MaterialTheme {
        Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                   onClick = {
                       count.value++
                   }) {
                Text(if (count.value == 0) "Hello World" else "Clicked ${count.value}!")
            }
            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                   onClick = {
                       count.value = 0
                    }) {
                Text("Reset")
            }
            NewsStory()
        }
    }
}

@Composable
fun NewsStory() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Image(asset = ImageAsset(20, 20))
        Text("A day in Shark Fin Cove")
        Text("Davenport, California")
        Text("December 2018")
    }
}

