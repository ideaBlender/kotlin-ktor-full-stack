package net.ideablender.kotlinfullstack.dto

import kotlinx.serialization.Serializable

@Serializable
data class DTOMin(
        val id: Int,
        val detail: String
)