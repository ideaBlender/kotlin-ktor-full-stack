package net.ideablender.kotlinfullstack.store


import endpoint
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import jsonClient
import net.ideablender.kotlinfullstack.dto.DTO
import net.ideablender.kotlinfullstack.dto.PlayerDTO
import net.ideablender.kotlinfullstack.dto.TeamDTO
import net.ideablender.kotlinfullstack.req.PlayerReq
import net.ideablender.kotlinfullstack.req.REQ
import net.ideablender.kotlinfullstack.req.TeamReq

class Store {


}

data class FormData(val param:String, val theValue:Any)
interface RestInterface<T,U> {
    suspend fun fetchAll()
    suspend fun create(entity:U)
}


abstract class DataStore<T : DTO, U: REQ>(val restPath: String) : Observed(), RestInterface<T,U> {
    val items: MutableMap<Int, T> = mutableMapOf()

    fun findById(id: Int): T? = items[id]

    fun getAll(): List<T> = items.values.toList()

    fun doIt() {
        console.log(restPath)
    }

    fun commit(enitiy: T) {
        items[enitiy.id] = enitiy
        invokeCbs()
    }

    fun commitAll(enitiyList: List<T>) {
        items.putAll(enitiyList.map { it.id to it }.toMap())
        invokeCbs()
    }
}

object TeamService : DataStore<TeamDTO, TeamReq>(TeamDTO.getRestPath()) {
    override suspend fun fetchAll() {
        val resp = jsonClient.get<List<TeamDTO>>(endpoint + restPath)
        commitAll(resp)
    }

    override suspend fun create(entity: TeamReq) {
        val dto = jsonClient.post<TeamDTO>(endpoint + restPath) {
            contentType(ContentType.Application.Json)
            body = entity
        }
        commit(dto)
    }
}

object PlayerService : DataStore<PlayerDTO,PlayerReq>(PlayerDTO.getRestPath()) {
    override suspend fun fetchAll() {
        val resp = jsonClient.get<List<PlayerDTO>>(endpoint + restPath)
        commitAll(resp)
    }

    override suspend fun create(entity: PlayerReq) {
        val dto = jsonClient.post<PlayerDTO>(endpoint + restPath) {
            contentType(ContentType.Application.Json)
            body = entity
        }
        commit(dto)
        TeamService.fetchAll()
    }
}


abstract class Observed {
    val cbMap: MutableMap<String, () -> Unit> = mutableMapOf()

    fun registerCallback(key: String, cb: () -> Unit) {
        cbMap[key] = cb
    }

    fun unRegisterCallback(key: String) {
        cbMap.remove(key)
    }

    fun invokeCbs() {
        cbMap.forEach {
            it.value.invoke()
        }
    }
}
