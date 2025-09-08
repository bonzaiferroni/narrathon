package ponder.narrathon.model.data

import androidx.compose.runtime.Stable
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
@Stable
data class Narration(
    val label: String,
    val segments: List<NarrationSegment>,
    val createdAt: Instant,
) {
    val seconds get() = segments.sumOf { it.seconds }
    fun toFileName() = label.replace(" ", "_").replace(Regex("[^A-Za-z0-9_]"), "")
}

@Suppress("ArrayInDataClass")
@Serializable
data class NarrationSegment(
    val text: String,
    val bytes: ByteArray,
    val seconds: Int,
)