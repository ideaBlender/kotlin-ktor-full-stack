package net.ideablender.kotlinfullstack

import kotlinx.coroutines.runBlocking
import net.ideablender.kotlinfullstack.repo.TeamRepo
import net.ideablender.kotlinfullstack.req.PlayerReq
import net.ideablender.kotlinfullstack.req.TeamReq
import net.ideablender.kotlinfullstack.usecase.player.PlayerCreate
import net.ideablender.kotlinfullstack.usecase.player.PlayerCreateImpl
import net.ideablender.kotlinfullstack.usecase.team.TeamCreate
import net.ideablender.kotlinfullstack.usecase.team.TeamCreateImpl
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import kotlin.test.assertTrue
import kotlin.test.fail

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TestCreate {

    private val teamCreate: TeamCreate = TeamCreateImpl
    private val playerCreate: PlayerCreate = PlayerCreateImpl

    @Test
    fun `TestCreates 1 TeamCreate`() {
        val preCount = TestIndex.getRecordCount()
        val req = TeamReq(name = "Yankees")

        when (val response = runBlocking {
            teamCreate.create(TeamCreate.Request(req))
        }) {
            is TeamCreate.Response.Failure ->  fail(TST_CATASTROPHIC_FAIL)
            is TeamCreate.Response.Success -> {
                val postCount = TestIndex.getRecordCount()
                assertTrue(TST_RECORD_COUNTS_ARE_AS_EXPECTED){
                    postCount == preCount.copy(
                            teamCount = preCount.teamCount + 1
                    )}
                assertTrue(TST_REQ_MATCHES_DTO) {
                    val reqMatchesDtoEval = TestIndex.teamReqMatchesDto(req, response.dto)
                    reqMatchesDtoEval.result
                }
            }
        }
    }

    @Test
    fun `TestCreates 2 TeamCreate`() { // need multiple teams to tes swapping a player team
        val preCount = TestIndex.getRecordCount()
        val req = TeamReq(name = "Jets")

        when (val response = runBlocking {
            teamCreate.create(TeamCreate.Request(req))
        }) {
            is TeamCreate.Response.Failure ->  fail(TST_CATASTROPHIC_FAIL)
            is TeamCreate.Response.Success -> {
                val postCount = TestIndex.getRecordCount()
                assertTrue(TST_RECORD_COUNTS_ARE_AS_EXPECTED){
                    postCount == preCount.copy(
                            teamCount = preCount.teamCount + 1
                    )}
                assertTrue(TST_REQ_MATCHES_DTO) {
                    val reqMatchesDtoEval = TestIndex.teamReqMatchesDto(req, response.dto)
                    reqMatchesDtoEval.result
                }
            }
        }
    }

    @Test
    fun `TestCreates 3 PlayerCreate`() {
        val preCount = TestIndex.getRecordCount()
        val allTeams = runBlocking { TeamRepo.getAll() }
        val req = PlayerReq(
                nameFirst = "Don",
                nameLast = "Mattingly",
                teamId = allTeams.first().id
        )

        when (val response = runBlocking {
            playerCreate.create(PlayerCreate.Request(req))
        }) {
            is PlayerCreate.Response.Failure ->  fail(TST_CATASTROPHIC_FAIL)
            is PlayerCreate.Response.Success -> {
                val postCount = TestIndex.getRecordCount()
                assertTrue(TST_RECORD_COUNTS_ARE_AS_EXPECTED){
                    postCount == preCount.copy(
                            playerCount = preCount.playerCount + 1
                    )}
                assertTrue(TST_REQ_MATCHES_DTO) {
                    val reqMatchesDtoEval = TestIndex.playerReqMatchesDto(req, response.dto)
                    reqMatchesDtoEval.result
                }
                assertTrue(TST_PARENT_SHOULD_HAVE_CHILD) {
                    TestIndex.teamHasPlayer(response.dto)
                }
            }
        }
    }
}