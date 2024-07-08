import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.sub_components.BannerHome
import components.sub_components.MarqueeText
import components.sub_components.SongListViewArea
import components.sub_components.VideoListViewArea

@Composable
fun HomeScreen(
    function: () -> Unit,
    folderPaths: List<String>,
    videoFolderPaths: List<String>,

) {
    val verticalScrollState = rememberScrollState()


    Box(
        modifier = Modifier.fillMaxSize().background(Color.DarkGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
                .verticalScroll(verticalScrollState)
        ) {

            MarqueeText("Hello, Let's Enjoy the Rock!!, To Use the Video Play you need to install the VLC Media Player")

            // Show the banner
            BannerHome(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                title = "What's New?",
                message = "Hello, Let's Enjoy the Rock!!"
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.DarkGray),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "Videos", fontSize = 20.sp, color = Color.White)
                Text(text = "Audio", fontSize = 20.sp, color = Color.White)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.DarkGray)
                    .padding(4.dp)
            ) {
                // Show the list view
                VideoListViewArea(
                    videoFolderPaths = videoFolderPaths,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color.DarkGray)
                        .padding(4.dp),
                )

                SongListViewArea(
                    audioFolderPaths = folderPaths,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color.DarkGray)
                        .padding(4.dp),
                    mediaPlayerController = MediaPlayerController,

                )
            }
        }

        // Vertical scrollbar
        VerticalScrollbar(
            modifier = Modifier
                .fillMaxHeight()
                .width(8.dp)
                .align(Alignment.CenterEnd),
            adapter = rememberScrollbarAdapter(verticalScrollState)
        )
    }
}
