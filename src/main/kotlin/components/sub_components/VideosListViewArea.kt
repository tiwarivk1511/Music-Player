package components.sub_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.io.File

@Composable
fun VideoListViewArea(modifier: Modifier, videoFolderPaths: List<String>) {
    var selectedVideoPath by remember { mutableStateOf<String?>(null) }

    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF1E1E1E))
        ) {
            // Iterate through each video folder path
            for (folderPath in videoFolderPaths) {
                val folder = File(folderPath)

                // List files in the folder that are video files
                folder.listFiles { file ->
                    file.isFile && file.extension.lowercase() in listOf("mp4", "avi", "mkv", "mov") // Adjust extensions as needed
                }?.forEachIndexed { index, file ->

                    VideoCards(
                        title = file.nameWithoutExtension,
                        subtitle = file.absolutePath,
                        description = "Duration: ${file.length() / 1000} seconds",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(bottom = if (index == ((folder.listFiles()?.size ?: 0) - 1)) 0.dp else 2.dp),
                        onCardClick = { selectedVideoPath = file.absolutePath },
                        onAddFavoriteClick = { /* Handle favorite click */ }
                    )
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
