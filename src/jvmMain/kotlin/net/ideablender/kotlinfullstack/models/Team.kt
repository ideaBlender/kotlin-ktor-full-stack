package net.ideablender.kotlinfullstack.models

import net.ideablender.kotlinfullstack.dto.DTOMin
import net.ideablender.kotlinfullstack.dto.TeamDTO
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Teams : IntIdTable() {
    val name = varchar("name", 20)
}

class Team(id: EntityID<Int>) : IntEntity(id), DatabaseModel {
    companion object : IntEntityClass<Team>(Teams)
    var name by Teams.name
    val players  by Player referrersOn Players.team // @OneToMany

    override fun toDTOMin(): DTOMin = DTOMin(id = this.id.value, detail = name)

    override fun toDTO(): TeamDTO  = TeamDTO(
            id = this.id.value,
            name = name,
            players = players.map { it.toDTOMin() }
    )
}