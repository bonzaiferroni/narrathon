package ponder.narrathon.app.ui

import kabinet.clients.KokoroKmpClient
import kotlinx.datetime.Clock
import ponder.narrathon.AppDb
import ponder.narrathon.model.data.Narration
import ponder.narrathon.model.data.NarrationSegment
import pondui.WavePlayer
import pondui.ui.core.ModelState
import pondui.ui.core.StateModel
import pondui.utils.FileDb

class DispatchModel(
    private val kokoro: KokoroKmpClient = KokoroKmpClient(),
    private val wavePlayer: WavePlayer = WavePlayer(),
    private val narrationDb: FileDb<Narration> = AppDb.narration
) : StateModel<DispatchState>() {
    override val state = ModelState(DispatchState())

    fun setContent(value: String) {
        setState { it.copy(content = value) }
    }

    fun setLabel(value: String) {
        setState { it.copy(label = value) }
    }

    fun generate() {
        ioLaunch {
            val paragraphs = stateNow.content.split('\n').filter { it.isNotEmpty() }
            if (paragraphs.isEmpty()) return@ioLaunch
            setStateWithMain { it.copy(progress = 0, count = paragraphs.size, isSaved = false) }
            val segments = paragraphs.mapIndexed { index, text ->
                val bytes = kokoro.getMessage(text)
                val seconds = wavePlayer.readInfo(bytes) ?: error("seconds not found")
                setStateWithMain { it.copy(progress = index + 1) }
                NarrationSegment(text, bytes, seconds)
            }
            val narration = Narration("", segments, Clock.System.now())
            setStateWithMain { it.copy(narration = narration) }
        }
    }

    fun save() {
        val narration = stateNow.narration ?: return
        val label = stateNow.label.takeIf { it.isNotEmpty() } ?: narration.segments.first().text.take(50)
        ioLaunch {
            narrationDb.create(narration.copy(label = label))
            setStateWithMain { it.copy(isSaved = true) }
        }
    }
}

data class DispatchState(
    val label: String = "",
    val content: String = "",
    var narration: Narration? = null,
    val progress: Int? = null,
    val count: Int? = null,
    val isSaved: Boolean = false,
)
