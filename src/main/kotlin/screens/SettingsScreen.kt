package screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.prefs.Preferences
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.FloatControl
import javax.swing.JFileChooser

@Composable
fun SettingsScreen(
    onNavigate: () -> Unit,
    onAudioFoldersSelected: (List<String>) -> Unit,
    onVideoFoldersSelected: (List<String>) -> Unit
) {
    val audioFolders = remember { mutableStateListOf<String>() }
    val videoFolders = remember { mutableStateListOf<String>() }
    val verticalScrollState = rememberScrollState()
    var systemVolume by remember { mutableStateOf(getSystemVolume().toFloat()) }
    var selectedAudioQuality by remember { mutableStateOf("Low") }

    // Launch a coroutine to observe system volume changes
    LaunchedEffect(Unit) {
        while (true) {
            val currentVolume = getSystemVolume().toFloat()
            if (systemVolume != currentVolume) {
                systemVolume = currentVolume
            }
            delay(1000) // Adjust the delay as needed
        }
    }

    // Load saved audio and video folder paths on start
    LaunchedEffect(Unit) {
        audioFolders.addAll(loadAudioFolderPaths())
        videoFolders.addAll(loadVideoFolderPaths())
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(verticalScrollState)
                .background(Color.DarkGray)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource("drawable/ic_settings.png"),
                    contentDescription = "Album Image",
                    modifier = Modifier.size(40.dp).align(Alignment.CenterVertically),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Settings",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

            }

//            DropdownSettingItem(
//                option = SettingOption(
//                    title = "Audio Quality",
//                    description = "Select audio quality",
//                    type = SettingType.DROPDOWN,
//                    icon = painterResource("drawable/volume_up.png")
//                ),
//                selectedItem = selectedAudioQuality,
//                onItemSelected = { newQuality ->
//                    selectedAudioQuality = newQuality
//                    // Handle the selected quality as needed
//                },
//                items = listOf("Low", "Medium", "High")
//            )

            SliderSettingItem(
                option = SettingOption(
                    title = "Volume",
                    description = "Adjust volume level",
                    type = SettingType.SLIDER,
                    icon = painterResource("drawable/volume_up.png")
                ),
                volumeLevel = systemVolume,
                onVolumeChange = { newVolume ->
                    systemVolume = newVolume
                    setSystemVolume(newVolume.toInt())
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            AudioFolderSelectionSection(audioFolders) {
                saveAudioFolderPaths(audioFolders)
                onAudioFoldersSelected(audioFolders)
            }

            Spacer(modifier = Modifier.height(16.dp))

            VideoFolderSelectionSection(videoFolders) {
                saveVideoFolderPaths(videoFolders)
                onVideoFoldersSelected(videoFolders)
            }
        }
    }
}

data class SettingOption(
    val title: String,
    val description: String,
    val type: SettingType,
    val icon: Painter
)

enum class SettingType {
    DROPDOWN,
    SLIDER,
}

@Composable
fun DropdownSettingItem(
    option: SettingOption,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    items: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = option.icon,
                contentDescription = option.title,
                modifier = Modifier.padding(end = 8.dp).size(24.dp),
                tint = Color.White
            )
            Column {
                Text(
                    text = option.title,
                    style = MaterialTheme.typography.body1,
                    color = Color.White
                )
                Text(
                    text = option.description,
                    style = MaterialTheme.typography.body2,
                    color = Color.White
                )
            }
        }

        Box(
            modifier = Modifier.clickable { expanded = !expanded },
            contentAlignment = Alignment.Center
        ) {
            Button(
                modifier = Modifier.width(200.dp),
                onClick = { expanded = true }
            ) {
                Text(text = selectedItem)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(onClick = {
                        onItemSelected(item)
                        expanded = false
                    }) {
                        Text(text = item)
                    }
                }
            }
        }
    }
}

@Composable
fun SliderSettingItem(option: SettingOption, volumeLevel: Float, onVolumeChange: (Float) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = option.icon,
                contentDescription = option.title,
                modifier = Modifier.padding(end = 8.dp).size(24.dp),
                tint = Color.White
            )
            Column {
                Text(
                    text = option.title,
                    style = MaterialTheme.typography.body1,
                    color = Color.White
                )
                Text(
                    text = option.description,
                    style = MaterialTheme.typography.body2,
                    color = Color.White
                )
            }
        }

        Slider(
            value = volumeLevel,
            onValueChange = {
                onVolumeChange(it)
            },
            valueRange = 0f..100f,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTickColor = Color.White,
                inactiveTickColor = Color.LightGray
            )
        )
    }
}

// Audio volume control functions
fun setSystemVolume(volumePercent: Int) {
    val volume = volumePercent / 100.0f
    val mixerInfo = AudioSystem.getMixerInfo().firstOrNull() ?: return
    val mixer = AudioSystem.getMixer(mixerInfo)
    val targetLine = mixer.getLine(mixer.targetLineInfo[0])
    targetLine.open()
    val control = targetLine.getControl(FloatControl.Type.VOLUME) as FloatControl
    control.value = volume.coerceIn(control.minimum, control.maximum)
}

