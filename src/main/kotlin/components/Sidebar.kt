package components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.sub_components.SearchCard
import components.sub_components.SidebarItem

@Composable
fun Sidebar(onNavigate: (String) -> Unit) {
    var inputValue by remember { mutableStateOf("") }

    val verticalScrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier
            .width(250.dp)
            .fillMaxHeight(),
        backgroundColor = Color(0xFF121212), // Modern dark background color
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            contentAlignment = Alignment.TopEnd
        ){
            VerticalScrollbar(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(8.dp)
                    .align(Alignment.CenterEnd),
                adapter = rememberScrollbarAdapter(verticalScrollState)
            )

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(verticalScrollState),
            ) {

                SearchCard(inputValue) { inputValue = it }
                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Browse",
                    style = MaterialTheme.typography.h6.copy(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(20.dp))

                // Example items in the sidebar
                SidebarItem(painterResource("drawable/ic_home.png"), text = "Home", onClick = { onNavigate("home") })
                SidebarItem(painterResource("drawable/music_icon.png"), text = "Audio", onClick = { onNavigate("genre") })
                SidebarItem(painterResource("drawable/ic_video_play.png"), text = "Video", onClick = { onNavigate("top_charted") })
                SidebarItem(painterResource("drawable/microphone.png"), text = "Favorites", onClick = { onNavigate("podcasts") })
            }
        }
    }
}
