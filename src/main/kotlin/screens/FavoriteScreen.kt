package screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.sub_components.FavoriteCard
import java.util.prefs.Preferences

@Composable
fun FavoritesScreen(onNavigate: () -> Unit) {
    val verticalScrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
                .padding(16.dp)
        ) {
            Text(
                text = "Favorites",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.h5,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(verticalScrollState)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "Favorite Videos",
                        fontSize = 20.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Favorite Audio",
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FavoriteVideoViewArea(modifier = Modifier.weight(1f).fillMaxHeight())
                    FavoriteAudioViewArea(modifier = Modifier.weight(1f).fillMaxHeight())
                }
            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(verticalScrollState)
        )
    }
}

// Favorite Audio view Area
@Composable
fun FavoriteAudioViewArea(modifier: Modifier) {
    val favoriteAudioList = Settings.favoriteAudio.toMutableList()

    Column(modifier = modifier.fillMaxHeight()) {
        favoriteAudioList.forEach { audioPath ->
            FavoriteCard(
                name = audioPath.substringAfterLast("/"),
                filepath = audioPath,
                modifier = Modifier.fillMaxWidth(),
                isAudio = true,
                onCardClick = { /* Play audio */ },
                onRemoveClick = {
                    Settings.favoriteAudio.remove(audioPath)
                    saveFavoriteAudio(Settings.favoriteAudio)
                    removeFavoriteAudioFromPreferences(audioPath)
                }
            )
        }
    }
}

// Function to remove a favorite audio path from preferences
fun removeFavoriteAudioFromPreferences(audioPath: String) {
    val prefs = Preferences.userRoot().node("com.example.musicplayer.settings")
    val currentFavoriteAudio = prefs.get("favoriteAudio", "").split(";").filter { it.isNotBlank() }.toMutableList()
    currentFavoriteAudio.remove(audioPath)
    prefs.put("favoriteAudio", currentFavoriteAudio.joinToString(separator = ";"))
}

@Composable
fun FavoriteVideoViewArea(modifier: Modifier) {
    val favoriteVideosList = Settings.favoriteVideos.toMutableList()

    Column(modifier = modifier.fillMaxHeight()) {
        favoriteVideosList.forEach { videoPath ->
            FavoriteCard(
                name = "Show the file's name",
                filepath = videoPath,
                modifier = Modifier.fillMaxWidth(),
                isAudio = false,
                onCardClick = { /* Play video */ },
                onRemoveClick = {
                    Settings.favoriteVideos.remove(videoPath)
                    removeFavoriteVideoFromPreferences(videoPath)
                    saveFavoriteVideos(Settings.favoriteVideos)
                }
            )
        }
    }
}

// Function to remove a favorite video path from preferences
fun removeFavoriteVideoFromPreferences(videoPath: String) {
    val prefs = Preferences.userRoot().node("com.example.musicplayer.settings")
    val currentFavoriteVideos = prefs.get("favoriteVideos", "").split(";").filter { it.isNotBlank() }.toMutableList()
    currentFavoriteVideos.remove(videoPath)
    prefs.put("favoriteVideos", currentFavoriteVideos.joinToString(separator = ";"))
}

// Function to save favorite audio paths
fun saveFavoriteAudio(favoriteAudio: List<String>) {
    val prefs = Preferences.userRoot().node("com.example.musicplayer.settings")
    prefs.put("favoriteAudio", favoriteAudio.joinToString(separator = ";"))
}

// Function to save favorite video paths
fun saveFavoriteVideos(favoriteVideos: List<String>) {
    val prefs = Preferences.userRoot().node("com.example.musicplayer.settings")
    prefs.put("favoriteVideos", favoriteVideos.joinToString(separator = ";"))
}

// Update Settings object to hold favorite audio and video lists
object Settings {
    var audioFolders = loadAudioFolderPaths()
    var videoFolders = loadVideoFolderPaths()
    var favoriteVideos = mutableStateListOf<String>()
    var favoriteAudio = mutableStateListOf<String>()

    // Load favorite audio and video paths
    init {
        val prefs = Preferences.userRoot().node("com.example.musicplayer.settings")
        val favoriteAudioString = prefs.get("favoriteAudio", "")
        favoriteAudio.addAll(favoriteAudioString.split(";").filter { it.isNotBlank() })

        val favoriteVideoString = prefs.get("favoriteVideos", "")
        favoriteVideos.addAll(favoriteVideoString.split(";").filter { it.isNotBlank() })
    }
}
