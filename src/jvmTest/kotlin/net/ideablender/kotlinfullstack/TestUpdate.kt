package net.ideablender.kotlinfullstack

import kotlinx.coroutines.runBlocking
import net.ideablender.kotlinfullstack.repo.PlayerRepo
import net.ideablender.kotlinfullstack.repo.TeamRepo
import net.ideablender.kotlinfullstack.req.PlayerReq
import net.ideablender.kotlinfullstack.req.TeamReq
import net.ideablender.kotlinfullstack.usecase.player.PlayerUpdate
import net.ideablender.kotlinfullstack.usecase.player.PlayerUpdateImpl
import net.ideablender.kotlinfullstack.usecase.team.TeamUpdate
import net.ideablender.kotlinfullstack.usecase.team.TeamUpdateImpl
import org.junit.Assert
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import java.util.*
import kotlin.test.assertTrue
import kotlin.test.fail

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TestUpdate {

    private val teamUpdate: TeamUpdate = TeamUpdateImpl
    private val playerUpdate: PlayerUpdate = PlayerUpdateImpl

    @Test
    fun `TestUpdates 1 TeamUpdate`() {
        val preCount = TestIndex.getRecordCount()
        val model = runBlocking { TeamRepo.getAll().toList().shuffled().first() }

        val req = TeamReq(
                name = UUID.randomUUID().toString().take(20), // team name capped at 20
                id = model.id
        )
        when (val response = runBlocking {
            teamUpdate.update(TeamUpdate.Request(req))
        }) {
            is TeamUpdate.Response.Failure ->  fail(TST_CATASTROPHIC_FAIL)
            is TeamUpdate.Response.Success -> {
                val postCount = TestIndex.getRecordCount()
                Assert.assertTrue(TST_RECORD_COUNTS_ARE_AS_EXPECTED, postCount == preCount)
                assertTrue(TST_REQ_MATCHES_DTO) {
                    val reqMatchesDtoEval = TestIndex.teamReqMatchesDto(req, response.dto)
                    reqMatchesDtoEval.result
                }
            }
        }
    }

    @Test
    fun `TestUpdates 2 PlayerUpdate - just names`() {
        val preCount = TestIndex.getRecordCount()
        val model = runBlocking { PlayerRepo.getAll().toList().shuffled().first() }

        val req = PlayerReq(
                nameFirst = UUID.randomUUID().toString().take(20), // team name capped at 20
                nameLast = UUID.randomUUID().toString().take(20), // team name capped at 20
                teamId = model.team.id,
                id = model.id
        )
        when (val response = runBlocking {
            playerUpdate.update(PlayerUpdate.Request(req))
        }) {
            is PlayerUpdate.Response.Failure ->  fail(TST_CATASTROPHIC_FAIL)
            is PlayerUpdate.Response.Success -> {
                val postCount = TestIndex.getRecordCount()
                Assert.assertTrue(TST_RECORD_COUNTS_ARE_AS_EXPECTED, postCount == preCount)
                assertTrue(TST_REQ_MATCHES_DTO) {
                    val reqMatchesDtoEval = TestIndex.playerReqMatchesDto(req, response.dto)
                    reqMatchesDtoEval.result
                }
            }
        }
    }

    @Test
    fun `TestUpdates 3 PlayerUpdate - update team`() {
        val preCount = TestIndex.getRecordCount()
        val model = runBlocking { PlayerRepo.getAll().toList().shuffled().first() }
        val teamList = runBlocking { TeamRepo.getAll().toList()}

        val req = PlayerReq(
                nameFirst = UUID.randomUUID().toString().take(20), // team name capped at 20
                nameLast = UUID.randomUUID().toString().take(20), // team name capped at 20
                teamId = teamList.find { it.id != model.team.id }?.id ?: throw Exception("problem finding a team for this test"),
                id = model.id
        )
        when (val response = runBlocking {
            playerUpdate.update(PlayerUpdate.Request(req))
        }) {
            is PlayerUpdate.Response.Failure ->  fail(TST_CATASTROPHIC_FAIL)
            is PlayerUpdate.Response.Success -> {
                val postCount = TestIndex.getRecordCount()
                Assert.assertTrue(TST_RECORD_COUNTS_ARE_AS_EXPECTED, postCount == preCount)
                assertTrue("The teams are differnt") { model.team.id != response.dto.team.id  }
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