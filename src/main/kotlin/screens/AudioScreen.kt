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
import androidx.compose.ui.unit.dp
import components.BottomControl
import components.sub_components.ListCards
import java.io.File

@Composable
fun AudioScreen(
    onNavigate: () -> Unit,
    folderPaths: List<String>
) {
    val verticalScrollState = rememberScrollState()
    val audioFiles = remember { scanAudioFiles(folderPaths) }

    // MediaPlayer state
    val mediaPlayer = remember { MediaPlayerController() }

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
                // Display audio files
                audioFiles.forEach { file ->
                    ListCards(
                        file.nameWithoutExtension,
                        file.parentFile?.name ?: "Unknown Artist",
                        file.absolutePath,
                        Modifier.fillMaxWidth().height(65.dp),
                        onCardClick = { /* Handle card click */ },
                        onFavoriteClick = { /* Handle favorite click */ }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Fixed bottom control bar
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

        // Vertical scrollbar (optional)
        VerticalScrollbar(
            modifier = Modifier
                .fillMaxHeight()
                .width(8.dp)
                .align(Alignment.CenterEnd),
            adapter = rememberScrollbarAdapter(verticalScrollState)
        )
    }
}

// Function to scan specified folder paths for audio files
fun scanAudioFiles(folderPaths: List<String>): List<File> {
    val audioFiles = mutableListOf<File>()

    folderPaths.forEach { path ->
        val folder = File(path)
        if (folder.exists() && folder.isDirectory) {
            folder.walkTopDown().forEach { file ->
                if (file.isFile && file.extension in listOf("mp3", "wav")) {
                    audioFiles.add(file)
                }
            }
        }
    }

    return audioFiles
}
