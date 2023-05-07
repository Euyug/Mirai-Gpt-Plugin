package org.gptbot.LiseningEvent

import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.*
import org.gptbot.ChatBot
import org.gptbot.ChatBot.BotBasic.folder_preset
import org.gptbot.ChatBot.BotBasic.path_preset
import org.gptbot.InitChatCfg
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

fun presetSet(key : String){
    ChatBot.BotBasic.bot.eventChannel.subscribeMessages {

        startsWith(key)reply {
            val getmsg = message.content.substring(key.length).replace("\n","").replace(" ","")
            var id : Long = message.source.fromId
            var private = true
            var path = path_preset
            val c = if(message.source.kind == MessageSourceKind.GROUP){
                id = message.source.targetId
                private = false
                path = "$path/group"
                "群聊"
            }else{
                "QQ"
            }

            InitChatCfg(id,private)
            val file_Set = File("$path/$id/set.json")
            val set = JSONObject(file_Set.readText().trimIndent())
            val file_preset = File(folder_preset,"$getmsg.json")
            //创建给定qq的预设文件
            if(file_preset.exists()){
                //修改当前qq的设置文件
                set.put("type",getmsg)
                file_Set.writeText(set.toString())

                try{
                    //删除聊天记录，用新的预设开启对话
                    val fileMessage = File("$path/$id/message.json")
                    if (fileMessage.exists()) fileMessage.delete()

                    val msgArray = JSONArray(file_preset.readText().trimIndent())
                    subject.sendMessage("${c}${id}预设\"${getmsg}\"成功")
                    msgArray.getJSONObject(msgArray.length()-1).getString("content")
                }catch (e: Exception){
                    "设置失败，检查预设格式"
                }
            }else{
                "设置失败，没有这个预设\n请输入“全部预设”查看全部预设"
            }
        }

        "全部预设" reply {
            allpreset()
        }

    }
}

fun allpreset() = run {
    val Files_json = folder_preset.listFiles {
            file -> file.isFile
            && file.name.endsWith(".json")
    }
    var msg = ""
    Files_json.forEach { file ->
        msg += file.nameWithoutExtension + ",  "
    }
    msg.dropLast(3)
}
