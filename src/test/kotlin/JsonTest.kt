import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.openqa.selenium.Cookie
import java.io.File
import java.lang.reflect.Type
import kotlin.test.Test

class JsonTest {

    @Test
    fun testJson() {
        var cookie = Cookie("a","b")
        var gson = Gson()
        var toJson = gson.toJson(cookie)
        println(toJson)
    }

    @Test
    fun testFileToJson() {
        var readText = File("c2.json").readText()
        var gson = Gson()
//        var cookieListType = mutableSetOf<Cookie>()
        val typeToken:Type = object : TypeToken<MutableSet<Cookie>>() {}.type
        var fromJson = gson.fromJson<MutableSet<Cookie>>(readText, typeToken)
        println(fromJson.first().javaClass)
        println(fromJson)
    }
}