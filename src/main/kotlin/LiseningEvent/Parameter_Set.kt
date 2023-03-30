package org.gptbot.LiseningEvent

import net.mamoe.mirai.event.subscribeMessages
import org.gptbot.ChatBot.BotBasic.bot

fun parameterSet(key : String){
    bot.eventChannel.subscribeMessages {
        "$key" listen@{

        }
    }
}