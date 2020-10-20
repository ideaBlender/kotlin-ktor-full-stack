package net.ideablender.kotlinfullstack.pages

import crScrope
import kotlinx.coroutines.launch
import kotlinx.html.ButtonType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import net.ideablender.kotlinfullstack.dto.TeamDTO
import net.ideablender.kotlinfullstack.req.TeamReq
import net.ideablender.kotlinfullstack.store.FormData
import net.ideablender.kotlinfullstack.store.TeamService
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*


interface PageStateTeam:RState{
    var entites:List<TeamDTO>
    var req:TeamReq
}
class Teams : RComponent<RProps, PageStateTeam>(){
    override fun PageStateTeam.init() {
        TeamService.registerCallback(key = "Teams", ::handleStoreChange)
        entites = TeamService.getAll()
        req = TeamReq("")
    }

    private fun handleStoreChange(){
        setState {
            entites = TeamService.getAll()
            req = TeamReq("")
        }
    }

    fun handleFormChange(fd: FormData){
        when(fd.param){
            "name" ->{
                setState {
                    req = req.copy(name = fd.theValue as String)
                }
            }
        }
    }

    override fun RBuilder.render() {
        div("card"){
            div("card-header"){
                +"Teams"
            }
            div("card-body"){
                if(state.entites.count() > 0){
                    table("table table-striped table-bordered") {
                        thead {
                            tr{
                                th {
                                    +"Id"
                                }
                                th {
                                    +"Name"
                                }
                                th{
                                    +"Players"
                                }
                            }
                        }
                        tbody {
                            state.entites.forEach {team ->
                                tr{
                                    td {
                                        +team.id.toString()
                                    }
                                    td {
                                        +team.name
                                    }
                                    td {
                                        ul{
                                            team.players.forEach { player ->
                                                li{
                                                    +player.detail
                                                }
                                            }
                                        }
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
                            +"Name:"
                        }
                        div("col-sm-8"){
                            input(classes = "form-control") {
                                attrs.value = state.req.name ?: ""
                                attrs.onChangeFunction = {
                                    val target = it.target as HTMLInputElement
                                    handleFormChange(FormData(param = "name", theValue = target.value))
                                }
                            }
                        }
                        div("col-sm-2"){
                            button(classes = "btn btn-primary", type = ButtonType.button) {
                                attrs.onClickFunction = {
                                    val toSave = state.req
                                    crScrope.launch {
                                        console.log(toSave)
                                        TeamService.create(toSave)
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