package net.ideablender.kotlinfullstack.repo

import net.ideablender.kotlinfullstack.db.DbFactory.dbQuery
import net.ideablender.kotlinfullstack.dto.PlayerDTO
import net.ideablender.kotlinfullstack.models.Player

object PlayerRepo {
    suspend fun getAll(): List<PlayerDTO> = dbQuery {
       Player.all().map { it.toDTO() }.toList()
    }

    suspend fun getAllById(idList:List<Int>): List<PlayerDTO> = dbQuery {
       Player.forIds(idList).map { it.toDTO() }.toList()
    }

    suspend fun getById(id:Int): PlayerDTO? = dbQuery {
       Player.findById(id)?.toDTO()
    }

    suspend fun getAllModel(): List<Player> = dbQuery {
       Player.all().toList()
    }

    suspend fun getModelById(id:Int): Player? = dbQuery {
       Player.findById(id)
    }
}