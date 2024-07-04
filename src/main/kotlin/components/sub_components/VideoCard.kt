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
fun VideoCards(

    title: String,
    subtitle: String,
    description: String,
    modifier: Modifier,
    onCardClick: () -> Unit,
    onAddFavoriteClick: () -> Unit,
) {
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
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource("drawable/ic_colored_video.png"),
                contentDescription = "Album Image",
                modifier = Modifier
                    .size(40.dp)
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
                    text = title,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

            }

            Image(
                painter = painterResource("drawable/play_button.png"),
                contentDescription = "Play Icon",
                modifier = Modifier
                    .size(40.dp)
                    .padding(8.dp)
                    .clickable {onCardClick},
                alpha = 0.5f
            )

            Image(
                painter = painterResource("drawable/ic_favorite.png"),
                contentDescription = "Play Icon",
                modifier = Modifier
                    .size(40.dp)
                    .padding(8.dp)
                    .clickable {onAddFavoriteClick},
                alpha = 0.5f
            )
        }
    }
}
