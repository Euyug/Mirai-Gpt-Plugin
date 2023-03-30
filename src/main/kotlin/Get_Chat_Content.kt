package org.gptbot

fun Get_Chat_Content(
    msg: String,
    ID: Long,
    name: String = ""): String
{
    return if (ChatBot.BotBasic.apikey == "Your apiKey") {
        "你没有配置apikey\n请输入/key~你的key"
    } else {
        if (ChatBot.BotBasic.apikey.matches(ChatBot.BotBasic.tokenPattern)) {
            Openai_http_api(msg, ID = ID, name = name)
        } else {
            "格式错误"
        }
    }
}