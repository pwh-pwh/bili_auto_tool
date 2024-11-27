package org.coderpwh

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.devtools.NetworkInterceptor
import org.openqa.selenium.remote.http.Filter
import org.openqa.selenium.remote.http.HttpHandler


fun main() {
    val options = ChromeOptions().apply {
        setExperimentalOption("debuggerAddress","localhost:9222")
    }
    val chromeDriver = ChromeDriver(options)
    println(chromeDriver.title)
    NetworkInterceptor(chromeDriver, Filter { next -> HttpHandler {
        req ->
        val execute = next.execute(req)
        println(req.uri)
        if (req.uri.contains("followings")) {
            println(execute.content.get().bufferedReader(Charsets.UTF_8).readLine())
        }
        execute
    }
     })
    chromeDriver.get("https://space.bilibili.com/437966767/fans/follow?tagid=-1")
    Thread.sleep(1000L)

}


