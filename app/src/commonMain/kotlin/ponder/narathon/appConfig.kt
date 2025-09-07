package ponder.narathon

import compose.icons.TablerIcons
import compose.icons.tablericons.Heart
import compose.icons.tablericons.Home
import compose.icons.tablericons.Rocket
import compose.icons.tablericons.YinYang
import kotlinx.collections.immutable.persistentListOf
import ponder.narathon.ui.ExampleListScreen
import ponder.narathon.ui.ExampleProfileScreen
import ponder.narathon.ui.HelloScreen
import ponder.narathon.ui.StartScreen
import ponder.narathon.app.ui.DispatchScreen
import pondui.ui.core.PondConfig
import pondui.ui.core.RouteConfig
import pondui.ui.nav.PortalDoor
import pondui.ui.nav.defaultScreen

val appConfig = PondConfig(
    name = "Narathon",
    logo = TablerIcons.Heart,
    home = StartRoute,
    routes = persistentListOf(
        RouteConfig(StartRoute::matchRoute) { defaultScreen<StartRoute> { StartScreen() } },
        RouteConfig(HelloRoute::matchRoute) { defaultScreen<HelloRoute> { HelloScreen() } },
        RouteConfig(ExampleListRoute::matchRoute) { defaultScreen<ExampleListRoute> { ExampleListScreen() } },
        RouteConfig(ExampleProfileRoute::matchRoute) { defaultScreen<ExampleProfileRoute> { ExampleProfileScreen(it) } },
        RouteConfig(DispatchRoute::matchRoute) { defaultScreen<DispatchRoute> { DispatchScreen() } }
    ),
    doors = persistentListOf(
        PortalDoor(TablerIcons.Home, StartRoute),
        PortalDoor(TablerIcons.YinYang, DispatchRoute),
        // PortalDoor(TablerIcons.Rocket, ExampleListRoute),
    ),
)