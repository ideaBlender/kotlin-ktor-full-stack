package net.ideablender.kotlinfullstack.usecase.team

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.ideablender.kotlinfullstack.exception.MissingMandatoryFieldException
import net.ideablender.kotlinfullstack.helpers.LogHelper
import net.ideablender.kotlinfullstack.models.Team
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object TeamCreateImpl:TeamCreate {
    val logger: Logger = LoggerFactory.getLogger(TeamCreateImpl::class.simpleName)
    val modelClazz = "Team"

    override suspend fun create(request: TeamCreate.Request): TeamCreate.Response {
        val req = request.req
        return try {
            withContext(Dispatchers.IO) {
                transaction {
                    val model = transaction{
                        Team.new {
                            name = req.name ?: throw MissingMandatoryFieldException(fieldName = "name")
                        }
                    }
                    TeamCreate.Response.Success(model.toDTO())
                }
            }
        }catch (e:Exception){
            logger.error(LogHelper.uCCreateError(modelClazz, req, e))
            TeamCreate.Response.Failure(e.message ?: "No exception Message")
        }
    }
}