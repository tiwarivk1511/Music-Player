package components.sub_components

import MediaPlayerController
import Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import screens.saveFavoriteAudio
import screens.saveFavoriteVideos
import java.io.File
import java.util.prefs.Preferences
import javax.swing.JOptionPane

@Composable
fun SongListViewArea(
    modifier: Modifier,
    audioFolderPaths: List<String>,
    mediaPlayerController: MediaPlayerController,
) {
    val audioFiles = remember(audioFolderPaths) {
        audioFolderPaths.flatMap { folderPath ->
            val folder = File(folderPath)
            if (folder.exists() && folder.isDirectory) {
                folder.listFiles { file -> file.extension.equals("mp3", ignoreCase = true) }?.toList() ?: emptyList()
            } else {
                emptyList()
            }
        }
    }

    val favoriteAudioList by remember { derivedStateOf { Settings.favoriteAudio.toList() } }

    Card(
        modifier = modifier,
        backgroundColor = Color(0xFF1E1E1E)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
        ) {
            audioFiles.forEach { file ->
                ListCards(
                    soundName = file.nameWithoutExtension,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(bottom = 2.dp),
                    onCardClick = {
                        mediaPlayerController.load(file.absolutePath)
                        mediaPlayerController.play()
                    },
                    onFavoriteClick = {
                        addToFavorites(file)
                    },
                    favoriteList = favoriteAudioList,
                    onPauseClick = {
                        mediaPlayerController.pause()
                    },
                    isPlaying = mediaPlayerController.isPlaying()
                )
            }
        }
    }
}

fun showErrorDialog(message: String) {
    JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE)
}

fun addToFavorites(file: File) {
    when (file.extension.toLowerCase()) {
        "mp3" -> {
            if (!Settings.favoriteAudio.contains(file.nameWithoutExtension)) {
                Settings.favoriteAudio.add(file.nameWithoutExtension)
                saveFavoriteAudio(Settings.favoriteAudio)
            }
        }
        "mp4" -> {
            if (!Settings.favoriteVideos.contains(file.nameWithoutExtension)) {
                Settings.favoriteVideos.add(file.nameWithoutExtension)
                saveFavoriteVideos(Settings.favoriteVideos)
            }
        }
        else -> {
            showErrorDialog("Unsupported file format: ${file.extension}")
        }
    }
}

// Function to save favorite audio paths
fun saveFavoriteAudio(favoriteAudio: List<String>) {
    val prefs = Preferences.userRoot().node("com.example.musicplayer.settings")
    prefs.put("favoriteAudio", favoriteAudio.joinToString(";"))
    prefs.flush()
}

// Function to save favorite video paths
fun saveFavoriteVideos(favoriteVideos: List<String>) {
    val prefs = Preferences.userRoot().node("com.example.musicplayer.settings")
    prefs.put("favoriteVideos", favoriteVideos.joinToString(";"))
    prefs.flush()
}
