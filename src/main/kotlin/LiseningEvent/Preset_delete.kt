package org.gptbot.LiseningEvent

import net.mamoe.mirai.event.selectMessages
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.PlainText
import org.gptbot.ChatBot
import java.io.File

fun presetDelete(key : String) {
    ChatBot.BotBasic.bot.eventChannel.subscribeMessages {
        "$key" listen@{
            subject.sendMessage("输入你想要的删除的预设名字")
            subject.sendMessage("已经存在预设：${allpreset()}")
            val preset_delet = selectMessages {
                has<PlainText>{ it.toString() }
                defaultReply { "请输入要删除的预设名" }
                timeout(30_000) { subject.sendMessage("请在 30 秒内发送名字"); "" }
            }
            if (preset_delet.isEmpty()) return@listen
            val preset_file = File(ChatBot.BotBasic.path_preset,"$preset_delet.json")
            if (preset_file.exists()){
                preset_file.delete()
                subject.sendMessage("删除预设“$preset_delet”成功")
            }else{
                subject.sendMessage("没有这个预设")
            }
        }
    }
}

