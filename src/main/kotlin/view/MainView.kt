package view

import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.EventHandler
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane
import javafx.util.Duration
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.glxn.qrgen.core.image.ImageType
import net.glxn.qrgen.javase.QRCode
import tornadofx.Rest
import tornadofx.View
import tornadofx.runLater
import tornadofx.string
import util.ApiNetwork
import util.ConfigReader
import util.constant
import java.io.FileInputStream
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import javax.json.JsonException
import javax.json.JsonObject
import kotlin.collections.HashMap
import kotlin.concurrent.schedule

class MainView : View("Main") {
    override val root: AnchorPane by fxml("/MainViews.fxml")
    val api : Rest by inject()

    val timeText : Label by fxid("timeText")
    val nameText : Label by fxid("nameText")
    val QRImageView : ImageView by fxid("QRImageView")

    var url = ""
    var session = HashMap<String,String>()
    init {
        session = ConfigReader().getSession()!!
        if (session["id"]!!.isNotEmpty()){
            nameText.text = session["name"]
        }
        url = ConfigReader().getApi()
        api.baseURI = url
        refreshTimeText()
        GlobalScope.launch {
            delay(1000)
            requestToken()
        }
    }
    fun requestToken(){
        var objects : JsonObject? = null
        runAsync {
            val params = "id=${session["id"]}&&secret=${session["secret"]}"
            objects = ApiNetwork().POST_REQUEST(api,constant().URL_TOKEN_REQUEST,params)!!
        }ui{
            parseJSONToken(objects!!)
        }
    }

    private fun parseJSONToken(obj : JsonObject){
        try {
            if (!obj.getBoolean("Error")){
                val token = obj.string("Token")

                val file = QRCode.from(token.toString()).withSize(1024,1024).file()
                val inputStream = FileInputStream(file)
                QRImageView.image = Image(inputStream)
                QRImageView.isSmooth = true
            }
        }catch (e : JsonException){
            e.printStackTrace()
        }
    }

    private fun refreshTimeText(){
        val timeline = Timeline(
            KeyFrame(Duration.seconds(0.0),EventHandler{
                val current = LocalDateTime.now()
                timeText.text = current.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
            }), KeyFrame(Duration.seconds(1.0))
        )
        timeline.cycleCount = Animation.INDEFINITE
        timeline.play()
    }

}