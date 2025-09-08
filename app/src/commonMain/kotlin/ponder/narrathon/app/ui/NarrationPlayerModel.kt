package ponder.narrathon.app.ui

import androidx.compose.runtime.Stable
import ponder.narrathon.model.data.Narration
import pondui.WavePlayer
import pondui.WavePlaylist
import pondui.ui.core.ModelState
import pondui.ui.core.StateModel

@Stable
class NarrationPlayerModel(
): StateModel<NarrationPlayerState>() {
    override val state = ModelState(NarrationPlayerState())

    val playlist: WavePlaylist = WavePlaylist(this)

    fun load(narration: Narration?) {
        val narration = narration ?: return
        playlist.load(narration.segments) { it.bytes }
        setState { it.copy(narration = narration) }
    }

    fun togglePlay() {
        playlist.togglePlay()
    }
}

data class NarrationPlayerState(
    val narration: Narration? = null,
)