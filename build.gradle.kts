plugins {
    val kotlinVersion = "1.5.30"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("net.mamoe.mirai-console") version "2.9.2"
}

group = "org.gptbot"
version = "1.2"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}

dependencies{
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation ("org.json:json:20210307")
}
