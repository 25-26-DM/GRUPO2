package ec.edu.uce.taller7.data

import kotlinx.serialization.Serializable

@Serializable
data class MarsPhoto(
    val id: String,
    val img_src: String
)