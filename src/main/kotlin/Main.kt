import javafx.scene.Scene
import javafx.stage.Stage
import tornadofx.App
import tornadofx.UIComponent
import tornadofx.runAsync
import util.ConfigReader
import view.LoginView
import kotlin.reflect.KClass

class Main : App() {

    override val primaryView= LoginView::class

    override fun start(stage: Stage) {
        stage.isResizable = false
        super.start(stage)
    }
}