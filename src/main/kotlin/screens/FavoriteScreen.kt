package screens

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
import androidx.compose.ui.unit.sp
import components.sub_components.FavoriteCard
import kotlinx.coroutines.delay
import java.io.File
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
                    text = "Favorites",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.h5,
                )

            }

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

@Composable
fun FavoriteAudioViewArea(modifier: Modifier) {
    val favoriteAudioList by rememberUpdatedState(Settings.favoriteAudio.toList())

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            val prefs = Preferences.userRoot().node("com.example.musicplayer.settings")
            val favoriteAudioString = prefs.get("favoriteAudio", "")
            Settings.favoriteAudio.clear()
            Settings.favoriteAudio.addAll(favoriteAudioString.split(";").filter { it.isNotBlank() }.toSet())
        }
    }

    Column(modifier = modifier.fillMaxHeight()) {
        favoriteAudioList.forEach { audioPath ->
            FavoriteCard(
                name = File(audioPath).nameWithoutExtension,
                modifier = Modifier.fillMaxWidth(),
                isAudio = true,
                onCardClick = {
                    MediaPlayerController.playAudioFile(audioPath)
                },
                onRemoveClick = {
                    Settings.favoriteAudio.remove(audioPath)
                    saveFavoriteAudio(Settings.favoriteAudio)
                    removeFavoriteAudioFromPreferences(audioPath)
                }
            )
        }
    }
}

@Composable
fun FavoriteVideoViewArea(modifier: Modifier) {
    val favoriteVideosList by rememberUpdatedState(Settings.favoriteVideos.toList())

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            val prefs = Preferences.userRoot().node("com.example.musicplayer.settings")
            val favoriteVideoString = prefs.get("favoriteVideos", "")
            Settings.favoriteVideos.clear()
            Settings.favoriteVideos.addAll(favoriteVideoString.split(";").filter { it.isNotBlank() }.toSet())
        }
    }

    Column(modifier = modifier.fillMaxHeight()) {
        favoriteVideosList.forEach { videoPath ->
            FavoriteCard(
                name = File(videoPath).nameWithoutExtension,
                modifier = Modifier.fillMaxWidth(),
                isAudio = false,
                onCardClick = { /* Play video */ },
                onRemoveClick = {
                    Settings.favoriteVideos.remove(videoPath)
                    saveFavoriteVideos(Settings.favoriteVideos)
                    removeFavoriteVideoFromPreferences(videoPath)
                }
            )
        }
    }
}

fun removeFavoriteAudioFromPreferences(audioPath: String) {
    val prefs = Preferences.userRoot().node("com.example.musicplayer.settings")
    val currentFavoriteAudio = prefs.get("favoriteAudio", "").split(";").filter { it.isNotBlank() }.toMutableList()
    currentFavoriteAudio.remove(audioPath)
    prefs.put("favoriteAudio", currentFavoriteAudio.joinToString(separator = ";"))
}

fun removeFavoriteVideoFromPreferences(videoPath: String) {
    val prefs = Preferences.userRoot().node("com.example.musicplayer.settings")
    val currentFavoriteVideos = prefs.get("favoriteVideos", "").split(";").filter { it.isNotBlank() }.toMutableList()
    currentFavoriteVideos.remove(videoPath)
    prefs.put("favoriteVideos", currentFavoriteVideos.joinToString(separator = ";"))
}

fun saveFavoriteAudio(favoriteAudio: List<String>) {
    val prefs = Preferences.userRoot().node("com.example.musicplayer.settings")
    prefs.put("favoriteAudio", favoriteAudio.joinToString(separator = ";"))
}

fun saveFavoriteVideos(favoriteVideos: List<String>) {
    val prefs = Preferences.userRoot().node("com.example.musicplayer.settings")
    prefs.put("favoriteVideos", favoriteVideos.joinToString(separator = ";"))
}

object Settings {
    var favoriteVideos = mutableStateListOf<String>()
    var favoriteAudio = mutableStateListOf<String>()

    init {
        val prefs = Preferences.userRoot().node("com.example.musicplayer.settings")
        val favoriteAudioString = prefs.get("favoriteAudio", "")
        favoriteAudio.addAll(favoriteAudioString.split(";").filter { it.isNotBlank() }.toSet())

        val favoriteVideoString = prefs.get("favoriteVideos", "")
        favoriteVideos.addAll(favoriteVideoString.split(";").filter { it.isNotBlank() }.toSet())
    }
}
