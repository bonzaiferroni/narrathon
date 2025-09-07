package ponder.narrathon.model.data

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Narration(
    val label: String,
    val textContents: List<String>,
    val waveBytes: List<ByteArray>,
    val createdAt: Instant,
) {
    fun toFileName() = label.replace(" ", "_").replace(Regex("[^A-Za-z0-9_]"), "")
}