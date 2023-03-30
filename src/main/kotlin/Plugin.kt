package org.gptbot

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.events.BotOnlineEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.utils.info


object Plugin : KotlinPlugin(
    JvmPluginDescription(
        id = "org.gptbot.Plugin",
        version = "0.1",
    )
) {

    override fun onEnable() {

        logger.info{"\n  ____ _   _    _  _____     ____ ____ _____ \n" +
                " / ___| | | |  / \\|_   _|   / ___|  _ \\_   _|\n" +
                "| |   | |_| | / _ \\ | |    | |  _| |_) || |  \n" +
                "| |___|  _  |/ ___ \\| |    | |_| |  __/ | |  \n" +
                " \\____|_| |_/_/   \\_\\_|     \\____|_|    |_|  \n" +
                "\nchatbot Plugin loaded"}
        First_init()

        this.globalEventChannel().subscribeOnce<BotOnlineEvent> {
            ChatBot.BotBasic.chat()
        }

    }
}


