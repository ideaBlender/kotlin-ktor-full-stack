package net.ideablender.kotlinfullstack

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.ideablender.kotlinfullstack.dto.TeamDTO
import net.ideablender.kotlinfullstack.repo.TeamRepo
import net.ideablender.kotlinfullstack.req.PlayerReq
import net.ideablender.kotlinfullstack.req.TeamReq
import net.ideablender.kotlinfullstack.usecase.player.PlayerCreate
import net.ideablender.kotlinfullstack.usecase.player.PlayerCreateImpl
import net.ideablender.kotlinfullstack.usecase.team.TeamCreate
import net.ideablender.kotlinfullstack.usecase.team.TeamCreateImpl

object BootStrap {
    private val teamCreate: TeamCreate = TeamCreateImpl
    private val playerCreate: PlayerCreate = PlayerCreateImpl

    private val YANKEES = "Yankees"
    private val RANGERS = "Rangers"
    private val JETS = "Jets"

    fun loadData(){
        GlobalScope.launch(Dispatchers.IO) {
           createTeams()
            createPlayers()
        }
    }

    private suspend fun createTeams(){
        teamCreate.create(TeamCreate.Request(TeamReq(name = YANKEES)))
        teamCreate.create(TeamCreate.Request(TeamReq(name = RANGERS)))
        teamCreate.create(TeamCreate.Request(TeamReq(name = JETS)))
    }

    suspend fun getRandomTeam():TeamDTO = TeamRepo.getAll().shuffled().first()

    private suspend fun createPlayers(){
        val yankees = TeamRepo.getByName(YANKEES)
        val jets = TeamRepo.getByName(JETS)
        val rangers = TeamRepo.getByName(RANGERS)
        playerCreate.create(PlayerCreate.Request(PlayerReq(nameFirst = "Derek", nameLast = "Jeter", teamId = yankees.id)))
        playerCreate.create(PlayerCreate.Request(PlayerReq(nameFirst = "Mickey", nameLast = "Mantle", teamId = yankees.id)))
        playerCreate.create(PlayerCreate.Request(PlayerReq(nameFirst = "Joe", nameLast = "DiMaggio", teamId = yankees.id)))
        playerCreate.create(PlayerCreate.Request(PlayerReq(nameFirst = "Mariano", nameLast = "Rivera", teamId = yankees.id)))
        playerCreate.create(PlayerCreate.Request(PlayerReq(nameFirst = "Lou", nameLast = "Gehrig", teamId = yankees.id)))
        playerCreate.create(PlayerCreate.Request(PlayerReq(nameFirst = "Mark", nameLast = "Messier", teamId = rangers.id)))
        playerCreate.create(PlayerCreate.Request(PlayerReq(nameFirst = "Joe", nameLast = "Namath", teamId = jets.id)))
    }
}