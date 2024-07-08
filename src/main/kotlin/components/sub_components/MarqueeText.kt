package components.sub_components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MarqueeText(notice: String) {
    var scrollState by remember { mutableStateOf(0f) }

    val infiniteTransition = rememberInfiniteTransition()
    val scrollAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    LaunchedEffect(Unit) {
        scrollState = withFrameNanos { time ->
            (time / 1e6).toFloat() // Initialize scrollState with current time in milliseconds
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray)
            .padding(4.dp)
    ) {
        Text(
            text = notice,
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier
                .offset(x = -(scrollAnim * 300).dp) // Adjust multiplier for faster or slower scroll speed
                .padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.body1
        )
    }
}
