package org.gptbot

import org.json.JSONObject
import java.io.File

fun responseBodyMessage(obj : JSONObject) : JSONObject {
    return obj.getJSONArray("choices")
        .getJSONObject(0)
        .getJSONObject("message")
}

fun responseBodyError(obj : JSONObject) : JSONObject {
    return obj.getJSONObject("error")
}
