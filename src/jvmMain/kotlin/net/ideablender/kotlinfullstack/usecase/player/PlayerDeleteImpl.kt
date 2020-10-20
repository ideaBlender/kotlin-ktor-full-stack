package net.ideablender.kotlinfullstack.usecase.player

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.ideablender.kotlinfullstack.exception.PlayerNotFoundException
import net.ideablender.kotlinfullstack.helpers.LogHelper
import net.ideablender.kotlinfullstack.models.Player
import net.ideablender.kotlinfullstack.models.Players
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object PlayerDeleteImpl : PlayerDelete {
    val logger: Logger = LoggerFactory.getLogger(PlayerDeleteImpl::class.simpleName)
    val modelClazz = "Player"

    override suspend fun delete(request: PlayerDelete.Request): PlayerDelete.Response {
        val req = request.req
        return try {
            withContext(Dispatchers.IO) {
                transaction {
                    val model: Player = Player.findById(req.value) ?: throw PlayerNotFoundException(req.value)
                    Players.deleteWhere { Players.id eq req.value }
                    PlayerDelete.Response.Success()
                }
            }
        }catch (e:Exception){
            logger.error(LogHelper.uCDeleteError(modelClazz, req.value, e))
            PlayerDelete.Response.Failure(e)
        }
    }
}