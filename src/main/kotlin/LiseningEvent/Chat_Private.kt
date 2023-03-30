package org.gptbot.LiseningEvent

import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.MessageSourceKind
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.message.data.kind
import net.mamoe.mirai.message.data.source
import org.gptbot.ChatBot.BotBasic.bot
import org.gptbot.ChatBot.BotBasic.path_preset
import org.gptbot.Get_Chat_Content
import org.gptbot.InitChatCfg
import java.io.File

fun chatPrivate (key : String) {
    bot.eventChannel.subscribeMessages {
//        val keyChat = "@${ChatBot.BotBasic.bot.id}"
        startsWith(key) quoteReply {
            val getmsg = message.content.substring(key.length)
            val id = message.source.fromId

            //创建给定qq的预设文件,id为文件夹名字可以是qqID可以是groupID
            InitChatCfg(id,private = true)
//            if (apikey == "Your apiKey") {
//                "你没有配置apikey\n请输入/key~你的key"
//            } else {
//                if (apikey.matches(tokenPattern)) {
//                    Openai_http_api(getmsg, ID = ID,role = role)
//                } else {
//                    "格式错误"
//                }
//            }
            Get_Chat_Content(getmsg,id)
        }

        "结束对话" reply {
            val path: String = if (message.source.kind == MessageSourceKind.GROUP) "$path_preset/group" else path_preset
            val id = if (message.source.kind == MessageSourceKind.GROUP) message.source.targetId else message.source.fromId
            if (File("$path/$id/message.json").exists()){
                File("$path/$id/message.json").delete()
                "已结束"
            }else{
                "你并没有开始对话"
            }
        }
    }
}

