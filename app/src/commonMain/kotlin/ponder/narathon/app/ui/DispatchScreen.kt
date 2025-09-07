package ponder.narathon.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import pondui.ui.controls.Button
import pondui.ui.controls.Column
import pondui.ui.controls.Row
import pondui.ui.controls.Scaffold
import pondui.ui.controls.Text
import pondui.ui.controls.TextField

@Composable
fun DispatchScreen(
    viewModel: DispatchModel = viewModel { DispatchModel() }
) {
    val state by viewModel.stateFlow.collectAsState()
    Scaffold {
        Column(1) {
            TextField(state.content, onTextChanged = viewModel::setContent, modifier = Modifier.weight(1f))
            Row(1) {
                Button("Generate", onClick = viewModel::generate)
                Button("Say", onClick = viewModel::say, isEnabled = state.secondsMax != null)
                state.secondsMax?.let {
                    val progress = state.progress
                    Text("${formatTime(progress)} / ${formatTime(it)}")
                }
            }
        }
    }
}

fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return "$minutes:%02d".format(secs)
}