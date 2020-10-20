package net.ideablender.kotlinfullstack.usecase.player

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.ideablender.kotlinfullstack.exception.MissingMandatoryFieldException
import net.ideablender.kotlinfullstack.exception.PlayerNotFoundException
import net.ideablender.kotlinfullstack.exception.TeamNotFoundException
import net.ideablender.kotlinfullstack.helpers.LogHelper
import net.ideablender.kotlinfullstack.models.Player
import net.ideablender.kotlinfullstack.models.Team
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object PlayerUpdateImpl: PlayerUpdate {
    val logger: Logger = LoggerFactory.getLogger(PlayerUpdateImpl::class.simpleName)
    val modelClazz = "Player"

    override suspend fun update(request: PlayerUpdate.Request): PlayerUpdate.Response {
        val req = request.req
        val id  = req.id ?: throw MissingMandatoryFieldException("id")
        return try {
            withContext(Dispatchers.IO) {
                transaction {
                    val _team: Team = Team.findById(req.teamId!!) ?: throw TeamNotFoundException(req.teamId!!)
                    val model = Player.findById(id) ?: throw PlayerNotFoundException(id)
                    model.nameFirst = req.nameFirst ?: throw MissingMandatoryFieldException(fieldName = "nameFirst")
                    model.nameLast = req.nameLast ?: throw MissingMandatoryFieldException(fieldName = "nameLast")
                    model.team = _team
                    PlayerUpdate.Response.Success(model.toDTO())
                }
            }
        }catch (e:Exception){
            logger.error(LogHelper.uCCreateError(modelClazz, req, e))
            PlayerUpdate.Response.Failure(e.message ?: "No exception Message")
        }
    }
}