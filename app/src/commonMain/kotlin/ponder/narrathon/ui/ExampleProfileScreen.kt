package ponder.narrathon.ui

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import ponder.narrathon.ExampleProfileRoute
import pondui.ui.controls.Button
import pondui.ui.controls.Row
import pondui.ui.controls.Scaffold
import pondui.ui.controls.Text
import pondui.ui.controls.TextField
import pondui.ui.theme.Pond

@Composable
fun ExampleProfileScreen(
    route: ExampleProfileRoute,
    viewModel: ExampleProfileModel = viewModel { ExampleProfileModel(route) }
) {
    val state by viewModel.stateFlow.collectAsState()
    val example = state.example
    if (example == null) return
    Scaffold {
        Row(1) {
            if (state.isEditing) {
                TextField(state.symtrix, onChange = viewModel::setSymtrix)
                Button("Done", onClick = viewModel::finalizeEdit)
            } else {
                Text(example.label)
                Button("Edit", onClick = viewModel::toggleEdit, color = Pond.colors.primary)
            }
        }
    }
}