package org.gptbot.LiseningEvent

import net.mamoe.mirai.event.selectMessages
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.PlainText
import org.gptbot.ChatBot.BotBasic.bot
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

//"Reverse-proxy"="https://api.openai-proxy.com/v1/chat/completions"

fun Proxy_set(key : String){
    bot.eventChannel.subscribeMessages {

        val parameters = JSONObject(File("./config/Chatbot/parameters.json").readText())

        key  listen@ {
            subject.sendMessage("请输入openai反向代理服务器地址（域名）")
            val address : String =  selectMessages {
                has<PlainText>{
                    val text = "https://${it.content}"
                    if (isUrl(text)){
                         if (canAccessUrl(text)){
                             it.content
                         }else {
                             subject.sendMessage("无法访问")
                             ""
                         }
                    }else {
                        subject.sendMessage("不是正确的url")
                        ""
                    }
                }
                defaultReply { "请发送正确的反向代理服务器地址" }
                timeout(30_000) { subject.sendMessage("请在 30 秒内发送").recallIn(30_000); "" }
            }
            if(address.isEmpty())return@listen
            parameters.put("Reverse-proxy",address)
            File("./config/Chatbot/parameters.json").writeText(parameters.toString())
            subject.sendMessage("配置成功\n" +
                    "代理服务器地址为：$address")
        }

       "${key}重置" reply {
            parameters.put("Reverse-proxy","api.openai-proxy.com")
            File("./config/Chatbot/parameters.json").writeText(parameters.toString())

            "已重置为api.openai-proxy.com"
        }
    }
}

fun isUrl(text: String): Boolean {
    return try {
        URL(text).toURI()
        true
    } catch (e: Exception) {
        false
    }
}

fun canAccessUrl(text: String): Boolean {
    return try {
        val url = URL(text)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        val responseCode = connection.responseCode
        responseCode == HttpURLConnection.HTTP_OK
    } catch (e: Exception) {
        false
    }
}