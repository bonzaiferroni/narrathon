package ponder.narrathon.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import ponder.narrathon.PlayerRoute
import pondui.ui.controls.Scaffold
import pondui.ui.controls.Text

@Composable
fun PlayerScreen(
    route: PlayerRoute,
    viewModel: PlayerModel = viewModel { PlayerModel(route.label) }
) {
    val state by viewModel.stateFlow.collectAsState()
    Scaffold {
        NarrationPlayer(state.narration)
    }
}
