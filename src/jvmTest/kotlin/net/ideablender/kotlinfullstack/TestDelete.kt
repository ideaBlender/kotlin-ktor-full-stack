package net.ideablender.kotlinfullstack

import kotlinx.coroutines.runBlocking
import net.ideablender.kotlinfullstack.dto.PlayerDTO
import net.ideablender.kotlinfullstack.dto.TeamDTO
import net.ideablender.kotlinfullstack.exception.ChildrenStillPresentException
import net.ideablender.kotlinfullstack.repo.PlayerRepo
import net.ideablender.kotlinfullstack.repo.TeamRepo
import net.ideablender.kotlinfullstack.req.IntReq
import net.ideablender.kotlinfullstack.usecase.player.PlayerDelete
import net.ideablender.kotlinfullstack.usecase.player.PlayerDeleteImpl
import net.ideablender.kotlinfullstack.usecase.team.TeamDelete
import net.ideablender.kotlinfullstack.usecase.team.TeamDeleteImpl
import org.junit.Assert
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import kotlin.test.assertTrue
import kotlin.test.fail

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TestDelete {

    private val teamDelete: TeamDelete = TeamDeleteImpl
    private val playerDelete: PlayerDelete = PlayerDeleteImpl

    @Test
    fun `TestDeletes 1 TeamDelete`() {
        val preCount = TestIndex.getRecordCount()
        val model :TeamDTO = runBlocking { TeamRepo.getAll().find { it.players.count() == 0 }!! }
        when (val response = runBlocking {
            teamDelete.delete(TeamDelete.Request(IntReq(model.id)))
        }) {
            is TeamDelete.Response.Failure ->  fail(TST_CATASTROPHIC_FAIL)
            is TeamDelete.Response.Success -> {
                val postCount = TestIndex.getRecordCount()
                val deleted = runBlocking { TeamRepo.getById(model.id) }
                assertTrue(TST_DELETED_SHOULD_BE_NULL) {
                    deleted == null
                }
                Assert.assertTrue(TST_RECORD_COUNTS_ARE_AS_EXPECTED, postCount == preCount.copy(teamCount = preCount.teamCount -1))
            }
        }
    }

    @Test
    fun `TestDeletes 2 TeamDelete - should fail for relationships present`() {
        val preCount = TestIndex.getRecordCount()
        val model :TeamDTO = runBlocking { TeamRepo.getAll().find { it.players.count() > 0 }!! }
        when (val response = runBlocking {
            teamDelete.delete(TeamDelete.Request(IntReq(model.id)))
        }) {
            is TeamDelete.Response.Failure ->  {
                val postCount = TestIndex.getRecordCount()
                val deleted = runBlocking { TeamRepo.getById(model.id) }
                Assert.assertFalse(TST_DELETED_SHOULD_BE_NULL, deleted == null)
                Assert.assertTrue(TST_RECORD_COUNTS_ARE_AS_EXPECTED, postCount == preCount) //should be the same because children being present stops delete
                assertTrue(TST_AN_EXCEPTION_SHOULD_HAVE_BEEN_THROWN) {
                    response.exception is ChildrenStillPresentException
                }
            }
            is TeamDelete.Response.Success -> {
                fail(TST_CATASTROPHIC_FAIL)
            }
        }
    }

    @Test
    fun `TestDeletes 3 PlayerDelete`() {
        val preCount = TestIndex.getRecordCount()
        val model :PlayerDTO = runBlocking { PlayerRepo.getAll().shuffled().first() }
        when (val response = runBlocking {
            playerDelete.delete(PlayerDelete.Request(IntReq(model.id)))
        }) {
            is PlayerDelete.Response.Failure ->  fail(TST_CATASTROPHIC_FAIL)
            is PlayerDelete.Response.Success -> {
                val postCount = TestIndex.getRecordCount()
                val deleted = runBlocking { PlayerRepo.getById(model.id) }
                assertTrue(TST_DELETED_SHOULD_BE_NULL) {
                    deleted == null
                }
                Assert.assertTrue(TST_RECORD_COUNTS_ARE_AS_EXPECTED, postCount == preCount.copy(playerCount = preCount.playerCount -1))
            }
        }
    }
}