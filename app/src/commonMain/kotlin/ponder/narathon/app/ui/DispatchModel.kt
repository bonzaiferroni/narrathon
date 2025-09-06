package ponder.narathon.app.ui

import pondui.ui.core.ModelState
import pondui.ui.core.StateModel

class DispatchModel(): StateModel<DispatchState>() {
    override val state = ModelState(DispatchState())

    fun setContent(value: String) {
        setState { it.copy(content = value)}
    }
}

data class DispatchState(
    val content: String = ""
)
