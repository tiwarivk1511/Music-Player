package screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import components.BottomControl
import components.sub_components.ListCards

@Composable
fun AudioScreen(onNavigate: () -> Unit) {
    val verticalScrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray)) {

        // Fixed header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .background(Color.DarkGray)
        ) {
            Text(
                text = "Audio",
                style = MaterialTheme.typography.h5,
                color = Color.White,
                modifier = Modifier.padding(vertical = 8.dp)
            )


            // Scrollable content area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(verticalScrollState)
                    .padding(horizontal = 16.dp)
                    .background(Color.DarkGray)
            ) {
                // Sample list of audio items
                Column(modifier = Modifier.fillMaxWidth()) {
                    repeat(10) { index ->
                        ListCards(
                            "Numb",
                            "Linkin Park",
                            "Numb",
                            Modifier.fillMaxWidth().height(65.dp),
                            onCardClick = {},
                            onFavoriteClick = {})
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // Fixed bottom control bar
        BottomControl(
            songName = "Numb",
            artistName = "Linkin Park",
            isPlaying = true,
            onPlayPauseToggle = {},
            onNext = {},
            onPrevious = {},
            onVolumeUp = {},
            onVolumeDown = {},
            onShuffleToggle = {},
            currentPosition = 0.0f,
            totalDuration = 100.0f,
            onSeek = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(82.dp)
                .background(color = Color(0xFF1E1E1E))
                .align(Alignment.BottomCenter)
        )

        //vertical scrollbar
        VerticalScrollbar(
            modifier = Modifier
                .fillMaxHeight()
                .width(8.dp)
                .align(Alignment.CenterEnd),
            adapter = rememberScrollbarAdapter(verticalScrollState)
        )
    }
}
