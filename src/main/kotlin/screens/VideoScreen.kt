package screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import components.BottomControl
import components.sub_components.ListCards
import java.io.File

@Composable
fun VideoScreen(onNavigate: () -> Unit, folderPaths: List<String>) {
    val videoFiles = remember { retrieveVideoFilesFromFolders(folderPaths) }
    val mediaPlayer = remember { MediaPlayerController() }
    val verticalScrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray)) {

        // Fixed header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .background(Color.DarkGray)
        ) {
            Row {
                Image(
                    painter = painterResource("drawable/ic_video_play.png"),
                    contentDescription = "Album Image",
                    modifier = Modifier.size(40.dp).align(Alignment.CenterVertically),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Videos",
                    style = MaterialTheme.typography.h5,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 8.dp).align(Alignment.CenterVertically)
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
                // Display video files
                videoFiles.forEach { videoFile ->
                    ListCards(
                        videoFile.nameWithoutExtension,
                        videoFile.parentFile?.name ?: "Unknown Artist",
                        videoFile.name,
                        Modifier.fillMaxWidth().height(65.dp),
                        onCardClick = {
                            // Handle video playback
                            // Example: navigate to a video playback screen
                            // For simplicity, you can add a placeholder action here
                        },
                        onFavoriteClick = {
                            // Handle favorite action if needed
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Fixed bottom control bar (example implementation, adjust as per your actual player implementation)
        BottomControl(
            songName = mediaPlayer.currentSongName,
            artistName = mediaPlayer.currentArtistName,
            isPlaying = mediaPlayer.isPlaying(),
            onPlayPauseToggle = { mediaPlayer.togglePlayPause() },
            onNext = { mediaPlayer.playNext() },
            onPrevious = { mediaPlayer.playPrevious() },
            onVolumeUp = { mediaPlayer.adjustVolume(true) },
            onVolumeDown = { mediaPlayer.adjustVolume(false) },
            onShuffleToggle = { mediaPlayer.toggleShuffle() },
            currentPosition = mediaPlayer.getCurrentPosition(),
            totalDuration = mediaPlayer.getTotalDuration(),
            onSeek = { position -> mediaPlayer.seekTo(position) },
            modifier = Modifier
                .fillMaxWidth()
                .height(82.dp)
                .background(color = Color(0xFF1E1E1E))
                .align(Alignment.BottomCenter)
        )

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

fun retrieveVideoFilesFromFolders(folderPaths: List<String>): List<File> {
    val videoFiles = mutableListOf<File>()

    for (folderPath in folderPaths) {
        val folder = File(folderPath)
        if (folder.exists() && folder.isDirectory) {
            val files = folder.listFiles { file ->
                file.isFile && file.extension.toLowerCase() in listOf("mp4", "avi", "mkv", "mov") // Add more extensions as needed
            }
            if (files != null) {
                videoFiles.addAll(files)
            }
        }
    }

    return videoFiles
}
