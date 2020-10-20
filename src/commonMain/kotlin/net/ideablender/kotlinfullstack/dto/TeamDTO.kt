package net.ideablender.kotlinfullstack.dto

import kotlinx.serialization.Serializable

@Serializable
data class TeamDTO(
        override val id: Int,
        val name:String,
        val players:List<DTOMin>
) : DTO {
    companion object{
        fun getRestPath(): String = "/teams"
    }
}