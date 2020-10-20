package net.ideablender.kotlinfullstack.repo

import net.ideablender.kotlinfullstack.db.DbFactory.dbQuery
import net.ideablender.kotlinfullstack.dto.TeamDTO
import net.ideablender.kotlinfullstack.models.Team
import net.ideablender.kotlinfullstack.models.Teams

object TeamRepo {
    suspend fun getAll(): List<TeamDTO> = dbQuery {
        Team.all().map { it.toDTO() }.toList()
    }

    suspend fun getAllById(idList:List<Int>): List<TeamDTO> = dbQuery {
        Team.forIds(idList).map { it.toDTO() }.toList()
    }

    suspend fun getById(id:Int): TeamDTO? = dbQuery {
        Team.findById(id)?.toDTO()
    }

    suspend fun getAllModel(): List<Team> = dbQuery {
        Team.all().toList()
    }

    suspend fun getModelById(id:Int): Team? = dbQuery {
        Team.findById(id)
    }

    suspend fun getByName(name: String): TeamDTO = dbQuery {
        Team.find { Teams.name.eq(name) }.first().toDTO()
    }
}