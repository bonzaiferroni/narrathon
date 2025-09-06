package ponder.narathon.server

import io.ktor.server.application.*
import klutch.server.configureSecurity
import ponder.narathon.server.plugins.configureApiRoutes
import ponder.narathon.server.plugins.configureCors
import ponder.narathon.server.plugins.configureDatabases
import ponder.narathon.server.plugins.configureLogging
import ponder.narathon.server.plugins.configureSerialization
import ponder.narathon.server.plugins.configureWebSockets

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