package ponder.narrathon

import kotlinx.serialization.Serializable
import pondui.ui.nav.AppRoute
import pondui.ui.nav.IdRoute
import pondui.ui.nav.matchLongIdRoute
import pondui.ui.nav.matchStringIdRoute

@Serializable
object StartRoute : AppRoute("Start")

@Serializable
object HelloRoute : AppRoute("Hello")

@Serializable
object ExampleListRoute : AppRoute("Examples")

@Serializable
object DispatchRoute : AppRoute("Create")

@Serializable
object LibraryRoute : AppRoute("Library")

@Serializable
data class PlayerRoute(val label: String) : IdRoute<String>(TITLE, label) {
    companion object {
        const val TITLE = "Player"
        fun matchRoute(path: String) = matchStringIdRoute(path, TITLE) { PlayerRoute(it) }
    }
}

@Serializable
data class ExampleProfileRoute(val exampleId: Long) : AppRoute(TITLE) {
    companion object {
        const val TITLE = "Example"
        fun matchRoute(path: String) = matchLongIdRoute(path, TITLE) { ExampleProfileRoute(it) }
    }
}