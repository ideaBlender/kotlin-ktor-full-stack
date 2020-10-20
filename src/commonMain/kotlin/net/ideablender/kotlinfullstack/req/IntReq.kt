package net.ideablender.kotlinfullstack.req

import kotlinx.serialization.Serializable

@Serializable
data class IntReq(
        var value: Int
) : REQ