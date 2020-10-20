package net.ideablender.kotlinfullstack.usecase.player

import net.ideablender.kotlinfullstack.dto.PlayerDTO
import net.ideablender.kotlinfullstack.req.PlayerReq


interface PlayerCreate {
    suspend fun create(request: Request): Response
    data class Request(val req: PlayerReq)
    sealed class Response {
        class Failure(val errMsg: String) : Response()
        class Success(val dto: PlayerDTO) : Response()
    }
}