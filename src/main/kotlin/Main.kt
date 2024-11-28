package org.coderpwh


import kotlinx.serialization.json.Json
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.devtools.NetworkInterceptor
import org.openqa.selenium.remote.http.Filter
import org.openqa.selenium.remote.http.HttpHandler
import pojo.ListItem
import pojo.MyData
import java.io.BufferedWriter
import java.io.File
import java.io.OutputStreamWriter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


fun main() {
    val options = ChromeOptions().apply {
        setExperimentalOption("debuggerAddress","localhost:9222")
    }
    val chromeDriver = ChromeDriver(options)
    println(chromeDriver.title)
    NetworkInterceptor(chromeDriver, Filter { next -> HttpHandler {
        req ->
        val execute = next.execute(req)
//        println(req.uri)
        if (req.uri.contains("followings")) {
            val jsonStr = execute.content.get().bufferedReader(Charsets.UTF_8).readLine()
            val data = Json {
                ignoreUnknownKeys = true
            }.decodeFromString<MyData>(jsonStr)
//            println(data)
            exportToCsv("result.csv",listOf("id","用户名","关注时间","用户签名","头像"),data.data!!.list)
            val r = data.data!!.list.map { it.uname }.joinToString("\n")
            println(r)
            println(execute.content.get().bufferedReader(Charsets.UTF_8).readLine())
        }
        execute
    }
     })
//    chromeDriver.get("https://space.bilibili.com/437966767/fans/follow?tagid=-1")
    chromeDriver.navigate().refresh()
    Thread.sleep(1000L)

}

fun exportToCsv(fileName: String, headers: List<String>, data: List<ListItem>) {
    // 使用 UTF-8 编码来写入文件
    val writer = BufferedWriter(OutputStreamWriter(File(fileName).outputStream(), "utf-8"))

    writer.use {
        // 写入CSV的头部
        it.write(headers.joinToString(","))
        it.newLine()

        // 写入每行数据
        data.forEach { item ->
            // 将每个字段值拼接成 CSV 行
            it.write("${item.mid},${item.uname},${convertTimestampToDateTime(item.mtime!!.toLong())},${item.sign?.replace("\n","")},${item.face}")
            it.newLine()
        }
    }
}

fun convertTimestampToDateTime(timestamp: Long): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault())
}




