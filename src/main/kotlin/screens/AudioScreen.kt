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
import components.sub_components.addToFavorites
import java.io.File
import java.util.prefs.Preferences
import javax.swing.JOptionPane

@Composable
fun AudioScreen(
    onNavigate: () -> Unit,
    folderPaths: List<String>
) {
    val verticalScrollState = rememberScrollState()

    // MediaPlayer state
    var isPlaying by remember { mutableStateOf(false) }

    // Favorite state
    val favoriteAudioList by remember { derivedStateOf { loadFavoriteMedia() } }

    // Function to handle unsupported file format error
    fun handleUnsupportedFormat(file: File) {
        showErrorDialog("Unsupported file format: ${file.extension}")
    }

    // Function to play audio file
    fun playAudio(file: File) {
        MediaPlayerController.playAudioFile(file)
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
                    text = "Audio",
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
                    val isFavorite = favoriteAudioList.contains(file.path)
                    ListCards(
                        soundName = file.nameWithoutExtension,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp),
                        onCardClick = {
                            when (file.extension.toLowerCase()) {
                                "mp3", "wav" -> playAudio(file)
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
                        favoriteList = favoriteAudioList
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
