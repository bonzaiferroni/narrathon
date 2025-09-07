package ponder.narathon.app.ui

import kabinet.clients.KokoroKmpClient
import pondui.WavePlayer
import pondui.ui.core.ModelState
import pondui.ui.core.StateModel

class DispatchModel(
    private val kokoro: KokoroKmpClient = KokoroKmpClient(),
    private val wavePlayer: WavePlayer = WavePlayer()
): StateModel<DispatchState>() {
    override val state = ModelState(DispatchState())

    private var bytes: ByteArray? = null

    fun setContent(value: String) {
        setState { it.copy(content = value)}
    }

    fun say() {
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
}

data class DispatchState(
    val content: String = "",
    val secondsMax: Int? = null,
    val progress: Int = 0,
)
