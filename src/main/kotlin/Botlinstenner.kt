package org.gptbot

import org.gptbot.ChatBot.BotBasic.bot
import org.gptbot.LiseningEvent.*


fun Botlistenner(){
    presetNew("添加预设")

    chatPrivate("@${bot.id}")

    keySet("/key~")

    presetSet("预设")

    presetDelete("删除预设")

    chatGroup("#")

    parameterSet("参数设置")
}

//fun GetChatMsg(content : String, ID : Long, role : String= "user") : String {
//    val folder_fromid = File("$path_preset/$ID")
//
//    //每个qq的聊天记录
//    val message_file = File(folder_fromid,"message.json")
//
//    //每个qq的设置文件
////        {
////            "type":"default",
////            "group":"",
////            "private":false
////        }
//    val set_json = JSONObject(File(folder_fromid,"set.json").readText())
//    val set_type = set_json.getString("type")//type为每个qq对应的预设文件名
//
//    //请求体中除了message以外的其他参数
//    val parameters = JSONObject(File("./config/Chatbot/parameters.json").readText())
//
//    //最终请求体的messages的最前面的预设提示
//    val preset = JSONArray(File(folder_preset,"$set_type.json").readText())
//
//    //request_message变量为最终请求体里的message，这里先读入预设中的值,message为对话内容，最后写入到message.json文件里
//    var message = JSONArray()
//    val request_message = preset
//
//    //读入存在message.json中存的旧消息，并合并到变量message中
//    if(message_file.exists()) message = JSONArray(message_file.readText())
//    /* 叠加新消息 */
//    val new_message = JSONObject().put("role",role).put("content",content)
//    message = message.put(new_message)
//
//    //请求体里的message添加聊天内容
//    for(i in 0 until message.length()) request_message.put(message.getJSONObject(i))
//
////建立请求体—————————————————————————————————————————————————————————————————————————————————————————————————————————————
//    //请求体json内更改新的message：JsonArray
//    val jsonMediaType = "application/json; charset=utf-8".toMediaType()
//    val requestBody = parameters.put("messages",request_message).toString().toRequestBody(jsonMediaType)
//    val client = OkHttpClient().newBuilder()
//        .connectTimeout(60, TimeUnit.SECONDS)
//        .readTimeout(60, TimeUnit.SECONDS)
//        .writeTimeout(60, TimeUnit.SECONDS)
//        .retryOnConnectionFailure(true)
//        .build()
//
//    //大致格式
////        val requestBody = """
////        {
////            "model": "${parameter.model}",
////            "temperature": ${parameter.temperature},
////            "messages": [{"role": "user", "content": "$content"}],
////            "frequency_penalty": ${parameter.frequency_penalty},
////            "presence_penalty": ${parameter.presence_penalty},
////            "max_tokens": ${parameter.max_tokens}
////        }
////    """.trimIndent().toRequestBody(jsonMediaType)
//
//    val request = Request.Builder()
//        .url("https://openai.geekr.cool/v1/chat/completions")
//        .addHeader("Authorization", "Bearer $apikey")
//        .post(requestBody)
//        .build()
////——————————————————————————————————————————————————————————————————————————————————————————————————————————————————————
//
////返回值为回复内容
//    return try {
//        val response = client.newCall(request).execute()
//        val responseBody = response.body?.string()
//        try {
//            val s = rspsbdMessage(JSONObject(responseBody))
//            //保存聊天记录,并根据长度修改防止过长
//            message = message.put(s)
//            while(message.toString().length > 2500) message = JSONArray(message.drop(2))
//            message_file.writeText(message.toString())
//
//
//            s.getString("content")
//        } catch (e: JSONException){
//            val gpterror = rspsbdError(JSONObject(responseBody))
//            "失败 错误信息：${gpterror.getString("message")}"
//        }//解析json
//    } catch (e: SocketTimeoutException) {
//        "请求超时，请检查网络状态 或重新提问、换个问题。"
//    }
//}
