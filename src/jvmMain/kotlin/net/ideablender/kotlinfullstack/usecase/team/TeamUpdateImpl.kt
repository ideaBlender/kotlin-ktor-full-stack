package net.ideablender.kotlinfullstack.usecase.team

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.ideablender.kotlinfullstack.exception.MissingMandatoryFieldException
import net.ideablender.kotlinfullstack.exception.TeamNotFoundException
import net.ideablender.kotlinfullstack.helpers.LogHelper
import net.ideablender.kotlinfullstack.models.Team
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object TeamUpdateImpl:TeamUpdate {
    val logger: Logger = LoggerFactory.getLogger(TeamUpdateImpl::class.simpleName)
    val modelClazz = "Team"

    override suspend fun update(request: TeamUpdate.Request): TeamUpdate.Response {
        val req = request.req
        val id  = req.id ?: throw MissingMandatoryFieldException("id")
        return try {
            withContext(Dispatchers.IO) {
                transaction {
                    val model = Team.findById(id) ?: throw TeamNotFoundException(id)
                    if(req.name != null){
                        model.name = req.name!!
                    }
                    TeamUpdate.Response.Success(model.toDTO())
                }
            }
        }catch (e:Exception){
            logger.error(LogHelper.uCCreateError(modelClazz, req, e))
            TeamUpdate.Response.Failure(e.message ?: "No exception Message")
        }
    }
}