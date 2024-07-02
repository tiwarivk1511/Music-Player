package screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*
import javax.sound.sampled.*
import javax.swing.JFileChooser

@Composable
fun SettingsScreen(onNavigate: () -> Unit) {
    val audioFolders = remember { mutableStateListOf<String>() }
    val videoFolders = remember { mutableStateListOf<String>() }
    val verticalScrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .verticalScroll(verticalScrollState)
                .background(Color.DarkGray)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Settings",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.h5,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dropdown menu to set the audio quality
            DropdownSettingItem(SettingOption("Audio Quality", "Select audio quality", SettingType.DROPDOWN, painterResource("drawable/volume_up.png")))

            // Slider to adjust the volume level
            SliderSettingItem(SettingOption("Volume", "Adjust volume level", SettingType.SLIDER, painterResource("drawable/volume_up.png")))

            Spacer(modifier = Modifier.height(16.dp))

            AudioFolderSelectionSection(audioFolders)

            Spacer(modifier = Modifier.height(16.dp))

            VideoFolderSelectionSection(videoFolders)
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
fun DropdownSettingItem(option: SettingOption) {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("Low", "Medium", "High")
    var selectedItem by remember { mutableStateOf(items[0]) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = option.icon, contentDescription = option.title, modifier = Modifier.padding(end = 8.dp).size(24.dp), tint = Color.White)
            Column {
                Text(text = option.title, style = MaterialTheme.typography.body1, color = Color.White)
                Text(text = option.description, style = MaterialTheme.typography.body2, color = Color.White)
            }
        }

        Box(
            modifier = Modifier.clickable { expanded = !expanded },
            contentAlignment = Alignment.Center
        ) {
            Button(modifier = Modifier.width(200.dp), onClick = { expanded = true }) {
                Text(text = selectedItem)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(onClick = {
                        selectedItem = item
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
fun SliderSettingItem(option: SettingOption) {
    val volumeLevel = remember { mutableStateOf(getSystemVolume().toFloat()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = option.icon, contentDescription = option.title, modifier = Modifier.padding(end = 8.dp).size(24.dp), tint = Color.White)
            Column {
                Text(text = option.title, style = MaterialTheme.typography.body1, color = Color.White)
                Text(text = option.description, style = MaterialTheme.typography.body2, color = Color.White)
            }
        }

        Slider(
            value = volumeLevel.value,
            onValueChange = {
                volumeLevel.value = it
                setSystemVolume(it.toInt())
            },
            valueRange = 0f..100f,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTickColor = Color.White,
                inactiveTickColor = Color.LightGray,
            )
        )
    }
}

fun getSystemVolume(): Int {
    val mixers = AudioSystem.getMixerInfo()
    var systemVolume = 0

    for (mixerInfo in mixers) {
        val mixer = AudioSystem.getMixer(mixerInfo)
        if (mixer.isLineSupported(Line.Info(Port::class.java))) {
            val line = mixer.getLine(Line.Info(Port::class.java)) as Port
            line.open()

            try {
                val volumeControl = line.getControl(FloatControl.Type.VOLUME) as? FloatControl
                if (volumeControl != null) {
                    val volumeLevel = volumeControl.value // Current volume level (0.0 - 1.0)
                    systemVolume = (volumeLevel * 100).toInt() // Convert to percentage
                    break
                }
            } catch (e: IllegalArgumentException) {
                // Handle the case where the control is not found
                e.printStackTrace()
            } finally {
                line.close()
            }
        }
    }

    return systemVolume
}

fun setSystemVolume(volume: Int) {
    val mixers = AudioSystem.getMixerInfo()

    for (mixerInfo in mixers) {
        val mixer = AudioSystem.getMixer(mixerInfo)
        if (mixer.isLineSupported(Line.Info(Port::class.java))) {
            val line = mixer.getLine(Line.Info(Port::class.java)) as Port
            line.open()

            try {
                val volumeControl = line.getControl(FloatControl.Type.VOLUME) as? FloatControl
                if (volumeControl != null) {
                    volumeControl.value = volume.toFloat() // Set volume level (0.0 - 1.0)
                    break
                }
            } catch (e: IllegalArgumentException) {
                // Handle the case where the control is not found
                e.printStackTrace()
            } finally {
                line.close()
            }
        }
    }
}

@Composable
fun AudioFolderSelectionSection(audioFolders: MutableList<String>) {
    FolderSelectionSection(
        icon = painterResource("drawable/music_folder.png"),
        title = "Audio Folders",
        folders = audioFolders
    )
}

@Composable
fun VideoFolderSelectionSection(videoFolders: MutableList<String>) {
    FolderSelectionSection(
        icon = painterResource("drawable/video_folder.png"),
        title = "Video Folders",
        folders = videoFolders
    )
}

@Composable
fun FolderSelectionSection(icon: Painter, title: String, folders: MutableList<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                painter = icon,
                contentDescription = title,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(16.dp),
                tint = Color.White
            )
            Text(
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        folders.forEachIndexed { index, folder ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
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
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = { folders.removeAt(index) }) {
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
                onClick = {
                    val selectedFolder = selectFolder()
                    if (!selectedFolder.isNullOrEmpty()) {
                        folders.add(selectedFolder)
                    }
                },
                modifier = Modifier.width(200.dp).padding(vertical = 8.dp),
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