fun getSystemVolume(): Int {
    val mixerInfo = AudioSystem.getMixerInfo().firstOrNull() ?: return 0
    val mixer = AudioSystem.getMixer(mixerInfo)
    val targetLine = mixer.getLine(mixer.targetLineInfo[0])
    targetLine.open()
    val control = targetLine.getControl(FloatControl.Type.VOLUME) as FloatControl
    return (control.value * 100).toInt()
}

// Function to save audio folder paths
fun saveAudioFolderPaths(audioFolders: List<String>) {
    val prefs = Preferences.userRoot().node("com.example.musicplayer.settings")
    prefs.put("audioFolders", audioFolders.joinToString(separator = ";"))
}

// Function to save video folder paths
fun saveVideoFolderPaths(videoFolders: List<String>) {
    val prefs = Preferences.userRoot().node("com.example.musicplayer.settings")
    prefs.put("videoFolders", videoFolders.joinToString(separator = ";"))
}

// Function to load audio folder paths
fun loadAudioFolderPaths(): List<String> {
    val prefs = Preferences.userRoot().node("com.example.musicplayer.settings")
    val audioFoldersString = prefs.get("audioFolders", "")
    return if (audioFoldersString.isNotBlank()) {
        audioFoldersString.split(";")
    } else {
        emptyList()
    }
}

// Function to load video folder paths
fun loadVideoFolderPaths(): List<String> {
    val prefs = Preferences.userRoot().node("com.example.musicplayer.settings")
    val videoFoldersString = prefs.get("videoFolders", "")
    return if (videoFoldersString.isNotBlank()) {
        videoFoldersString.split(";")
    } else {
        emptyList()
    }
}

// Folder selection functions
@Composable
fun AudioFolderSelectionSection(audioFolders: MutableList<String>, onAudioFoldersSelected: () -> Unit) {
    FolderSelectionSection(
        icon = painterResource("drawable/music_folder.png"),
        title = "Audio Folders",
        folders = audioFolders,
        onFolderRemove = { index ->
            audioFolders.removeAt(index)
            onAudioFoldersSelected()
        },
        onFolderAdd = {
            val selectedFolder = selectFolder()
            if (!selectedFolder.isNullOrEmpty() && !audioFolders.contains(selectedFolder)) {
                audioFolders.add(selectedFolder)
                onAudioFoldersSelected()
            }
        }
    )
}

@Composable
fun VideoFolderSelectionSection(videoFolders: MutableList<String>, onVideoFoldersSelected: () -> Unit) {
    FolderSelectionSection(
        icon = painterResource("drawable/video_folder.png"),
        title = "Video Folders",
        folders = videoFolders,
        onFolderRemove = { index ->
            videoFolders.removeAt(index)
            onVideoFoldersSelected()
        },
        onFolderAdd = {
            val selectedFolder = selectFolder()
            if (!selectedFolder.isNullOrEmpty() && !videoFolders.contains(selectedFolder)) {
                videoFolders.add(selectedFolder)
                onVideoFoldersSelected()
            }
        }
    )
}

@Composable
fun FolderSelectionSection(
    icon: Painter,
    title: String,
    folders: MutableList<String>,
    onFolderRemove: (Int) -> Unit,
    onFolderAdd: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                painter = icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
            Text(
                text = title,
                style = MaterialTheme.typography.body2,
                color = Color.White,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        folders.forEachIndexed { index, folder ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Icon(
                    painter = painterResource("drawable/ic_folder.png"),
                    contentDescription = "Folder",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = folder,
                    style = MaterialTheme.typography.caption,
                    color = Color.White,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = { onFolderRemove(index) }) {
                    Icon(
                        painter = painterResource("drawable/ic_remove.png"),
                        contentDescription = "Remove",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        if (folders.size < 5) {
            Button(
                onClick = { onFolderAdd() },
                modifier = Modifier
                    .width(200.dp)
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
            ) {
                Icon(
                    painter = painterResource("drawable/ic_add.png"),
                    contentDescription = "Add Folder",
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Add Folder",
                    fontSize = 12.sp,
                    color = Color.White,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// Function to select a folder using JFileChooser
fun selectFolder(): String? {
    // Create a file chooser dialog
    val fileChooser = JFileChooser().apply {
        fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        isAcceptAllFileFilterUsed = false
    }

    // Set the title of the dialog
    fileChooser.dialogTitle = "Select Folder"

    // Show the file chooser dialog
    val result = fileChooser.showOpenDialog(null)
    return if (result == JFileChooser.APPROVE_OPTION) {
        // Get the selected file path
        fileChooser.selectedFile.absolutePath
    } else {
        null
    }
}
