package util

import tornadofx.Rest
import tornadofx.urlEncoded
import java.nio.charset.StandardCharsets
import javax.json.Json
import javax.json.JsonObject

class ApiNetwork {
    fun POST_REQUEST(rest: Rest, path: String,urlencode : String ): JsonObject? {
        val response = rest.post(path,urlencode.byteInputStream(StandardCharsets.UTF_8)){
            it.addHeader("Content-Type", "application/x-www-form-urlencoded")
        }

        try {
            if (response.ok()){
                //println(response.list()[0].asJsonObject())
                return response.list()[0].asJsonObject()
            }else{
                println("ERROR :" + response.statusCode)
            }
        }finally {
            response.consume()
        }
        return null
    }
}