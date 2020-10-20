package net.ideablender.kotlinfullstack.usecase.team

import net.ideablender.kotlinfullstack.dto.TeamDTO
import net.ideablender.kotlinfullstack.req.TeamReq


interface TeamCreate {
    suspend fun create(request: Request): Response
    data class Request(val req: TeamReq)
    sealed class Response {
        class Failure(val errMsg: String) : Response()
        class Success(val dto: TeamDTO) : Response()
    }
}