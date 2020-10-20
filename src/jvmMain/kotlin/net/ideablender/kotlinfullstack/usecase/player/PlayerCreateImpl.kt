package net.ideablender.kotlinfullstack.usecase.player

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.ideablender.kotlinfullstack.exception.MissingMandatoryFieldException
import net.ideablender.kotlinfullstack.exception.TeamNotFoundException
import net.ideablender.kotlinfullstack.helpers.LogHelper
import net.ideablender.kotlinfullstack.models.Player
import net.ideablender.kotlinfullstack.models.Team
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object PlayerCreateImpl: PlayerCreate {
    val logger: Logger = LoggerFactory.getLogger(PlayerCreateImpl::class.simpleName)
    val modelClazz = "Player"

    override suspend fun create(request: PlayerCreate.Request): PlayerCreate.Response {
        val req = request.req
        return try {
            withContext(Dispatchers.IO) {
                transaction {
                    val _team: Team = Team.findById(req.teamId!!) ?: throw TeamNotFoundException(req.teamId!!)
                    val model = transaction{
                        Player.new {
                            nameFirst = req.nameFirst ?: throw MissingMandatoryFieldException(fieldName = "nameFirst")
                            nameLast = req.nameLast ?: throw MissingMandatoryFieldException(fieldName = "nameLast")
                            team = _team
                        }
                    }
                    PlayerCreate.Response.Success(model.toDTO())
                }
            }
        }catch (e:Exception){
            logger.error(LogHelper.uCCreateError(modelClazz, req, e))
            PlayerCreate.Response.Failure(e.message ?: "No exception Message")
        }
    }
}