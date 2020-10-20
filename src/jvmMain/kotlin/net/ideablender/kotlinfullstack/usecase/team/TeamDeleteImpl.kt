package net.ideablender.kotlinfullstack.usecase.team

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.ideablender.kotlinfullstack.exception.ChildrenStillPresentException
import net.ideablender.kotlinfullstack.exception.TeamNotFoundException
import net.ideablender.kotlinfullstack.helpers.LogHelper
import net.ideablender.kotlinfullstack.models.Team
import net.ideablender.kotlinfullstack.models.Teams
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object TeamDeleteImpl :TeamDelete {
    val logger: Logger = LoggerFactory.getLogger(TeamDeleteImpl::class.simpleName)
    val modelClazz = "Team"

    override suspend fun delete(request: TeamDelete.Request): TeamDelete.Response {
        val req = request.req
        return try {
            withContext(Dispatchers.IO) {
                transaction {
                    val model:Team = Team.findById(req.value) ?: throw TeamNotFoundException(req.value)
                    if(model.players.count() > 0) throw ChildrenStillPresentException(model = modelClazz, childeModel = "Players", describers = model.players.map { it.toDTOMin().detail })
                    Teams.deleteWhere { Teams.id eq req.value }
                    TeamDelete.Response.Success()
                }
            }
        }catch (e:Exception){
            logger.error(LogHelper.uCDeleteError(modelClazz, req.value, e))
            TeamDelete.Response.Failure(e)
        }
    }
}