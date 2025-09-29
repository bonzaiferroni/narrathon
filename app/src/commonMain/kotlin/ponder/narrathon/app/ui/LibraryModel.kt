package ponder.narrathon.app.ui

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ponder.narrathon.AppDb
import ponder.narrathon.model.data.Narration
import pondui.ui.core.ModelState
import pondui.ui.core.StateModel
import pondui.utils.FileDao

class LibraryModel(
    private val narrationDb: FileDao<Narration> = AppDb.narration
): StateModel<LibraryState>() {
    override val state = ModelState(LibraryState())

    init {
        ioCollect(narrationDb.items) { narrations ->
            withContext(Dispatchers.Main) {
                setState { it.copy(narrations = narrations)}
            }
        }
    }
}

data class LibraryState(
    val narrations: List<Narration> = emptyList()
)