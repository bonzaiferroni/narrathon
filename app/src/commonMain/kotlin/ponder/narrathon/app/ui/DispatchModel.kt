package ponder.narrathon.app.ui

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readString
import kabinet.clients.HtmlClient
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
    private val narrationDb: FileDb<Narration> = AppDb.narration,
    private val htmlClient: HtmlClient = HtmlClient()
) : StateModel<DispatchState>() {
    override val state = ModelState(DispatchState())

    fun setContent(value: String) {
        setState { it.copy(content = value) }
    }

    fun setLabel(value: String) {
        val narration = stateNow.narration?.copy(label = value)
        setState { it.copy(label = value, narration = narration) }
    }

    fun setUrl(value: String) {
        setState { it.copy(url = value) }
    }

    fun setTab(value: String) {
        setState { it.copy(tab = value)}
    }

    fun generate() {
        ioLaunch {
            val paragraphs = stateNow.content.split('\n').filter { it.isNotEmpty() }
            if (paragraphs.isEmpty()) return@ioLaunch
            setStateWithMain { it.copy(progress = 0, count = paragraphs.size, isSaved = false) }
            val segments = mutableListOf<NarrationSegment>()
            paragraphs.forEachIndexed { index, text ->
                val bytes = kokoro.getMessage(text)
                val seconds = wavePlayer.readInfo(bytes) ?: error("seconds not found")
                val segment = NarrationSegment(text, bytes, seconds)
                segments.add(segment)
                val narration = Narration("", segments, Clock.System.now())
                setStateWithMain { it.copy(progress = index + 1, narration = narration, resetClips = index == 0, tab = "preview") }
            }
        }
    }

    fun readHtml() {
        ioLaunch {
            val document = htmlClient.readUrl(stateNow.url) ?: return@ioLaunch
            setStateWithMain {
                it.copy(
                    label = document.title ?: "",
                    content = document.contents.joinToString("\n\n") { content -> content.text }
                )
            }
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
    val resetClips: Boolean = true,
    val tab: String? = null,
    val url: String = ""
)
