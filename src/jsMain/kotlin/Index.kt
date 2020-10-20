import net.ideablender.kotlinfullstack.pages.Players
import net.ideablender.kotlinfullstack.pages.Teams
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.div
import react.dom.h1


class Index : RComponent<RProps, RState>(){
    override fun RBuilder.render() {
        div("container") {
            h1{
                +"Hello From Kotlin React"
            }
            child(Teams::class){}
            child(Players::class){}
        }
    }
}