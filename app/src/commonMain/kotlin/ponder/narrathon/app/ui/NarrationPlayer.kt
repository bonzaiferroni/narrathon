package ponder.narrathon.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.lifecycle.viewmodel.compose.viewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.PlayerPause
import compose.icons.tablericons.PlayerPlay
import ponder.narrathon.model.data.Narration
import pondui.ui.controls.Button
import pondui.ui.controls.H3
import pondui.ui.controls.LazyColumn
import pondui.ui.controls.Row
import pondui.ui.controls.Text
import pondui.ui.controls.actionable
import pondui.ui.modifiers.selected
import pondui.ui.theme.Pond

@Composable
fun NarrationPlayer(
    narration: Narration?,
    modifier: Modifier = Modifier,
    resetClips: Boolean = true,
    viewModel: NarrationPlayerModel = viewModel { NarrationPlayerModel() }
) {
    val state by viewModel.stateFlow.collectAsState()
    viewModel.load(narration, resetClips)
    val playlistState by viewModel.playlist.stateFlow.collectAsState()

    val narration = state.narration ?: return

    Box(modifier = modifier) {
        LazyColumn(1) {
            item("header") {
                H3(narration.label.takeIf { it.isNotEmpty() } ?: "[No Label]")
            }
            itemsIndexed(narration.segments, key = { _, item -> item.text }) { index, narration ->
                Text(
                    text = narration.text,
                    modifier = Modifier.animateItem()
                        .selected(index == playlistState.index)
                        .actionable(isIndicated = false, icon = PointerIcon.Hand) { viewModel.playlist.playFrom(index) }
                )
            }
        }
        Row(
            gap = 1,
            modifier = Modifier.align(Alignment.BottomCenter)
                .clip(Pond.ruler.pill)
                .background(Pond.colors.void)
                .padding(Pond.ruler.unitPadding)
        ) {
            val icon = if (playlistState.isPlaying) TablerIcons.PlayerPause else TablerIcons.PlayerPlay
            Button(icon, onClick = viewModel::togglePlay)
            playlistState.progress?.let {
                Text("${formatTime(it)} / ${formatTime(playlistState.lengthMillis)}")
            }
        }
    }
}

fun formatTime(milliseconds: Int): String {
    val minutes = milliseconds / 1000 / 60
    val secs = milliseconds / 1000 % 60
    return "$minutes:%02d".format(secs)
}