package view

import javafx.scene.control.Alert
import javafx.scene.control.Dialog
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import tornadofx.View
import tornadofx.alert
import util.ConfigReader

class OptionView : View("Option ") {
    override val root: AnchorPane by fxml("/OptionView.fxml")

    val inputUrl : TextField by fxid("OptionUrlInput")



    var url = ""
    init {
        url = ConfigReader().getApi()
        inputUrl.text = url
    }

    fun OptionModalSave(){
        url = inputUrl.text
        ConfigReader().changeAPI(url)
        alert(type = Alert.AlertType.INFORMATION,content = "Please Click Refresh Button to take effect",header = "Success save configuration")

        close()
    }

    fun OptionModalCancel(){
        this.close()
    }
}