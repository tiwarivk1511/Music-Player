package components.sub_components

import Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import screens.MediaPlayerController
import screens.saveFavoriteAudio
import screens.saveFavoriteVideos
import java.io.File

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
                        mediaPlayerController.load(listOf(file))
                        mediaPlayerController.play()
                    },
                    onFavoriteClick = {
                        addToFavorites(file)
                    }
                )
            }
        }
    }
}

fun addToFavorites(file: File) {
    val filePath = file.absolutePath
    when (file.extension) {
        "mp3" -> {
            if (!Settings.favoriteAudio.contains(filePath)) {
                Settings.favoriteAudio.add(file.nameWithoutExtension)
                saveFavoriteAudio(Settings.favoriteAudio)
            }
        }
        "mp4" -> {
            if (!Settings.favoriteVideos.contains(filePath)) {
                Settings.favoriteVideos.add(filePath)
                saveFavoriteVideos(Settings.favoriteVideos)
            }
        }
        else -> {
            // Handle other file types if needed
        }
    }
}

