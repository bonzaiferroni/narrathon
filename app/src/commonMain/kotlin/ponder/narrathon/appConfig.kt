package ponder.narrathon

import compose.icons.TablerIcons
import compose.icons.tablericons.Book
import compose.icons.tablericons.Heart
import compose.icons.tablericons.Home
import compose.icons.tablericons.Rocket
import compose.icons.tablericons.YinYang
import kotlinx.collections.immutable.persistentListOf
import ponder.narrathon.ui.ExampleListScreen
import ponder.narrathon.ui.ExampleProfileScreen
import ponder.narrathon.ui.HelloScreen
import ponder.narrathon.ui.StartScreen
import ponder.narrathon.app.ui.DispatchScreen
import ponder.narrathon.app.ui.LibraryScreen
import pondui.ui.core.PondConfig
import pondui.ui.core.RouteConfig
import pondui.ui.nav.PortalDoor
import pondui.ui.nav.defaultScreen

val appConfig = PondConfig(
    name = "Narrathon",
    logo = TablerIcons.Heart,
    home = StartRoute,
    routes = persistentListOf(
        RouteConfig(StartRoute::matchRoute) { defaultScreen<StartRoute> { StartScreen() } },
        RouteConfig(HelloRoute::matchRoute) { defaultScreen<HelloRoute> { HelloScreen() } },
        RouteConfig(ExampleListRoute::matchRoute) { defaultScreen<ExampleListRoute> { ExampleListScreen() } },
        RouteConfig(ExampleProfileRoute::matchRoute) { defaultScreen<ExampleProfileRoute> { ExampleProfileScreen(it) } },
        RouteConfig(DispatchRoute::matchRoute) { defaultScreen<DispatchRoute> { DispatchScreen() } },
        RouteConfig(LibraryRoute::matchRoute) { defaultScreen<LibraryRoute> { LibraryScreen() } }
    ),
    doors = persistentListOf(
        // PortalDoor(TablerIcons.Home, StartRoute),
        PortalDoor(TablerIcons.YinYang, DispatchRoute),
        PortalDoor(TablerIcons.Book, LibraryRoute),
    ),
)