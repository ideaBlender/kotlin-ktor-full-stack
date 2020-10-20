package net.ideablender.kotlinfullstack.helpers

import net.ideablender.kotlinfullstack.req.REQ

object LogHelper {
    fun uCCreateError(clazz:String, req: REQ, e:Exception):String = "Failed to create $clazz : $req. Message: ${e.message}"
    fun uCUpdateError(clazz:String, req:REQ, id:Int, e:Exception):String = "Failed to update $clazz with id of $id : $req. Message: ${e.message}"
    fun uCDeleteError(clazz:String, id:Int, e:Exception):String = "Failed to delete $clazz with id of $id. Message: ${e.message}"
}