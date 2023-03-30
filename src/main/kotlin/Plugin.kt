package org.chatbot

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.events.BotOnlineEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.utils.info
import java.io.File

object Plugin : KotlinPlugin(
    JvmPluginDescription(
        id = "org.chatbot.Plugin",
        version = "0.1",
    )
) {

    override fun onEnable() {
        logger.info { "Plugin loaded" }
        InitConfig()

        globalEventChannel().subscribeOnce<BotOnlineEvent> {
            ChatBot.BotBasic.chat()
        }
    }
}

fun InitConfig(){
    val Folder_Chatbot = File("./config","Chatbot")
    if(!Folder_Chatbot.exists()) Folder_Chatbot.mkdir()

    val File_apikey = File("./config/Chatbot/key.txt")
    if (!File_apikey.exists()){
        File_apikey.createNewFile()
        File_apikey.writeText("Your apiKey")
    }

    val File_master = File("./config/Chatbot/master.txt")
    if (!File_master.exists()) {
        File_master.createNewFile()
        File_master.writeText("1223102221")
    }
}
