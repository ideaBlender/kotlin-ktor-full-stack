import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.header
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import net.ideablender.kotlinfullstack.store.PlayerService
import net.ideablender.kotlinfullstack.store.TeamService
import react.dom.render

val crScrope = MainScope()
val endpoint = window.location.origin
val jsonClient = HttpClient {
    install(JsonFeature) { serializer = KotlinxSerializer() }
    defaultRequest {
        header("key", "1")
    }
}

fun main() {
    crScrope.launch{
        TeamService.fetchAll()
        PlayerService.fetchAll()
    }
    render(document.getElementById("root")) {
        child(Index::class){}
    }
}
