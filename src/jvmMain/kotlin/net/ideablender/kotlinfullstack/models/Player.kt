package net.ideablender.kotlinfullstack.models

import net.ideablender.kotlinfullstack.dto.DTOMin
import net.ideablender.kotlinfullstack.dto.PlayerDTO
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Players : IntIdTable() {
    val nameFirst = varchar("name_first", 20)
    val nameLast = varchar("name_last", 20)
    val team = reference("team_id", Teams.id)
}

class Player(id: EntityID<Int>) : IntEntity(id), DatabaseModel {
    companion object : IntEntityClass<Player>(Players)
    var nameFirst by Players.nameFirst
    var nameLast by Players.nameLast
    var team by Team referencedOn Players.team

    override fun toDTOMin(): DTOMin = DTOMin(id = this.id.value, detail = "$nameFirst $nameLast")
    override fun toDTO(): PlayerDTO = PlayerDTO(
            id = this.id.value,
            nameFirst = nameFirst,
            nameLast = nameLast,
            team = team.toDTOMin()
    )
}