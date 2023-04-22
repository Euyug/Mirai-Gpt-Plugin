package org.gptbot

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import java.io.File

class ChatBot{
    object BotBasic {
        val bot = Bot.instances.first()//机器人
        val folder_preset = File("./config/Chatbot/preset")//预设
        const val path_home = "./config/Chatbot"
        const val path_preset = "./config/Chatbot/preset"
        const val path_Apikey = "./config/Chatbot/key.txt"
        var apikey = File(path_Apikey).readText()//你的apikey
        val tokenPattern = "sk-(?:[A-Za-z0-9+]{4})*(?:[A-Za-z0-9+]{2}==|[A-Za-z0-9+]{3}=)?".toRegex()

        @JvmStatic
        fun chat() = runBlocking {
            bot.getFriend(File("$path_home/master.txt").readText().toLong())?.sendMessage("ChatGpt已上线")
            Botlistenner()
        }
    }
}

fun isMaster(id : Long) : Boolean{
    return (id == File("${ChatBot.BotBasic.path_home}/master.txt").readText().toLong())
}





