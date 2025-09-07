package ponder.narrathon.app.ui

import kabinet.clients.KokoroKmpClient
import kotlinx.datetime.Clock
import ponder.narrathon.AppDb
import ponder.narrathon.model.data.Narration
import pondui.WavePlayer
import pondui.ui.core.ModelState
import pondui.ui.core.StateModel
import pondui.utils.FileDb

class DispatchModel(
    private val kokoro: KokoroKmpClient = KokoroKmpClient(),
    private val wavePlayer: WavePlayer = WavePlayer(),
    private val narrationDb: FileDb<Narration> = AppDb.narration
): StateModel<DispatchState>() {
    override val state = ModelState(DispatchState())

    private var bytes: ByteArray? = null

    fun setContent(value: String) {
        setState { it.copy(content = value)}
    }

    fun speak() {
        val bytes = bytes ?: return
        ioLaunch {
            wavePlayer.play(bytes) { progress ->
                setState { it.copy(progress = progress) }
            }
        }
    }

    fun generate() {
        ioLaunch {
            bytes = kokoro.getMessage(stateNow.content)
            val seconds = bytes?.let { wavePlayer.readInfo(it) }
            setState { it.copy(secondsMax = seconds, progress = 0) }
        }
    }

    fun save() {
        val bytes = bytes ?: return
        ioLaunch {
            narrationDb.create(Narration(
                label = stateNow.content.take(20),
                textContents = listOf(stateNow.content),
                waveBytes = listOf(bytes),
                createdAt = Clock.System.now()
            ))
        }
    }
}

data class DispatchState(
    val content: String = "",
    val secondsMax: Int? = null,
    val progress: Int = 0,
)
