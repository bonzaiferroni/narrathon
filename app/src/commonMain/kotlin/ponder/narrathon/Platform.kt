package ponder.narrathon

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform