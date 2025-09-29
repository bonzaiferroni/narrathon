package ponder.narrathon.app.ui

import ponder.narrathon.AppDb
import ponder.narrathon.model.data.Narration
import pondui.ui.core.ModelState
import pondui.ui.core.StateModel
import pondui.utils.FileDao

class PlayerModel(
    label: String,
    private val narrationDb: FileDao<Narration> = AppDb.narration
) : StateModel<PlayerState>() {
    override val state = ModelState(PlayerState())

    init {
        ioCollect(narrationDb.flowSingle { it.label == label }) { narration ->
            setStateFromMain { it.copy(narration = narration) }
        }
    }
}

data class PlayerState(
    val narration: Narration? = null
)
