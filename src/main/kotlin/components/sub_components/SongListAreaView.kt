package components.sub_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SongListViewArea(modifier: Modifier) {

    Card(
        modifier = modifier
    ) {
        Column(modifier = Modifier.fillMaxSize()
            .background(color = Color(0xFF1E1E1E))) {

            repeat(6) {
                ListCards(
                    soundName = "Song Name",
                    artistName = "Artist Name",
                    albumName = "Album Name",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(bottom = 2.dp),
                    onCardClick = {}
                )
            }
        }

    }
}
