package ponder.narrathon.app.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import pondui.ui.controls.Button
import pondui.ui.controls.Column
import pondui.ui.controls.ProgressBar
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
            TextField(
                text = state.label,
                placeholder = "Label",
                onTextChanged = viewModel::setLabel,
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                text = state.content,
                placeholder = "Content",
                onTextChanged = viewModel::setContent,
                modifier = Modifier.fillMaxWidth().weight(1f, false)
            )
            Row(1) {
                Button("Generate", onClick = viewModel::generate)
                val savedLabel = if (state.isSaved) "Saved" else "Save"
                Button(savedLabel, onClick = viewModel::save, isEnabled = state.narration != null && !state.isSaved)
                val progress = state.progress; val count = state.count
                if (progress != null && count != null) {
                    val ratio = progress / count.toFloat()
                    ProgressBar(ratio) { Text("$progress of $count") }
                }
            }
            NarrationPlayer(state.narration)
        }
    }
}