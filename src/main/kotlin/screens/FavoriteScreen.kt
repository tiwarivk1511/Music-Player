package screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.sub_components.*

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
    Column(modifier = modifier.fillMaxHeight()) {
        FavoriteCard(
            name = "Audio Name",
            filepath = "filepath",
            modifier = Modifier.fillMaxWidth(),
            isAudio = true,
            onCardClick = {},
            onRemoveClick = {}
        )
    }
}

// Favorite Video view Area
@Composable
fun FavoriteVideoViewArea(modifier: Modifier) {
    Column(modifier = modifier.fillMaxHeight()) {
        FavoriteCard(
            name = "Video Name",
            filepath = "filepath",
            modifier = Modifier.fillMaxWidth(),
            isAudio = false,
            onCardClick = {},
            onRemoveClick = {}
        )
    }
}
