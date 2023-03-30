package org.chatbot

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.chatbot.ChatBot.BotBasic.preset_path
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit


object botfun {
    fun GetChatMsg(content : String,formid : Long) : String {
        var type = "default"
        val path = "$preset_path/$formid"
        if (File("$path/set.txt").exists()){
            if(File("$preset_path/${File("$path/set.txt").readText()}.json").exists())
                type = File("$path/set.txt").readText()
        }
        //preset为预设请求体 格式如下
        //        {
        //            "model": "gpt-3.5-turbo",
        //            "temperature": 0.8,
        //            "messages": [{"role": "system", "content":"The following is a conversation with an AI assistant. The assistant is helpful, creative, clever, and very friendly."}],
        //            "frequency_penalty": 0,
        //            "presence_penalty": 0,
        //            "max_tokens": 2048
        //        }

        val preset = JSONObject(File("$preset_path/$type.json").readText().trimIndent())

        //message变量 为最终请求体里的message，这里先读入预设中的值
        var request_message = preset.getJSONArray("messages")
        var message = JSONArray()

        //读入存在message.json中存的久消息，并合并到变量message中
        if(File("$preset_path/$formid/message.json").exists()) {
            val old_message = JSONArray(File("$preset_path/$formid/message.json").readText())
            message = old_message
//            for (i in 0 until old_message.length()){
//                message.put(old_message.getJSONObject(i))
//            }
        }

        //叠加新消息
        val new_message = JSONObject("""{"role":"user","content":"${content.trimIndent()}"}""")
        message = message.put(new_message)
        for(i in 0 until message.length()) request_message.put(message.getJSONObject(i))

        val jsonMediaType = "application/json; charset=utf-8".toMediaType()
        //请求体json内更改新的message（type：JsonArray）
        val requestBody = preset.put("messages",request_message).toString().toRequestBody(jsonMediaType)
        val client = OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()

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
            .url("https://openai.geekr.cool/v1/chat/completions")
            .addHeader("Authorization", "Bearer ${ChatBot.BotBasic.apikey}")
            .post(requestBody)
            .build()
        return try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            try {
//                val s = GetgptContent(JSONObject(responseBody))
                val s = GetjsonObj_message(JSONObject(responseBody))
                message = message.put(s)
                File("$preset_path/$formid/message.json").writeText(message.toString())
                s.getString("content")
            } catch (e: JSONException){
                val gpterror = GetgptError(JSONObject(responseBody))
                "失败 错误信息：${gpterror.getString("message")}"
            }//解析json
        } catch (e: SocketTimeoutException) {
            "请求超时，请检查网络状态 或重新提问、换个问题。"
        }
    }
}

//
//object botfun {
//    fun GetChatMsg(content : String,formid : Long) : String {
//        var type = "default"
//        val path = "$preset_path/$formid"
//        if (File("$path/set.txt").exists()){
//            if(File("$preset_path/${File("$path/set.txt").readText()}").exists())
//                type = File("$path/set.txt").readText()
//        }
//        val parameter = GptParameter()
//        val client = OkHttpClient().newBuilder()
//            .connectTimeout(30, TimeUnit.SECONDS)
//            .readTimeout(30, TimeUnit.SECONDS)
//            .writeTimeout(30, TimeUnit.SECONDS)
//            .retryOnConnectionFailure(true)
//            .build()
//        val jsonMediaType = "application/json; charset=utf-8".toMediaType()
//
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
//
//        val request = Request.Builder()
//            .url(parameter.url)
//            .addHeader("Authorization", "Bearer ${ChatBot.BotBasic.apikey}")
//            .post(requestBody)
//            .build()
//        return try {
//            val response = client.newCall(request).execute()
//            val responseBody = response.body?.string()
//            try {
//                val new_message = GetgptContent(JSONObject(responseBody))
//                new_message
//            } catch (e: JSONException){
//                val gpterror = GetgptError(JSONObject(responseBody))
//                "请查看问题格式，注意不要有换行\n错误信息：${gpterror.getString("message")}"
//            }//解析json
//        } catch (e: SocketTimeoutException) {
//            "请求超时，请重新提问或者换个问题。或检查网络状态"
//        }
//    }
//}

