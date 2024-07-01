import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.BottomControl
import components.sub_components.AlbumListViewArea
import components.sub_components.BannerHome
import components.sub_components.ListViewArea

// Data class for Album
data class Album(
    val resourcePath: String, // Path to the album resource
    val albumName: String, // Name of the album
    val artistName: String // Name of the artist
)

@Composable
fun HomeScreen(function: () -> Unit) {
    val verticalScrollState = rememberScrollState()
    val albums = remember { mutableStateListOf<Album>() }
    val musicFolderPath = "C:/Users/BrancoSoft/Music"

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(verticalScrollState)
            ) {
                // Show the banner
                BannerHome(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    title = "What's New?",
                    message = "Hello, Let's Enjoy the Rock!!"
                )

                Row(modifier = Modifier.fillMaxWidth()
                    .background(color = Color.DarkGray),
                    horizontalArrangement = Arrangement.SpaceEvenly ) {
                    Text(text = "Latest Albums", fontSize = 20.sp, color = Color.White)
                    Text(text = "Latest Songs", fontSize = 20.sp, color = Color.White)
                }

                Row(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween // Adjust horizontalArrangement
                ) {
                    // Show the list view
                    AlbumListViewArea(
                        modifier = Modifier
                            .weight(1f) // Use weight to take 50% of available width
                            .fillMaxHeight()
                            .background(Color.DarkGray)
                            .padding(4.dp),
                    )

                    ListViewArea(
                        modifier = Modifier
                            .weight(1f) // Use weight to take 50% of available width
                            .fillMaxHeight()
                            .background(Color.DarkGray)
                            .padding(4.dp),
                    )
                }
            }

            // Bottom bar for music control (static)
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
            )
        }

        // Vertical scrollbar
        VerticalScrollbar(
            modifier = Modifier
                .fillMaxHeight()
                .width(8.dp)
                .align(Alignment.CenterEnd),
            adapter = rememberScrollbarAdapter(verticalScrollState)
        )
    }
}
