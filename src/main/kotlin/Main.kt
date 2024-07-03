import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import components.Sidebar
import androidx.compose.runtime.Composable
import screens.*

object Settings {
    var audioFolders = emptyList<String>()
    var videoFolders = emptyList<String>()
}

@Composable
@Preview
fun App() {

    var currentScreen by remember { mutableStateOf("home") }

    MaterialTheme {
        Row {
            Column {
                Sidebar(onNavigate = { screen -> currentScreen = screen })
            }

            //Show the current Screen here
            when (currentScreen) {
                "home" -> HomeScreen { currentScreen = "audio" }
                "audio" -> {
                    AudioScreen(
                        { currentScreen = "videos" } ,Settings.audioFolders
                    )
                }
                "videos" -> VideoScreen({ currentScreen = "favorites" }, Settings.videoFolders
                )
                "favorites" -> FavoritesScreen { currentScreen = "home" }
                "settings" -> SettingsScreen( { currentScreen = "home"},onAudioFoldersSelected = { folders ->
                Settings.audioFolders = folders
            },
            onVideoFoldersSelected = { folders ->
                Settings.videoFolders = folders
            })
            }
        }

    }
}




fun main() = application {
    Window(onCloseRequest = ::exitApplication,
        title = "Music Player",
        icon = painterResource("drawable/ic_album.png")
    ) {
        App()
    }
}

