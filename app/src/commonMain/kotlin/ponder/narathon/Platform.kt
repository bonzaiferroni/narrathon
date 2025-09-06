package ponder.narathon

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform