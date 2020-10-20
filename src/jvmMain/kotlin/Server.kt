import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.gzip
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import net.ideablender.kotlinfullstack.BootStrap
import net.ideablender.kotlinfullstack.db.DbFactory
import net.ideablender.kotlinfullstack.dto.PlayerDTO
import net.ideablender.kotlinfullstack.dto.TeamDTO
import net.ideablender.kotlinfullstack.helpers.PropsHelper
import net.ideablender.kotlinfullstack.repo.PlayerRepo
import net.ideablender.kotlinfullstack.repo.TeamRepo
import net.ideablender.kotlinfullstack.req.PlayerReq
import net.ideablender.kotlinfullstack.req.TeamReq
import net.ideablender.kotlinfullstack.usecase.player.PlayerCreate
import net.ideablender.kotlinfullstack.usecase.player.PlayerCreateImpl
import net.ideablender.kotlinfullstack.usecase.team.TeamCreate
import net.ideablender.kotlinfullstack.usecase.team.TeamCreateImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun main() {
    val logger: Logger = LoggerFactory.getLogger("Server.kt")
    PropsHelper.loadProps()
    val port = System.getenv("PORT")?.toInt() ?: 9090
    DbFactory.init()
    BootStrap.loadData()
    embeddedServer(Netty, port) {
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            method(HttpMethod.Get)
            method(HttpMethod.Post)
            method(HttpMethod.Delete)
            anyHost()
        }
        install(Compression) {
            gzip()
        }
        routing {
            get("/") {
                call.respondText(
                    this::class.java.classLoader.getResource("index.html")!!.readText(),
                    ContentType.Text.Html
                )
            }
            static("/") {
                resources("")
            }
            route("/hello") {
                get {
                    call.respond("Hello From Ktor")
                }
            }
            route(TeamDTO.getRestPath()) {
                get {
                    call.respond(TeamRepo.getAll())
                }
                post{
                    val teamCreate: TeamCreate = TeamCreateImpl
                    val req = call.receive<TeamReq>()
                    val result = teamCreate.create(TeamCreate.Request(req))
                    when (result) {
                        is TeamCreate.Response.Failure -> {
                            // yum
                        }
                        is TeamCreate.Response.Success -> {
                            call.respond(result.dto)
                        }
                    }
                }
            }
            route(PlayerDTO.getRestPath()) {
                get {
                    call.respond(PlayerRepo.getAll())
                }
                post{
                    val playerCreate: PlayerCreate = PlayerCreateImpl
                    val req = call.receive<PlayerReq>()
                    val result = playerCreate.create(PlayerCreate.Request(req))
                    when (result) {
                        is PlayerCreate.Response.Failure -> {
                            // yum
                        }
                        is PlayerCreate.Response.Success -> {
                            call.respond(result.dto)
                        }
                    }
                }
            }
        }
    }.start(wait = true)
}
