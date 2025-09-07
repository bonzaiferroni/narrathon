package ponder.narrathon.server

import io.ktor.server.application.*
import klutch.server.configureSecurity
import ponder.narrathon.server.plugins.configureApiRoutes
import ponder.narrathon.server.plugins.configureCors
import ponder.narrathon.server.plugins.configureDatabases
import ponder.narrathon.server.plugins.configureLogging
import ponder.narrathon.server.plugins.configureSerialization
import ponder.narrathon.server.plugins.configureWebSockets

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureCors()
    configureSerialization()
    configureDatabases()
    configureSecurity()
    configureApiRoutes()
    configureWebSockets()
    configureLogging()
}