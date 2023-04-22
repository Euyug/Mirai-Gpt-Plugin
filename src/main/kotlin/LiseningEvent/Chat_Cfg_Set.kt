package org.gptbot.LiseningEvent

import kotlinx.serialization.json.JsonObject
import net.mamoe.mirai.event.selectMessages
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.*
import netscape.javascript.JSObject
import org.gptbot.ChatBot
import org.gptbot.ChatBot.BotBasic.path_preset
import org.gptbot.InitChatCfg
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File

fun chatCfgSet(key : String){
    ChatBot.BotBasic.bot.eventChannel.subscribeMessages {
        "$key" listen@{
            var path = if(message.source.kind == MessageSourceKind.GROUP) "$path_preset/group" else path_preset
            val private = path == path_preset
            val id = if (private) message.source.fromId else message.source.targetId
            path = "$path/$id"
            InitChatCfg(id, private)
//            val at : Message = if (private) At(id) else EmptyMessageChain
            subject.sendMessage("复制设置格式并修改\n格式如下：\n" + File(path,"set.json").readText()).recallIn(30_000)
            /*            {
                "type":"default",
                "chat":true
            }*/
            val setJson = selectMessages {
                has<PlainText>{
                    try {
                        JSONObject(it.content.replace("\n",""))
                    }catch (e: JSONException){
                        subject.sendMessage(e.message.toString()).recallIn(30_000)
                        subject.sendMessage("你的内容是" + it.content).recallIn(30_000)
                        subject.sendMessage("请按格式，建议复制模板然后修改里面的内容").recallIn(30_000)
                        JSONObject()
                    }
                }
                defaultReply { "请发送请json文本" }
                timeout(300_000) { subject.sendMessage("请在 300 秒内发送json文本").recallIn(30_000); JSONObject() }
            }
            if (setJson.isEmpty)return@listen
            File(path,"set.json").writeText(setJson.toString())
            subject.sendMessage("$id 的设置已修改成功 现在的设置为:").recallIn(30_000)
            subject.sendMessage("""
                聊天（记住对话内容）: ${setJson.getBoolean("chat")}
                类型（预设名字）: ${setJson.getString("type")}
            """.trimIndent())
        }
    }
}