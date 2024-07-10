package screens

import MediaPlayerController
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import components.sub_components.ListCards
import components.sub_components.VideoListCards
import components.sub_components.addToFavorites
import java.io.File
import java.util.prefs.Preferences
import javax.swing.JOptionPane

@Composable
fun VideoScreen(
    onNavigate: () -> Unit,
    folderPaths: List<String>
) {
    val verticalScrollState = rememberScrollState()

    // MediaPlayer state
    var isPlaying by remember { mutableStateOf(false) }

    // Favorite state
    val favoriteVideoList by remember { derivedStateOf { loadFavoriteMedia() } }

    // Function to handle unsupported file format error
    fun handleUnsupportedFormat(file: File) {
        showErrorDialog("Unsupported file format: ${file.extension}")
    }

    // Function to play video file
    fun playVideo(file: File) {
        MediaPlayerController.playVideoFile(file.toString())
        isPlaying = true
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray)) {
        // Fixed header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .background(Color.DarkGray)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource("drawable/ic_favorites.png"),
                    contentDescription = "Album Image",
                    modifier = Modifier.size(40.dp).align(Alignment.CenterVertically),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Video",
                    style = MaterialTheme.typography.h5,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

            }

            // Scrollable content area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(verticalScrollState)
                    .padding(horizontal = 16.dp)
                    .background(Color.DarkGray)
            ) {
                // Display media files
                val mediaFiles = remember(folderPaths) { scanMediaFiles(folderPaths) }
                mediaFiles.forEach { file ->
                    val isFavorite = favoriteVideoList.contains(file.path)
                    VideoListCards(
                        videoName = file.nameWithoutExtension,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp),
                        onCardClick = {
                            when (file.extension.toLowerCase()) {
                                "mp4", "avi", "mkv" -> playVideo(file)
                                else -> handleUnsupportedFormat(file)
                            }
                        },
                        onFavoriteClick = {
                            addToFavorites(file)
                            // Update the favorite list after adding or removing from favorites
                            saveFavoriteMedia(loadFavoriteMedia())
                        },
                        onPauseClick = {
                            MediaPlayerController.pause()
                            isPlaying = false
                        },
                        isPlaying = isPlaying,
                        favoriteList = favoriteVideoList
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
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



