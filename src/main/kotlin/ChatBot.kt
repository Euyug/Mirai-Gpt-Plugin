package org.chatbot

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.message.data.source
import org.chatbot.ChatBot.BotBasic.apikey
import org.chatbot.ChatBot.BotBasic.bot
import org.chatbot.ChatBot.BotBasic.Apikey_path
import org.chatbot.ChatBot.BotBasic.preset_path
import org.chatbot.ChatBot.BotBasic.tokenPattern
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class ChatBot{
    object BotBasic {
        val preset_path = "./config/Chatbot/preset"
        val bot = Bot.instances.first()
        val Apikey_path = "./config/Chatbot/key.txt"
        var apikey = File(Apikey_path).readText()
        val tokenPattern = "sk-(?:[A-Za-z0-9+]{4})*(?:[A-Za-z0-9+]{2}==|[A-Za-z0-9+]{3}=)?".toRegex()

        @JvmStatic
        fun chat() = runBlocking {
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
                    val formid = message.source.fromId
                    val msg = botfun.GetChatMsg(getmsg,formid)
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
                File(Apikey_path).writeText(getmsg)
                apikey = File(Apikey_path).readText()
                "配置成功"
            }
        }

        startsWith("/预设")reply {
            val getmsg = message.content.substring("/预设".length)
            val fromid : Long = message.source.fromId
            if(File("$preset_path/$getmsg.json").exists()){
                if(!File("$preset_path/","$fromid").exists()) File("$preset_path/","$fromid").mkdir()
                File("$preset_path/$fromid/set.txt").writeText(getmsg)
                try{
                    val formid = message.source.fromId
                    if (File("$preset_path/$formid/message.json").exists())
                        File("$preset_path/$formid/message.json").delete()

                    val msg_arry = JSONObject(File("$preset_path/$getmsg.json").readText()).getJSONArray("messages")
                    msg_arry.getJSONObject(msg_arry.length()-1).getString("content")
                }catch (e:JSONException){
                    "设置成功，当前预设为: $getmsg"
                }
            }else{
                "设置失败，没有这个预设"
            }
        }

        "结束对话" reply {
            val formid = message.source.fromId
            if (File("$preset_path/$formid/message.json").exists()){
                File("$preset_path/$formid/message.json").delete()
                "已结束"
            }else{
                "你并没有开始对话"
            }
        }

        "预设列表" reply {
            val folder = File(preset_path)
            val jsonFiles = folder.listFiles { file -> file.isFile && file.name.endsWith(".json") }
            var msg = ""
            jsonFiles.forEach { file ->
                msg += file.nameWithoutExtension + ","
            }
            msg
        }
    }
}



//对接openai的api——————————————————————————————————————————————————————————————————————————————————————————————————————
//fun GetChatMsg(content : String) : String {
//
//    val parameter = GptParameter()
//    val client = OkHttpClient().newBuilder()
//        .connectTimeout(30, TimeUnit.SECONDS)
//        .readTimeout(30, TimeUnit.SECONDS)
//        .writeTimeout(30, TimeUnit.SECONDS)
//        .retryOnConnectionFailure(true)
//        .build()
//
//    val jsonMediaType = "application/json; charset=utf-8".toMediaType()
//
//    val requestBody = """
//        {
//            "model": "${parameter.model}",
//            "temperature": ${parameter.temperature},
//            "messages": [{"role": "user", "content": "$content"}],
//            "frequency_penalty": ${parameter.frequency_penalty},
//            "presence_penalty": ${parameter.presence_penalty},
//            "max_tokens": ${parameter.max_tokens}
//        }
//    """.trimIndent().toRequestBody(jsonMediaType)
//
//    val request = Request.Builder()
//        .url(parameter.url)
//        .addHeader("Authorization", "Bearer $apikey")
//        .post(requestBody)
//        .build()
//    return try {
//        val response = client.newCall(request).execute()
//        val responseBody = response.body?.string()
//        try {
//            val new_message = GetgptContent(JSONObject(responseBody))
//            new_message
//        } catch (e: JSONException){
//            val gpterror = GetgptError(JSONObject(responseBody))
//            "请查看问题格式，注意不要有换行\n错误信息：${gpterror.getString("message")}"
//        }//解析json
//    } catch (e: SocketTimeoutException) {
//        "请求超时，请重新提问或者换个问题。或检查网络状态"
//    }
//}







//——————————————————————————————————————————————————————————————————————————————————————————————————————


