package net.ideablender.kotlinfullstack.usecase.team

import net.ideablender.kotlinfullstack.dto.TeamDTO
import net.ideablender.kotlinfullstack.req.TeamReq


interface TeamUpdate {
    suspend fun update(request: Request): Response
    data class Request(val req: TeamReq)
    sealed class Response {
        class Failure(val errMsg: String) : Response()
        class Success(val dto: TeamDTO) : Response()
    }
}