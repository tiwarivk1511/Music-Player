package screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.io.File

object MediaPlayerState {
    var isPlaying by mutableStateOf(false)
    var currentFile: File? by mutableStateOf(null)
    var currentSongName: String by mutableStateOf("")
    var currentArtistName: String by mutableStateOf("")
}
