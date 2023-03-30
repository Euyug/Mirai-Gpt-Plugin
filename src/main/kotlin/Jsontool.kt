package org.gptbot

import org.json.JSONObject
import java.io.File

fun rspsbdMessage(obj : JSONObject) : JSONObject {
    return obj.getJSONArray("choices")
        .getJSONObject(0)
        .getJSONObject("message")
}

fun ismaster(qqid : Long) : Boolean{
    return (qqid == File("${ChatBot.BotBasic.path_home}/master.txt").readText().toLong())
}

fun rspsbdError(obj : JSONObject) : JSONObject {
    return obj.getJSONObject("error")
}
