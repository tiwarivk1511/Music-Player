package components.sub_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sun.jna.NativeLibrary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer
import javax.swing.JFrame
import javax.swing.SwingUtilities

@Composable
fun FullScreenVideoPlayer(videoPath: String, onClose: () -> Unit) {
    // Initialize VLCJ and ensure native libraries are found
    NativeLibrary.addSearchPath("libvlc", videoPath)
    NativeDiscovery().discover()

    // LaunchedEffect to handle window creation and video playback
    LaunchedEffect(videoPath) {
        withContext(Dispatchers.Swing) {
            // Create JFrame for video playback
            val frame = JFrame()

            frame.title = "Video Player"
            frame.isResizable = true
            frame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE

            // Create VLC media player component
            val mediaPlayerComponent = EmbeddedMediaPlayerComponent()
            val mediaPlayer = mediaPlayerComponent.mediaPlayer()

            // Set up media player
            mediaPlayer.media().prepare(videoPath)

            // Create a close button overlay using Compose content
            SwingUtilities.invokeLater {
                val composeWindow = ComposeWindow()
                composeWindow.setSize(800, 600) // Same size as video player
                composeWindow.setLocationRelativeTo(frame) // Position relative to video frame
                composeWindow.setContent {
                    Box(
                        modifier = Modifier
                            .background(Color.Black)
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = Color.Black
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                IconButton(
                                    onClick = { mediaPlayer.controls().pause() },
                                    modifier = Modifier.align(Alignment.CenterStart)
                                ) {
                                    Icon(
                                        painter = painterResource("drawable/ic_pause.png"),
                                        contentDescription = "Pause",
                                        tint = Color.White
                                    )
                                }

                                IconButton(
                                    onClick = { mediaPlayer.controls().play() },
                                    modifier = Modifier.align(Alignment.Center)
                                ) {
                                    Icon(
                                        painter = painterResource("drawable/ic_play.png"),
                                        contentDescription = "Play",
                                        tint = Color.White
                                    )
                                }

                                IconButton(
                                    onClick = { mediaPlayer.controls().stop() },
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                ) {
                                    Icon(
                                        painter = painterResource("drawable/ic_stop.png"),
                                        contentDescription = "Stop",
                                        tint = Color.White
                                    )
                                }
                            }

                            Slider(
                                value = remember { mediaPlayer.audio().volume().toFloat() },
                                onValueChange = {
                                    mediaPlayer.audio().setVolume(it.toInt())
                                },
                                valueRange = 0f..100f,
                                steps = 1,
                                modifier = Modifier.align(Alignment.BottomCenter)
                                    .padding(bottom = 16.dp)
                            )
                        }
                    }
                }
                composeWindow.isVisible = true
            }

            // Start playback
           // mediaPlayer.play()

            // Add media player component to JFrame
            frame.contentPane.add(mediaPlayerComponent)
            frame.setSize(800, 600) // Set window size
            frame.setLocationRelativeTo(null) // Center window on screen
            frame.isVisible = true
        }
    }
}
