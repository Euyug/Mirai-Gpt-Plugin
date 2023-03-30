package org.chatbot

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

import net.mamoe.mirai.message.data.content
import org.chatbot.ChatBot.BotBasic.apikey
import org.chatbot.ChatBot.BotBasic.bot
import org.chatbot.ChatBot.BotBasic.keypath
import org.chatbot.ChatBot.BotBasic.tokenPattern

class ChatBot{
    object BotBasic {
        val bot = Bot.instances.first()
        val keypath = "./config/Chatbot/key.txt"
        var apikey = File(keypath).readText()
        val tokenPattern = "sk-(?:[A-Za-z0-9+]{4})*(?:[A-Za-z0-9+]{2}==|[A-Za-z0-9+]{3}=)?".toRegex()

        @JvmStatic
        fun chat() = runBlocking {

//            val bot = MiraiConsole.addBot(2835157330, "xsy010711") {
//                fileBasedDeviceInfo(filepath = "./config/Chatbot/device.json") // 使用 device.json 存储设备信息
//                protocol = BotConfiguration.MiraiProtocol.MACOS// 切换协议
//            }.alsoLogin()
            bot.getFriend(File("./config/Chatbot/master.txt").readText().toLong())?.sendMessage("chatgpt已上线")
            botevent()
        }
    }
}


fun botevent(){
    bot.eventChannel.subscribeMessages {
        val key_chat = "@${bot.id}"
        val key_set = "/key~"

        startsWith(key_chat) reply {
            val getmsg = message.content.substring(key_chat.length)
            if (apikey == "Your apiKey") {
                "你没有配置apikey\n请输入/key~你的key"
            } else {
                if(apikey.matches(tokenPattern)){
                    val msg = GetChatMsg(getmsg)
                    message.quote() + msg
                }else{
                    "格式错误"
                }
            }
        }

        startsWith(key_set) reply {
            val getmsg = message.content.substring(key_set.length)
            if (!getmsg.matches(tokenPattern)) {
                "格式错误重新配置"
            }else {
                File(keypath).writeText(getmsg)
                apikey = File(keypath).readText()
                "配置成功"
            }
        }
    }
}



//对接openai的api——————————————————————————————————————————————————————————————————————————————————————————————————————
fun GetChatMsg(prompt : String) : String {

    val parameter = GptParameter()
    val client = OkHttpClient().newBuilder()
        .connectTimeout(parameter.Connect_Timeout, TimeUnit.SECONDS)
        .readTimeout(parameter.Read_timeout, TimeUnit.SECONDS)
        .writeTimeout(parameter.Write_Timeout, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    val jsonMediaType = "application/json; charset=utf-8".toMediaType()
    val requestBody = """
        {
            "model": "${parameter.model}",
            "temperature": ${parameter.temperature},
            "messages": [{"role": "user", "content": "$prompt"}],
            "frequency_penalty": ${parameter.frequency_penalty},
            "presence_penalty": ${parameter.presence_penalty},
            "max_tokens": ${parameter.max_tokens}
        }
    """.trimIndent().toRequestBody(jsonMediaType)

    val request = Request.Builder()
        .url(parameter.url)
        .addHeader("Authorization", "Bearer $apikey")
        .post(requestBody)
        .build()

    return try {
        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()
        try {
            GetgptContent(JSONObject(responseBody))
        } catch (e: JSONException){
            val gpterror = GetgptError(JSONObject(responseBody))
            "请查看问题格式，注意不要有换行\n错误类型：${gpterror.getString("type")}\ncode:${gpterror.getString("code")}"
        }//解析json
    } catch (e: SocketTimeoutException) {
        "请求超时，请重新提问或者换个问题。或检查网络状态"
    }
}

//解析返回的json
fun GetgptContent(obj : JSONObject) : String {
    return obj.getJSONArray("choices")
        .getJSONObject(0)
        .getJSONObject("message")
        .getString("content")
}
fun GetgptError(obj : JSONObject) : JSONObject {
    return obj.getJSONObject("error")
}

//——————————————————————————————————————————————————————————————————————————————————————————————————————


