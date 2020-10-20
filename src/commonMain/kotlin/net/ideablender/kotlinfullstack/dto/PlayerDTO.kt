package net.ideablender.kotlinfullstack.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlayerDTO(
        override val id: Int,
        val nameFirst:String,
        val nameLast:String,
        val team:DTOMin) : DTO {
    companion object{
        fun getRestPath(): String = "/players"
    }
}