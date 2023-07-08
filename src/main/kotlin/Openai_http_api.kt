package org.gptbot

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.gptbot.ChatBot.BotBasic.apikey
import org.gptbot.ChatBot.BotBasic.folder_preset
import org.gptbot.ChatBot.BotBasic.path_preset
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit


fun openaiChatApi(content: String, ID: Long, name: String = ""): String {
    val path = if (name.isEmpty()) path_preset else "$path_preset/group"

    //配置文件夹
    val folderId = File("$path/$ID")

    //每个qq的聊天记录
    val messageFile = File(folderId,"message.json")

    //每个qq的设置文件
//        {
//            "type":"default",
//            "group":"",
//            "private":false
//        }

    val setJson = JSONObject(File(folderId,"set.json").readText())
    var setType = setJson.getString("type")//type为每个qq对应的预设文件名
    if (!File(path_preset,"$setType.json").exists()) setType = "default"
    val chat = setJson.getBoolean("chat")

    //请求体中除了message以外的其他参数
    val parameters = JSONObject(File("./config/Chatbot/parameters.json").readText())
    val proxyAddress = parameters.getString("Reverse-proxy")//代理地址
    parameters.remove("Reverse-proxy")//这个参数不参与gpt的api

    //先读取最终请求体的messages的最前面的预设提示
    val requestMessage = JSONArray(File(folder_preset,"$setType.json").readText())

    //requestMessage变量为最终请求体里的message，这里先读入预设中的值,message为对话内容，最后写入到message.json文件里
    var message = JSONArray()

    //读入存在message.json中存的旧消息，并合并到变量message中
    if (messageFile.exists() && chat) message = JSONArray(messageFile.readText())
    /* 叠加新消息 */
    val newMessage = JSONObject().put("role","user").put("content",content)
    if (name.isNotEmpty()) newMessage.put("name",name.substring(0, 6))
    message = message.put(newMessage)

    //请求体里的message添加聊天内容
    for (i in 0 until message.length()) requestMessage.put(message.getJSONObject(i))

//建立请求体—————————————————————————————————————————————————————————————————————————————————————————————————————————————
    //请求体json内更改新的message：JsonArray
    val jsonMediaType = "application/json;charset=utf-8".toMediaType()
    val requestBody = parameters.put("messages",requestMessage).toString().toRequestBody(jsonMediaType)
    val client = OkHttpClient().newBuilder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    //大致格式
//        val requestBody = """
//        {
//            "model": "${parameter.model}",
//            "temperature": ${parameter.temperature},
//            "messages": [{"role": "user", "content": "$content"}],
//            "frequency_penalty": ${parameter.frequency_penalty},
//            "presence_penalty": ${parameter.presence_penalty},
//            "max_tokens": ${parameter.max_tokens}
//        }
//    """.trimIndent().toRequestBody(jsonMediaType)
    val request = Request.Builder()
        .url("https://$proxyAddress/v1/chat/completions")
        .addHeader("Authorization", "Bearer $apikey")
        .post(requestBody)
        .build()
//——————————————————————————————————————————————————————————————————————————————————————————————————————————————————————

//返回值为回复内容
    return try {
        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()
        try {
            val s = responseBodyMessage(JSONObject(responseBody))
            //保存聊天记录,并根据长度修改防止过长
            if (chat) {
                message = message.put(s)
                while (message.toString().length > 2000) message = JSONArray(message.drop(2))
                messageFile.writeText(message.toString())
            }

            s.getString("content")
        } catch (e: JSONException) {
            val gptError = responseBodyError(JSONObject(responseBody))
            "失败 错误信息：${gptError.getString("message")}"
        }//解析json
    } catch (e: SocketTimeoutException) {
        "请求超时，请检查网络状态 或重新提问、换个问题。"
    }
}