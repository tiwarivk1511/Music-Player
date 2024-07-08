import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import components.Sidebar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import components.BottomControl
import screens.*

object Settings {
    val favoriteVideos: MutableList<String> = mutableListOf()
    val favoriteAudio: MutableList<String> = mutableListOf()
    var audioFolders = loadAudioFolderPaths()
    var videoFolders = loadVideoFolderPaths()
}

@Composable
@Preview
fun App() {

    var currentScreen by remember { mutableStateOf("home") }
    val verticalScrollState = rememberScrollState()

    MaterialTheme {
        Box(modifier = Modifier
            .background(Color.DarkGray)
        ) {
            Row {
                Column {
                    Sidebar(onNavigate = { screen -> currentScreen = screen })
                }
                Column(modifier = Modifier.verticalScroll(verticalScrollState)) {  }
                //Show the current Screen here
                when (currentScreen) {
                    "home" -> HomeScreen ({ currentScreen = "audio" },
                        Settings.audioFolders,
                        videoFolderPaths = Settings.videoFolders
                    )

                    "audio" -> {
                        AudioScreen(
                            { currentScreen = "videos" }, Settings.audioFolders
                        )
                    }

                    "videos" -> VideoScreen({ currentScreen = "favorites" }, Settings.videoFolders)

                    "favorites" -> FavoritesScreen { currentScreen = "home" }
                    "settings" -> SettingsScreen( { currentScreen = "home"},onAudioFoldersSelected = { folders ->
                        Settings.audioFolders = folders
                    },
                        onVideoFoldersSelected = { folders ->
                            Settings.videoFolders = folders
                        })
                }
            }


            VerticalScrollbar(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight(),
                adapter = rememberScrollbarAdapter(verticalScrollState)
            )

        }


    }
}




fun main() = application {
    Window(onCloseRequest = ::exitApplication,
        title = "Music Player",
        icon = painterResource("drawable/ic_album.png")
    ) {
        App()

        downloadVLC()
    }
}

fun downloadVLC() {
    //auto-download vlc media playe
    java.awt.Desktop.getDesktop().browse(java.net.URI("https://get.videolan.org/vlc/3.0.21/"))

}

