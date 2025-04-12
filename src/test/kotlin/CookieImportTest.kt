import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import org.openqa.selenium.Cookie
import org.openqa.selenium.chrome.ChromeDriver
import java.io.File
import java.lang.reflect.Type
import kotlin.test.Test


class CookieImportTest {

    @Test
    fun testImportC() {
        var readText = File("c2.json").readText()
        var gson = Gson()
        val typeToken: Type = object : TypeToken<MutableSet<Cookie>>() {}.type
        var cookieList = gson.fromJson<MutableSet<Cookie>>(readText, typeToken)
        val chromeDriver = ChromeDriver()
        chromeDriver.get("https://www.bilibili.com/")
        cookieList.forEach {
//        sameSite:    no_restriction to Lax
            println("before add cookie ${it}")
            chromeDriver.manage().addCookie(it)
        }
        chromeDriver.get("https://space.bilibili.com")
    }
}