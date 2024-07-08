package components.sub_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ListCards(
    soundName: String,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    favoriteList: List<String>,  // Assuming favoriteList contains names of favorite items
    onPauseClick: () -> Unit,
    isPlaying: Boolean
) {
    // Determine if the current song is a favorite on initial load
    var currentIsPlaying by remember { mutableStateOf(isPlaying) }
    var isFavorite by remember { mutableStateOf(favoriteList.contains(soundName)) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp)
            .height(150.dp),
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color(0xFF676565),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource("drawable/ic_album.png"),
                contentDescription = "Album Image",
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 8.dp),
                alpha = 0.5f
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = soundName,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Play/Pause Icon
            Image(
                painter = painterResource(if (currentIsPlaying) "drawable/pause_button.png" else "drawable/play_button.png"),
                contentDescription = if (currentIsPlaying) "Pause Icon" else "Play Icon",
                modifier = Modifier
                    .size(30.dp)
                    .padding(8.dp)
                    .clickable {
                        if (currentIsPlaying) {
                            onPauseClick()
                        } else {
                            onCardClick()
                        }
                        currentIsPlaying = !currentIsPlaying  // Toggle currentIsPlaying state
                    },
                alpha = 0.5f
            )

            // Favorite Icon
            Image(
                painter = painterResource(if (isFavorite) "drawable/ic_favorites_filled.png" else "drawable/ic_favorites.png"),
                contentDescription = if (isFavorite) "Remove from Favorites Icon" else "Add to Favorites Icon",
                modifier = Modifier
                    .size(40.dp)
                    .padding(8.dp)
                    .clickable {
                        isFavorite = !isFavorite  // Toggle isFavorite state
                        onFavoriteClick()

                    },
                alpha = 0.5f
            )
        }
    }
}