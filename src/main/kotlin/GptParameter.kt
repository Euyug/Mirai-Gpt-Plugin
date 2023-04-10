package org.chatbot

import kotlinx.serialization.Serializable

@Serializable
data class GptParameter(
    val model: String = "gpt-3.5-turbo",
    val temperature: Double = 0.8,
    val frequency_penalty: Double = 0.5,
    val presence_penalty: Double = 0.5,
    val max_tokens: Int = 2048,
    val url: String = "https://openai.geekr.cool/v1/chat/completions"//如果没有梯子可以尝试开源的反向代理地址openai.geekr.cool
)