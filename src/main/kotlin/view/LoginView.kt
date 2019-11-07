package view

import javafx.scene.control.Alert
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.stage.Modality
import sun.applet.Main
import tornadofx.*
import util.ApiNetwork
import util.ConfigReader
import util.constant
import javax.json.JsonException
import javax.json.JsonObject

class LoginView : View("Login Machine!"){
    val api : Rest by inject()
    var url = ""

    override val root: AnchorPane by fxml("/LoginViews.fxml")
    val shareCode : TextField by fxid("sharec_input")

    init {
        val session = ConfigReader().getSession()
        if (session != null){
            if (session["id"]!!.isNotEmpty()){
                runAsync {
                    replaceLogin()
                }
            }
        }else{
            url = ConfigReader().getApi()
            api.baseURI = url
        }
    }
    private fun replaceLogin(){
        this.replaceWith<MainView>(sizeToScene = true,centerOnScreen = true,transition = ViewTransition.Metro(500.millis))
    }

    fun LoginMachine(){
        var objects : JsonObject?= null
        val shc = shareCode.text
        if (shc.isBlank()){
            alert(Alert.AlertType.ERROR,header = "Input Empty",content = "Must Enter Input")
        }else{
            runAsync {
                val param = "sharecode=$shc"
                objects = ApiNetwork().POST_REQUEST(api,constant().URL_MACHINE_LOGIN,param)
            }ui{
                if (!objects.isNullOrEmpty()){
                    parseJsonLogin(objects!!)
                }
            }
        }
    }

    fun parseJsonLogin(objects : JsonObject){
        try{
            if (!objects.getBoolean("Error")){
                information(header = "Success!!",content = objects.getString("Message"))
                val machine = objects.getJsonObject("Machine")
                val id = machine.getString("IDMachine")
                val name = machine.getString("Name")
                val secret = machine.getString("Secret")
                ConfigReader().writeLocalConfig(id,name,secret)
                replaceLogin()
            }
        }catch (e : JsonException){
            e.printStackTrace()
        }
    }

    fun OpenOption(){
        OptionView().openModal(resizable = false)
    }
}