package org.coderpwh


import com.github.dockerjava.api.model.Driver
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.serialization.json.Json
import org.openqa.selenium.By
import org.openqa.selenium.Cookie
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.devtools.NetworkInterceptor
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.remote.http.Filter
import org.openqa.selenium.remote.http.HttpHandler
import org.openqa.selenium.support.ui.WebDriverWait
import pojo.ListItem
import pojo.MyData
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.lang.reflect.Type
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.random.Random


fun main(args: Array<String>) {
    //windows: ./chrome --remote-debugging-port=9222 --user-data-dir='D:\temp\aa1'
    //mac: open -a "Google Chrome" --args --remote-debugging-port=9222 --user-data-dir="/Users/pwhcoder/temp"
    println("正在启动项目...")
//    可配置参数
    val keyValuePairs = args
        .filter { it.contains("=") }
        .map {
            val (key, value) = it.split("=", limit = 2)
            key to value
        }
        .toMap()
    var cookieExist = File("cookie.json").exists()
    val options = ChromeOptions().apply {
        this.setBrowserVersion("135")
        if (cookieExist && !args.contains("-v")) {
            addArguments("--headless")
        }
//        setExperimentalOption("debuggerAddress", "localhost:9222")
    }
    val chromeDriver = ChromeDriver(options)
    chromeDriver.get("https://www.bilibili.com/")
//    判断文件是否存在，如果存在，则不用登录，否则登录并保存cookie
    if (cookieExist) {
        var readText = File("cookie.json").readText()
        var gson = Gson()
        val typeToken: Type = object : TypeToken<MutableSet<Cookie>>() {}.type
        var cookieList = gson.fromJson<MutableSet<Cookie>>(readText, typeToken)
        cookieList.forEach {
            chromeDriver.manage().addCookie(it)
        }
    } else {
        println("请登录b站,登录成功输入y")
        var next = Scanner(System.`in`).next()
        if (next != "y") {
            println("未登录")
            return
        }
    }
    var cookieNamed = chromeDriver.manage().getCookieNamed("bili_jct")
    if (cookieNamed != null) {
        println("登录成功")
    } else {
        println("未登录，请重试")
        return
    }
    chromeDriver.get("https://www.bilibili.com/")
    var cookies = chromeDriver.manage().cookies
    val gson = Gson()
    var encodeToString = gson.toJson(cookies)
    File("cookie.json").writeText(encodeToString)

    val sc = Scanner(System.`in`)
    while (true) {
        showMenu()
        var next = sc.next()
        if (next == "1") {
            println("请输入用户mid")
            var mid = sc.next()
            exportAllFollowByMid(chromeDriver, mid)
        } else if (next == "2") {
            followAll(chromeDriver, "result.csv")
        } else if (next == "3") {
            println("请输入用户mid")
            var mid = sc.next()
            exportAllFanByMid(chromeDriver, mid)
        } else {
            println("退出")
            break
        }
    }

//        followAll(chromeDriver,"result.csv")
//    File("result.csv").delete()

//    导出到csv   437966767 9824766
//        exportAllFollowByMid(chromeDriver,"946974")

}

fun showMenu() {
//    打印功能 1. 导出所有关注列表 2.从csv关注所有
    println("请选择功能：1. 导出所有关注列表 \n2.从csv关注所有\n3.导出所有粉丝列表\n其他输入退出应用")
}

fun exportAllFollowByMid(chromeDriver: ChromeDriver, mid: String) {
    NetworkInterceptor(chromeDriver, Filter { next ->
        HttpHandler { req ->
            val execute = next.execute(req)
//        println(req.uri)
            if (req.uri.contains("followings")) {
                println(req.uri)
                val jsonStr = execute.content.get().bufferedReader(Charsets.UTF_8).readLine()
                val data = Json {
                    ignoreUnknownKeys = true
                }.decodeFromString<MyData>(jsonStr)
//            println(data)
                exportToCsv("result.csv", listOf("id", "用户名", "关注时间", "用户签名", "头像"), data.data!!.list)
                val r = data.data!!.list.map { it.uname }.joinToString("\n")
                println(r)
                println(execute.content.get().bufferedReader(Charsets.UTF_8).readLine())
            }
            execute
        }
    })
    chromeDriver.get("https://space.bilibili.com/$mid/fans/follow?tagid=-1")
    try {
        while (true) {
            val webDriverWait = WebDriverWait(chromeDriver, Duration.ofSeconds(5))
            val re = webDriverWait.until { t ->
                t.findElement(By.xpath("//button[text()='下一页']"))
            }
            Thread.sleep(1000L)
            re.click()
        }
    } catch (e: Exception) {
        println(e)
    }
}

