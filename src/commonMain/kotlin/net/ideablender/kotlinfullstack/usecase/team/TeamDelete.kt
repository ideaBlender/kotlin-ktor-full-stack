package net.ideablender.kotlinfullstack.usecase.team

import net.ideablender.kotlinfullstack.req.IntReq


interface TeamDelete {
    suspend fun delete(request: Request): Response
    data class Request(val req: IntReq)
    sealed class Response {
        class Failure(val exception: Exception) : Response()
        class Success : Response()
    }
}