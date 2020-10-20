package net.ideablender.kotlinfullstack.req

import kotlinx.serialization.Serializable

@Serializable
data class TeamReq(var name:String?, var id:Int? = 0) : REQ