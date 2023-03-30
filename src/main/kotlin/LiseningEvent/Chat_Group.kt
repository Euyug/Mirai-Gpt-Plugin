package org.gptbot.LiseningEvent

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.message.data.source
import org.gptbot.ChatBot.BotBasic.bot
import org.gptbot.Get_Chat_Content
import org.gptbot.InitChatCfg

fun chatGroup(key : String){
    bot.eventChannel.subscribeAlways<GroupMessageEvent> {
        message.content.replace("＃","#")
        if (message.content.startsWith(key)){
            val getmsg = message.content.substring(key.length)
            val id = message.source.targetId
            val name = message.source.fromId
            //创建给定qq的预设文件,id为文件夹名字可以是qqID可以是groupID
            InitChatCfg(id,private = false)
            subject.sendMessage(At(name) + Get_Chat_Content(getmsg,id,name.toString()))
        }
    }
}