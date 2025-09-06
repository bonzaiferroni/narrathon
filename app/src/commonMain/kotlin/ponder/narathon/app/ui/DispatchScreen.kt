package ponder.narathon.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import pondui.ui.controls.Scaffold
import pondui.ui.controls.Text
import pondui.ui.controls.TextField

@Composable
fun DispatchScreen(
    viewModel: DispatchModel = viewModel { DispatchModel() }
) {
    val state by viewModel.stateFlow.collectAsState()
    Scaffold {
        TextField(state.content, onTextChanged = viewModel::setContent)
    }
}
