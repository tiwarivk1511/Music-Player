package screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import components.sub_components.ListCards
import components.sub_components.addToFavorites
import java.io.File
import java.util.prefs.Preferences
import javax.swing.JOptionPane

@Composable
fun AudioScreen(
    onNavigate: () -> Unit,
    folderPaths: List<String>,
) {
    val verticalScrollState = rememberScrollState()
    val mediaFiles = remember { scanMediaFiles(folderPaths) }

    // MediaPlayer state
    val mediaPlayer = remember { MediaPlayerController() }
    var isPlaying by remember { mutableStateOf(false) }

    // Favorite state
    val favoriteMedia = remember { mutableStateListOf<String>().apply { addAll(loadFavoriteMedia()) } }

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
                // Display media files
                mediaFiles.forEach { file ->
                    ListCards(
                        file.nameWithoutExtension,
                        Modifier
                            .fillMaxWidth()
                            .height(65.dp),
                        onCardClick = {
                            if (file.extension.toLowerCase() == "mp3") {
                                mediaPlayer.playAudioFile(file)
                                isPlaying = mediaPlayer.isPlaying()
                            } else {
                                // Handle video playback or unsupported formats
                                // Example: mediaPlayer.playVideoFile(file)
                                // For now, display error message
                                showErrorDialog("Unsupported file format: ${file.extension}")
                            }
                        },
                        onFavoriteClick = {
                            addToFavorites(file)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

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

// Function to scan specified folder paths for media files (both audio and video)
fun scanMediaFiles(folderPaths: List<String>): List<File> {
    val mediaFiles = mutableListOf<File>()

    folderPaths.forEach { path ->
        val folder = File(path)
        if (folder.exists() && folder.isDirectory) {
            folder.walkTopDown().forEach { file ->
                if (file.isFile && file.extension.toLowerCase() in listOf("mp3", "wav", "mp4", "avi", "mkv")) {
                    mediaFiles.add(file)
                }
            }
        }
    }

    return mediaFiles
}

// Function to save favorite media paths
fun saveFavoriteMedia(favoriteMedia: List<String>) {
    val prefs = Preferences.userRoot().node("com.example.musicplayer.settings")
    prefs.put("favoriteMedia", favoriteMedia.joinToString(";"))
    prefs.flush()
}

// Function to load favorite media paths
fun loadFavoriteMedia(): List<String> {
    val prefs = Preferences.userRoot().node("com.example.musicplayer.settings")
    val favoriteMedia = prefs.get("favoriteMedia", "")
    return if (favoriteMedia.isNotEmpty()) favoriteMedia.split(";") else emptyList()
}

// Function to show an error dialog
fun showErrorDialog(message: String) {
    JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE)
}
