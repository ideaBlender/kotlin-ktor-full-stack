package net.ideablender.kotlinfullstack.pages

import crScrope
import kotlinx.coroutines.launch
import kotlinx.html.ButtonType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import net.ideablender.kotlinfullstack.dto.PlayerDTO
import net.ideablender.kotlinfullstack.dto.TeamDTO
import net.ideablender.kotlinfullstack.req.PlayerReq
import net.ideablender.kotlinfullstack.req.TeamReq
import net.ideablender.kotlinfullstack.store.FormData
import net.ideablender.kotlinfullstack.store.PlayerService
import net.ideablender.kotlinfullstack.store.TeamService
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import react.*
import react.dom.*


interface PageStatePlayers:RState{
    var entites:List<PlayerDTO>
    var req:PlayerReq
}
class Players : RComponent<RProps, PageStatePlayers>(){
    override fun PageStatePlayers.init() {
        PlayerService.registerCallback(key = "Players", ::handleStoreChange)
        TeamService.registerCallback(key = "Players", ::handleStoreChange)
        entites = PlayerService.getAll()
        req = PlayerReq(nameFirst = "", nameLast = "", teamId = 1)
    }

    private fun handleStoreChange(){
        setState {
            entites = PlayerService.getAll()
            req = PlayerReq(nameFirst = "", nameLast = "", teamId = 1)
        }
    }

    fun handleFormChange(fd: FormData){
        when(fd.param){
            "nameFirst" ->{
                setState {
                    req = req.copy(nameFirst = fd.theValue as String)
                }
            }
            "nameLast" ->{
                setState {
                    req = req.copy(nameLast = fd.theValue as String)
                }
            }
            "teamId" ->{
                setState {
                    req = req.copy(teamId = fd.theValue as Int)
                }
            }
        }
    }
    override fun RBuilder.render() {
        console.log(state.req)
        div("card"){
            div("card-header"){
                +"Players"
            }
            div("card-body"){
                if(state.entites.count() > 0){
                    table("table table-striped table-bordered") {
                        thead {
                            tr{
                                th {
                                    +"id"
                                }
                                th {
                                    +"First Name"
                                }
                                th{
                                    +"Last Name"
                                }
                            }
                        }
                        tbody {
                            state.entites.forEach {player ->
                                tr{
                                    td {
                                        +player.id.toString()
                                    }
                                    td {
                                        +player.nameFirst
                                    }
                                    td {
                                        +player.nameLast
                                    }
                                    td {
                                        +player.team.detail
                                    }
                                }
                            }
                        }
                    }
                }
                hr{}
                form{
                    div("form-group row") {
                        label(classes = "col-sm-2 col-form-label") {
                            +"First Name:"
                        }
                        div("col-sm-4"){
                            input(classes = "form-control") {
                                attrs.value = state.req.nameFirst ?: ""
                                attrs.onChangeFunction = {
                                    val target = it.target as HTMLInputElement
                                    handleFormChange(FormData(param = "nameFirst", theValue = target.value))
                                }
                            }
                        }

                        label(classes = "col-sm-2 col-form-label") {
                            +"Last Name:"
                        }
                        div("col-sm-4"){
                            input(classes = "form-control") {
                                attrs.value = state.req.nameLast ?: ""
                                attrs.onChangeFunction = {
                                    val target = it.target as HTMLInputElement
                                    handleFormChange(FormData(param = "nameLast", theValue = target.value))
                                }
                            }
                        }

                    }
                    div("form-group row") {
                        label(classes = "col-sm-2 col-form-label") {
                            +"First Name:"
                        }
                        div("col-sm-8"){
                            select("form-control"){
                                if(state.req.teamId != null){
                                    attrs.value = state.req.teamId.toString()
                                }
                                attrs.onChangeFunction = {
                                    val target = it.target as HTMLSelectElement
                                    handleFormChange(FormData(param = "teamId", theValue = target.value.toInt()))
                                }
                                TeamService.getAll().forEach {
                                    option {
                                        attrs.value = it.id.toString()
                                        +it.name
                                    }
                                }
                            }
                        }

                        div("col-sm-2"){
                            button(classes = "btn btn-primary", type = ButtonType.button) {
                                attrs.onClickFunction = {
                                    val toSave = state.req
                                    crScrope.launch {
                                        console.log(toSave)
                                        PlayerService.create(toSave)
                                    }
                                }
                                +"Save"
                            }
                        }
                    }
                }
            }
        }
    }
}