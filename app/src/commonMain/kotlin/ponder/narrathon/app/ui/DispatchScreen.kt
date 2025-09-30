package ponder.narrathon.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import io.github.vinceglb.filekit.write
import kotlinx.coroutines.launch
import pondui.ui.controls.Button
import pondui.ui.controls.Column
import pondui.ui.controls.ProgressBar
import pondui.ui.controls.Row
import pondui.ui.controls.Scaffold
import pondui.ui.controls.Tab
import pondui.ui.controls.Tabs
import pondui.ui.controls.Text
import pondui.ui.controls.TextField

@Composable
fun DispatchScreen(
    viewModel: DispatchModel = viewModel { DispatchModel() }
) {
    val state by viewModel.stateFlow.collectAsState()

    val scope = rememberCoroutineScope()
    val launcher = rememberFileSaverLauncher { file ->
        // Write your data to the file
        val narration = state.narration ?: return@rememberFileSaverLauncher
        val bytes = mergeWavsToWav(
            narration.segments.map { it.bytes },
        )
        if (file != null) {
            scope.launch {
                file.write(bytes)
            }
        }
    }

    Scaffold {
        Tabs(selectedTab = state.tab, modifier = Modifier.weight(1f, false), onChangeTab = viewModel::setTab) {
            Tab("from text") {
                TextField(
                    text = state.label,
                    placeholder = "Label",
                    onChange = viewModel::setLabel,
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    text = state.content,
                    placeholder = "Content",
                    onChange = viewModel::setContent,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Tab("from url") {
                TextField(
                    text = state.url,
                    placeholder = "Url",
                    onChange = viewModel::setUrl,
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    text = state.label,
                    placeholder = "Label",
                    onChange = viewModel::setLabel,
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    text = state.content,
                    placeholder = "Content",
                    onChange = viewModel::setContent,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Tab("preview", isVisible = state.narration != null) {
                NarrationPlayer(state.narration, resetClips = state.resetClips)
            }
        }
        Row(1) {
            if (state.tab == "from url") {
                Button("Read", onClick = viewModel::readHtml)
            }
            Button("Generate", onClick = viewModel::generate)
            val savedLabel = if (state.isSaved) "Saved" else "Save"
            Button(savedLabel, onClick = {launcher.launch("narration", "wav")}, isEnabled = state.narration != null && !state.isSaved)
            val progress = state.progress; val count = state.count
            if (progress != null && count != null) {
                val ratio = progress / count.toFloat()
                ProgressBar(ratio) { Text("$progress of $count") }
            }
        }
    }
}

