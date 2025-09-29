package ponder.narrathon.model.data

import kotlinx.serialization.Serializable

@Serializable
data class Example(
    val id: String,
    val userId: Long,
    val label: String,
)