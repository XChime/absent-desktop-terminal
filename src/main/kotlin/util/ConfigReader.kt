package util

import com.natpryce.konfig.*
import com.natpryce.konfig.ConfigurationProperties.Companion.systemProperties
import tornadofx.urlEncoded
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.HashMap

class ConfigReader {
    val apiurl = Key("server.api", stringType)

    val machineid = Key("machine.id", stringType)
    val machinename = Key("machine.name", stringType)
    val machinesecret = Key("machine.secret", stringType)

    val config = systemProperties() overriding
            EnvironmentVariables() overriding
            ConfigurationProperties.fromFile(File("config.properties")) overriding
            ConfigurationProperties.fromResource("defaults.properties")

    val session = systemProperties() overriding
            EnvironmentVariables() overriding
            ConfigurationProperties.fromFile(File("session.properties"))

    /*For getAPI URL from properties*/
    fun getApi() : String{
        return config[apiurl]
    }

    fun getSession() : HashMap<String,String>?{
        val map = HashMap<String,String>()
        val id = session.getOrNull(machineid)
        val name = session.getOrNull(machinename)
        val secret = session.getOrNull(machinesecret)
        if (id != null && name!= null && secret!=null){
            map["id"] = id
            map["name"] = name
            map["secret"] = secret

            return map
        }
        return null
    }

    /*Write new url to properties*/
    fun changeAPI(newProperty : String){
        val fis = FileOutputStream("config.properties")
        val prop = Properties()
        prop.setProperty("server.api",newProperty)
        prop.store(fis,null)
    }

    //Write Machine Code
    fun writeLocalConfig(id: String,name : String,secret : String){
        val fis = FileOutputStream("session.properties")
        val prop = Properties()
        prop.setProperty("machine.id",id)
        prop.setProperty("machine.name",name)
        prop.setProperty("machine.secret",secret)
        prop.store(fis,null)
    }
}