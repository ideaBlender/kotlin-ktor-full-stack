package net.ideablender.kotlinfullstack.req

import kotlinx.serialization.Serializable

@Serializable
data class PlayerReq(var nameFirst:String?, var nameLast:String?, var teamId:Int?, var id:Int? = 0) : REQ