package ponder.narrathon

import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import ponder.narrathon.model.data.Narration

import pondui.io.ProvideUserContext
import pondui.ui.core.PondApp
import pondui.ui.nav.NavRoute
import pondui.ui.theme.ProvideTheme
import pondui.utils.FileDao

@Composable
@Preview
fun App(
    changeRoute: (NavRoute) -> Unit,
    exitApp: (() -> Unit)?,
) {
    ProvideTheme {
//        ProvideUserContext {
//
//        }

        PondApp(
            config = appConfig,
            changeRoute = changeRoute,
            exitApp = exitApp
        )
    }
}

object AppDb {
    val narration = FileDao(Narration::class) { it.toFileName() }
}