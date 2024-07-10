package components.sub_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import screens.loadFavoriteMedia
import screens.saveFavoriteMedia
import java.io.File

@Composable
fun VideoListViewArea(modifier: Modifier = Modifier, videoFolderPaths: List<String>) {
    var selectedVideoPath by remember { mutableStateOf<String?>(null) }

    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF1E1E1E))
                .padding(8.dp)
        ) {
            // Iterate through each video folder path
            videoFolderPaths.forEach { folderPath ->
                val folder = File(folderPath)
                if (folder.exists() && folder.isDirectory) {
                    val videoFiles = folder.listFiles { file ->
                        file.isFile && file.extension.lowercase() in listOf("mp4", "avi", "mkv", "mov") // Adjust extensions as needed
                    }

                    videoFiles?.forEachIndexed { index, file ->
                        VideoListCards(
                            videoName = file.nameWithoutExtension,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .padding(bottom = if (index == (videoFiles.size - 1)) 0.dp else 2.dp),
                            onCardClick = { selectedVideoPath = file.absolutePath },
                            onFavoriteClick = {  addToFavorites(file)
                                // Update the favorite list after adding or removing from favorites
                                saveFavoriteMedia(loadFavoriteMedia()) },
                            favoriteList = emptyList(), // Pass the actual favorite list here
                            onPauseClick = { /* Handle pause click */ },
                            isPlaying = false // Update this based on actual state
                        )
                    }
                }
            }
        }

        selectedVideoPath?.let { videoPath ->
            FullScreenVideoPlayer(
                videoPath = videoPath,
                onClose = { selectedVideoPath = null }
            )
        }
    }
}
