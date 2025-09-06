package ponder.narathon.ui

import androidx.compose.runtime.Composable
import ponder.narathon.StartRoute
import pondui.ui.controls.RouteButton
import pondui.ui.controls.Scaffold
import pondui.ui.controls.Text

@Composable
fun HelloScreen() {
    Scaffold {
        Text("Hello world!")
        RouteButton("Go to start") { StartRoute }
    }
}