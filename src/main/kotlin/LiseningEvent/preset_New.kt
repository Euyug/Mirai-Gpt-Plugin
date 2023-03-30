package org.gptbot.LiseningEvent

import net.mamoe.mirai.event.selectMessages
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.PlainText
import org.gptbot.ChatBot.BotBasic.bot
import org.gptbot.ChatBot.BotBasic.folder_preset
import org.json.JSONArray
import org.json.JSONException
import java.io.File

fun presetNew (key : String){
    bot.eventChannel.subscribeMessages {
        "$key" listen@{
            subject.sendMessage("请发送json文本 格式如下:")
            subject.sendMessage(File(folder_preset,"格式").readText()).recallIn(15_000)
            val preset_json : JSONArray = selectMessages {
                has<PlainText>{
                    try {
                        JSONArray(it.content.replace("\n",""))
                    }catch (e: JSONException){
                        subject.sendMessage(e.message.toString())
                        subject.sendMessage("你的内容是" + it.content)
                        subject.sendMessage("请按格式，建议复制模板然后修改里面的内容")
                        JSONArray()
                    }
                }
                defaultReply { "请发送请json文本" }
                timeout(300_000) { subject.sendMessage("请在 300 秒内发送json文本"); JSONArray() }
            }
            if (preset_json.isEmpty)return@listen
            subject.sendMessage("已经获取到json文本 请输入名字")

            val preset_name : String = selectMessages {
                has<PlainText>{ it.toString() }
                defaultReply { "请输入名字" }
                timeout(30_000) { subject.sendMessage("请在 30 秒内发送json文本"); "" }
            }
            if (preset_name.isEmpty())return@listen

            val preset_file = File(folder_preset,"$preset_name.json")
            if (preset_file.exists()){
                val preset_bac = File(folder_preset,"$preset_name.json.bac")
                preset_file.renameTo(preset_bac)
                subject.sendMessage("已存预设“$preset_name”,已将原预设文件添加后缀.bac")
            }
            preset_file.writeText(preset_json.toString())
            subject.sendMessage("已经成功添加预设：$preset_name")
        }
    }
}