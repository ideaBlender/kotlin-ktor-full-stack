package net.ideablender.kotlinfullstack.models

import net.ideablender.kotlinfullstack.dto.DTO
import net.ideablender.kotlinfullstack.dto.DTOMin

interface DatabaseModel {
    fun toDTOMin(): DTOMin
    fun toDTO(): DTO
}