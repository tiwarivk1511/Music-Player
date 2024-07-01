package components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun BottomControl(
    songName: String,
    artistName: String,
    isPlaying: Boolean,
    onPlayPauseToggle: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onVolumeUp: () -> Unit, // Callback for volume up action
    onVolumeDown: () -> Unit, // Callback for volume down action
    onShuffleToggle: () -> Unit, // Callback for shuffle toggle action
    currentPosition: Float, // Current position of the song in seconds
    totalDuration: Float, // Total duration of the song in seconds
    onSeek: (Float) -> Unit, // Callback for seek action
    modifier: Modifier = Modifier

) {

    // State to hold the current position of the song
    var currentPosition by remember{ mutableStateOf(0f) }

    Card(
        modifier = modifier.padding(4.dp),
        shape = RoundedCornerShape(15.dp),
        backgroundColor = Color(0xFF121212),
        elevation = 8.dp,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()

        ) {
            // Previous Button
            IconButton(onClick = { onPrevious() }) {
                Icon(
                    painter = painterResource("drawable/rewind.png"),
                    contentDescription = "Previous",
                    modifier = Modifier.size(24.dp), // Set icon size
                    tint = Color.White // Set icon color to white
                )
            }

            // Play/Pause Button
            IconButton(onClick = { onPlayPauseToggle() }) {
                Icon(
                    painter = if (isPlaying) painterResource("drawable/play_button.png") else painterResource("drawable/pause_button.png"),
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    modifier = Modifier.size(24.dp), // Set icon size
                    tint = Color.White // Set icon color to white
                )
            }

            // Next Button
            IconButton(onClick = { onNext() }) {
                Icon(
                    painter = painterResource("drawable/next_song.png"),
                    contentDescription = "Next",
                    modifier = Modifier.size(24.dp), // Set icon size
                    tint = Color.White // Set icon color to white
                )
            }

                // Song Info (Name and Artist)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = songName,
                        style = MaterialTheme.typography.subtitle1,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = artistName,
                        style = MaterialTheme.typography.caption,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Seekbar (Slider)
                    Slider(
                        value = currentPosition,
                        onValueChange = {
                            currentPosition = it
                            onSeek(it) // Callback to notify about the seek position
                        },
                        valueRange = 0f..totalDuration,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = true,
                        colors = SliderDefaults.colors(
                            thumbColor = Color.White,
                            activeTrackColor = Color.White,
                            inactiveTrackColor = Color.Gray // Optional, to set the inactive track color
                        ),
                        onValueChangeFinished = {
                            // Optional: Perform any actions needed after user finishes interaction
                        }
                    )
                }




            // Volume Control (Adjust volume)
            IconButton(onClick = { onVolumeDown() }) {
                Icon(
                    painter = painterResource("drawable/volume_down.png"),
                    contentDescription = "Volume Down",
                    modifier = Modifier.size(24.dp), // Set icon size
                    tint = Color.White // Set icon color to white
                )
            }
            IconButton(onClick = { onVolumeUp() }) {
                Icon(
                    painter = painterResource("drawable/volume_up.png"),
                    contentDescription = "Volume Up",
                    modifier = Modifier.size(24.dp), // Set icon size
                    tint = Color.White // Set icon color to white
                )
            }

            // Shuffle Toggle Button
            IconButton(onClick = { onShuffleToggle() }) {
                Icon(
                    painter = painterResource("drawable/shuffle.png"),
                    contentDescription = "Shuffle",
                    modifier = Modifier.size(24.dp), // Set icon size
                    tint = Color.White // Set icon color to white
                )
            }
        }
    }
}
