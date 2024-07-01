package components.sub_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun AlbumCards(
    albumName: String,
    artistName: String,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp)
            .height(150.dp)
            .clickable(onClick = onCardClick),
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
                    text = albumName,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = artistName,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Image(
                painter = painterResource("drawable/play_button.png"),
                contentDescription = "Play Icon",
                modifier = Modifier
                    .size(30.dp)
                    .padding(8.dp),
                alpha = 0.5f
            )
        }
    }
}
