package net.ideablender.kotlinfullstack

import kotlinx.coroutines.runBlocking
import net.ideablender.kotlinfullstack.db.DbFactory
import net.ideablender.kotlinfullstack.dto.PlayerDTO
import net.ideablender.kotlinfullstack.dto.TeamDTO
import net.ideablender.kotlinfullstack.exception.TeamNotFoundException
import net.ideablender.kotlinfullstack.helpers.PropsHelper
import net.ideablender.kotlinfullstack.models.Team
import net.ideablender.kotlinfullstack.repo.PlayerRepo
import net.ideablender.kotlinfullstack.repo.TeamRepo
import net.ideablender.kotlinfullstack.req.PlayerReq
import net.ideablender.kotlinfullstack.req.TeamReq
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        TestCreate::class,
        TestUpdate::class,
        TestDelete::class
)
class TestIndex {
    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            PropsHelper.loadProps()
            DbFactory.init()
        }

        fun getRecordCount(): TestRecordCounts {
            return TestRecordCounts(
                    teamCount = runBlocking { TeamRepo.getAll().count() },
                    playerCount = runBlocking { PlayerRepo.getAll().count() }
            )
        }
        fun teamReqMatchesDto(req: TeamReq, dto: TeamDTO): TestDataEval {
            val tde = TestDataEval()
            if (req.name != dto.name) tde.failed("name")
            return tde
        }
        fun playerReqMatchesDto(req: PlayerReq, dto: PlayerDTO): TestDataEval {
            val tde = TestDataEval()
            if (req.nameFirst != dto.nameFirst) tde.failed("nameFirst")
            if (req.nameLast != dto.nameLast) tde.failed("nameLast")
            if (req.teamId != dto.team.id) tde.failed("team")
            return tde
        }
        fun teamHasPlayer(playerDTO: PlayerDTO): Boolean {
            var result = true
            transaction {
                val team: Team = runBlocking { TeamRepo.getModelById(playerDTO.team.id) ?: throw TeamNotFoundException((playerDTO.team.id)) }
                if(team.players.find { it.id.value == playerDTO.id } == null){
                    result = false
                }
            }
            return result
        }
    }

    data class TestRecordCounts(
            val teamCount: Int,
            val playerCount: Int
    ) {
        fun showDifferences(other: TestRecordCounts): List<String> {
            val retList: MutableList<String> = mutableListOf()
            if (this.teamCount != other.teamCount) {
                retList.add("teamCount this:${this.teamCount} | other:${other.teamCount}")
            }
            if (this.playerCount != other.playerCount) {
                retList.add("playerCount this:${this.playerCount} | other:${other.playerCount}")
            }
            return retList
        }
    }
}

const val TST_CATASTROPHIC_FAIL = "Catastrophic failure"
const val TST_REQ_MATCHES_DTO = "Req data should match dto data"
const val TST_AN_EXCEPTION_SHOULD_HAVE_BEEN_THROWN = "An exception should have been thrown"
const val TST_RECORD_COUNTS_ARE_AS_EXPECTED = "The record count should be as expected"
const val TST_PARENT_SHOULD_HAVE_CHILD = "The parent record should contain the child"
const val TST_REMOVED_PARENT_SHOULD_NOT_HAVE_CHILD = "The removed parent record should contain the child"
const val TST_DELETED_SHOULD_BE_NULL = "The deleted obj should be null"
const val TST_PARENT_SHOULD_NOT_HAVE_DELETED_CHILD = "The parent obj should not have the deleted child"

data class TestDataEval(var result: Boolean = true, val errorMsgs: MutableList<String> = mutableListOf()) {
    fun failed(param: String) {
        addMsg("$param does not match")
    }

    fun addMsg(msg: String) {
        result = false
        errorMsgs.add("$msg")
    }
}