fun exportAllFanByMid(chromeDriver: ChromeDriver, mid: String) {
    NetworkInterceptor(chromeDriver, Filter { next ->
        HttpHandler { req ->
            val execute = next.execute(req)
//        println(req.uri)
            if (req.uri.contains("x/relation/fans")) {
                println(req.uri)
                val jsonStr = execute.content.get().bufferedReader(Charsets.UTF_8).readLine()
                println(jsonStr)
                val data = Json {
                    ignoreUnknownKeys = true
                }.decodeFromString<MyData>(jsonStr)
//            println(data)
                exportToCsv("result.csv", listOf("id", "用户名", "关注时间", "用户签名", "头像"), data.data!!.list)
                val r = data.data!!.list.map { it.uname }.joinToString("\n")
                println(r)
                println(execute.content.get().bufferedReader(Charsets.UTF_8).readLine())
            }
            execute
        }
    })
    chromeDriver.get("https://space.bilibili.com/$mid/relation/fans")
    try {
        while (true) {
            val webDriverWait = WebDriverWait(chromeDriver, Duration.ofSeconds(5))
            val re = webDriverWait.until { t ->
                t.findElement(By.xpath("//button[text()='下一页']"))
            }
            Thread.sleep(1000L)
            re.click()
        }
    } catch (e: Exception) {
        println(e)
    }
}

fun exportToCsv(fileName: String, headers: List<String>, data: List<ListItem>) {
    // 使用 UTF-8 编码来写入文件
    val writer = BufferedWriter(OutputStreamWriter(FileOutputStream(fileName, true), "utf-8"))
    writer.use {
        // 写入CSV的头部
        /*it.write(headers.joinToString(","))
        it.newLine()*/

        // 写入每行数据
        data.forEach { item ->
            // 将每个字段值拼接成 CSV 行
            it.write(
                "${item.mid},${item.uname},${convertTimestampToDateTime(item.mtime!!.toLong())},${
                    item.sign?.replace(
                        "\n",
                        ""
                    )
                },${item.face}"
            )
            it.newLine()
        }
    }
}

fun convertTimestampToDateTime(timestamp: Long): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault())
}

fun followByMid(driver: ChromeDriver, mid: String, duration: Duration = Duration.ofMillis(500)): Boolean {
    driver.get("https://space.bilibili.com/$mid")
    try {
        val webDriverWait = WebDriverWait(driver, Duration.ofSeconds(2))
        val r = Random(System.currentTimeMillis()).nextInt(500)
        Thread.sleep(duration.plus(Duration.ofMillis(r.toLong())))
        val re = webDriverWait.until { t ->
            t.findElement(By.cssSelector(".follow-btn"))
        }
//        re.click()
        re.clickElement(driver)
        return true
    } catch (e: Exception) {
        println(e)
    }
    return false
}

fun followAll(driver: ChromeDriver, fileName: String, batchNum: Int = 50, duration: Duration = Duration.ofMillis(500)) {
    var count = 0
    File(fileName).bufferedReader(Charsets.UTF_8).forEachLine { text ->
        val values = text.split(",").map { it.trim() }
        val isSuc = followByMid(driver, values[0])
        if (count % batchNum == 0) {
            Thread.sleep(duration.multipliedBy(4))
        }
        if (isSuc) {
            count++
            println("关注成功 up主：${values[1]} uid:${values[0]}")
        } else {
            println("已经成功关注数量 $count")
            println("关注失败，触发风控，停止运行")
            return@forEachLine
        }
    }
    println("运行成功")
    println("已经成功关注数量 $count")
}

fun WebElement.clickElement(driver: ChromeDriver) {
    val location = this.location
    val size = this.size
    // 按钮的左上角坐标
    val x = location.x
    val y = location.y
    // 按钮的宽度和高度
    val width = size.width
    val height = size.height
    // 随机生成点击的偏移量
    val offsetX = Random.nextInt(width)  // x轴的偏移
    val offsetY = Random.nextInt(height) // y轴的偏移
    // 计算随机点击的位置
    val clickX = x + offsetX
    val clickY = y + offsetY
//    println("随机点击位置: ($clickX, $clickY)")
    // 使用 Actions 点击指定位置
    val actions = Actions(driver)
    actions.moveByOffset(clickX, clickY).click().perform()
}



