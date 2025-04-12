# bili_auto_tool b站自动化工具

## 项目简介
该项目是一个使用 Selenium 和 Kotlin 编写的自动化工具，用于从 Bilibili 导出用户的关注列表和粉丝列表，并可以根据导出的 CSV 文件进行批量关注操作。

## 功能
1. **导出关注列表**：根据用户提供的 `mid`（用户ID），导出该用户的关注列表到 CSV 文件。 ![Export Followings](https://img.shields.io/badge/Export%20Followings-blue)
2. **导出粉丝列表**：根据用户提供的 `mid`（用户ID），导出该用户的粉丝列表到 CSV 文件。 ![Export Fans](https://img.shields.io/badge/Export%20Fans-blue)
3. **批量关注**：根据 CSV 文件中的用户ID列表，自动关注这些用户。 ![Batch Follow](https://img.shields.io/badge/Batch%20Follow-blue)

## 环境准备
1. **安装 Java JDK**：确保已安装 Java JDK 11 或更高版本。 ![Java](https://img.shields.io/badge/Java-11%2B-blue)
2. **安装 Chrome 浏览器**：确保已安装 Chrome 浏览器。 ![Chrome](https://img.shields.io/badge/Chrome-latest-blue)
3. **安装 ChromeDriver**：确保已安装 ChromeDriver，并将其路径添加到系统环境变量中。 ![ChromeDriver](https://img.shields.io/badge/ChromeDriver-latest-blue)

## 项目依赖
- `org.seleniumhq.selenium:selenium-java` ![Selenium](https://img.shields.io/badge/Selenium-latest-blue)
- `com.google.code.gson:gson` ![Gson](https://img.shields.io/badge/Gson-latest-blue)
- `org.jetbrains.kotlinx:kotlinx-serialization-json` ![Kotlinx Serialization](https://img.shields.io/badge/Kotlinx%20Serialization-latest-blue)

## 使用方法
1. **克隆项目**