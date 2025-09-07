package ponder.narrathon.app.ui

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import pondui.ui.controls.Column
import pondui.ui.controls.LazyColumn
import pondui.ui.controls.Scaffold
import pondui.ui.controls.Text

@Composable
fun LibraryScreen(
    viewModel: LibraryModel = viewModel { LibraryModel() }
) {
    val state by viewModel.stateFlow.collectAsState()
    Scaffold {
        LazyColumn(1) {
            items(state.narrations) { narration ->
                Text(narration.label)
            }
        }
    }
}
