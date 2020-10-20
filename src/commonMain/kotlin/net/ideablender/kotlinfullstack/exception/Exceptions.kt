package net.ideablender.kotlinfullstack.exception

class MissingMandatoryFieldException(fieldName: String) :
    Exception("The field $fieldName is mandatory, but was not supplied")

data class TeamNotFoundException(val id: Int) : Exception("Could not find a Team with id : $id")
data class PlayerNotFoundException(val id: Int) : Exception("Could not find a player with id : $id")
data class ChildrenStillPresentException(val model:String, val childeModel:String, val describers:List<String>):Exception("Could not delete $model, $childeModel relationships present : $describers")