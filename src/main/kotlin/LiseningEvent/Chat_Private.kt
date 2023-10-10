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
            val tempmsg = getmsg.replace(" ","")
            if (tempmsg == "结束对话"){
                val path_p: String = path_preset
                val id_p = message.source.fromId
                if (File("$path_p/$id_p/message.json").exists()){
                    File("$path_p/$id_p/message.json").delete()
                    "已结束"
                }else{
                    "你并没有开始对话"
                }
            }else{
                // 创建给定qq的预设文件,id为文件夹名字可以是qqID可以是groupID
                InitChatCfg(id,private = true)
            //    if (apikey == "Your apiKey") {
            //        "你没有配置apikey\n请输入/key~你的key"
            //    } else {
            //        if (apikey.matches(tokenPattern)) {
            //            Openai_http_api(getmsg, ID = ID,role = role)
            //        } else {
            //            "格式错误"
            //        }
            //    }
                Get_Chat_Content(getmsg,id)
            }
        }

        "结束对话" reply {
            val path_g: String = "$path_preset/group"
            val id_g = message.source.targetId
            val path_p: String = path_preset
            val id_p = message.source.fromId
            if(message.source.kind == MessageSourceKind.GROUP)
            {
                if (File("$path_g/$id_g/message.json").exists()){
                    File("$path_g/$id_g/message.json").delete()
                    "已结束"
                }else{
                    "你并没有开始对话"
                }
            }
            else
            {
                if (File("$path_p/$id_p/message.json").exists()){
                    File("$path_p/$id_p/message.json").delete()
                    "已结束"
                }else{
                    "你并没有开始对话"
                }
            }
        }
    }
}

