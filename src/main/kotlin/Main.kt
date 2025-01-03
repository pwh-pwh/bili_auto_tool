package org.coderpwh


import kotlinx.serialization.json.Json
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.devtools.NetworkInterceptor
import org.openqa.selenium.remote.http.Filter
import org.openqa.selenium.remote.http.HttpHandler
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import pojo.ListItem
import pojo.MyData
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.coroutines.coroutineContext


fun main() {
    //windows: ./chrome --remote-debugging-port=9222 --user-data-dir='D:\temp\aa1'
    //mac: open -a "Google Chrome" --args --remote-debugging-port=9222 --user-data-dir="/Users/pwhcoder/temp"
    val options = ChromeOptions().apply {
        setExperimentalOption("debuggerAddress", "localhost:9222")
    }
    val chromeDriver = ChromeDriver(options)

//    根据csv文件关注
    followAll(chromeDriver,"result.csv")
    File("result.csv").delete()

//    导出到csv
//    exportAllFollowByMid(chromeDriver,"437966767")


    Thread.sleep(1000L)

}

fun exportAllFollowByMid(chromeDriver: ChromeDriver,mid: String) {
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
                t.findElement(By.className("be-pager-next"))
            }
            Thread.sleep(1000L)
            re.click()
        }
    }catch (e: Exception) {
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

fun followByMid(driver: ChromeDriver, mid: String) {
    driver.get("https://space.bilibili.com/$mid")
    try {
        val webDriverWait = WebDriverWait(driver, Duration.ofSeconds(2))
        val re = webDriverWait.until { t ->
            t.findElement(By.cssSelector(".h-f-btn.h-follow"))
        }
        re.click()
    }catch (e: Exception) {
        println(e)
    }

}

fun followAll(driver: ChromeDriver, fileName: String) {
    File(fileName).bufferedReader(Charsets.UTF_8).
            forEachLine { text ->
                val values = text.split(",").map { it.trim() }
                followByMid(driver, values[0])
            }
}



