package ponder.narrathon.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.PlayerPause
import compose.icons.tablericons.PlayerPlay
import ponder.narrathon.model.data.Narration
import pondui.ui.controls.Button
import pondui.ui.controls.Row
import pondui.ui.controls.Text

@Composable
fun NarrationPlayer(
    narration: Narration?,
    modifier: Modifier = Modifier,
    viewModel: NarrationPlayerModel = viewModel { NarrationPlayerModel() }
) {
    val state by viewModel.stateFlow.collectAsState()
    viewModel.load(narration)
    val playlistState by viewModel.playlist.stateFlow.collectAsState()

    Row(1, modifier = modifier) {
        val icon = if (playlistState.isPlaying) TablerIcons.PlayerPause else TablerIcons.PlayerPlay
        Button(icon, onClick = viewModel::togglePlay, isEnabled = state.narration != null)
        playlistState.progress?.let {
            Text("${formatTime(it)} / ${formatTime(playlistState.lengthMillis)}")
        }
    }
}

fun formatTime(milliseconds: Int): String {
    val minutes = milliseconds / 1000 / 60
    val secs = milliseconds / 1000 % 60
    return "$minutes:%02d".format(secs)
}