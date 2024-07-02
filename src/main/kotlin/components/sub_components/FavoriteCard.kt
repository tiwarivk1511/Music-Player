package components.sub_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun FavoriteCard(
    name: String,
    filepath: String,
    modifier: Modifier,
    isAudio: Boolean,
    onCardClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    if (isAudio) {
        FavoriteAudioCards(name, filepath, modifier, onCardClick, onRemoveClick)
    } else {
        FavoriteVideoCards(name, filepath,modifier, onCardClick, onRemoveClick)
    }
}



@Composable
fun FavoriteVideoCards(
    videoName: String,
    filepath: String,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp)
            .height(50.dp),
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color(0xFF676565),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp),
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
                    .padding(horizontal = 2.dp)
            ) {
                Text(
                    text = videoName,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

            }

            Image(
                painter = painterResource("drawable/play_button.png"),
                contentDescription = "Play Icon",
                modifier = Modifier
                    .size(30.dp)
                    .padding(8.dp)
                    .clickable(onClick = onCardClick),
                alpha = 0.5f
            )

            Image(
                painter = painterResource("drawable/ic_remove_favorites.png"),
                contentDescription = "Remove Icon",
                modifier = Modifier
                    .size(40.dp)
                    .padding(8.dp)
                    .clickable(onClick = onRemoveClick),
                colorFilter = ColorFilter.tint(Color.White),
                alpha = 0.5f
            )
        }
    }
}


@Composable
fun FavoriteAudioCards(
    audioName: String,
    filepath: String,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp)
            .height(50.dp),
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color(0xFF676565),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp),
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
                    .padding(horizontal = 2.dp)
            ) {
                Text(
                    text = audioName,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

            }

            Image(
                painter = painterResource("drawable/play_button.png"),
                contentDescription = "Play Icon",
                modifier = Modifier
                    .size(30.dp)
                    .padding(8.dp)
                    .clickable(onClick = onCardClick),
                alpha = 0.5f
            )

            Image(
                painter = painterResource("drawable/ic_remove_favorites.png"),
                contentDescription = "Remove Icon",
                modifier = Modifier
                    .size(40.dp)
                    .padding(8.dp)
                    .clickable(onClick = onRemoveClick),
                colorFilter = ColorFilter.tint(Color.White),
                alpha = 0.5f
            )
        }
    }
}