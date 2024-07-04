package components.sub_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import java.awt.Desktop
import java.io.File
import java.io.IOException

@Composable
fun FullScreenVideoPlayer(videoPath: String, onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Launch the default system media player for the video file
        try {
            val file = File(videoPath)
            if (file.exists()) {
                Desktop.getDesktop().open(file)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Close button
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            Icon(
                painter = painterResource("drawable/ic_close.png"),
                contentDescription = "Close",
                tint = Color.White
            )
        }
    }
}
