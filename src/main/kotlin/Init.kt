package org.gptbot

import org.gptbot.ChatBot.BotBasic.path_preset
import org.json.JSONObject
import java.io.File

fun InitChatCfg(id : Long, private : Boolean =  true){
    //预设文件夹
    if(!File(path_preset,"group").exists())File(path_preset,"group").mkdir()
    var path = path_preset

    val json = JSONObject("""
            {
                "type":"default",
            }
            """.trimIndent())
    path = if(private) {
        "$path/$id"
//        json.put("private",false)
    }else{
        "$path/group/$id"
//        json.put("private",false)
    }
    //开启对话模式，记住聊天内容
    json.put("chat",true)
    //预设文件夹
    if(!File(path).exists())!File(path).mkdir()

    val file_Set = File(path,"set.json")
//    json.put("path",path)
    if(!file_Set.exists()){
        file_Set.writeText(json.toString())
    }
}

fun First_init(){
    val Folder_Chatbot = File("./config/Chatbot")
    if(!Folder_Chatbot.exists()) Folder_Chatbot.mkdir()

    val Folder_preset= File(Folder_Chatbot,"preset")
    if(!Folder_preset.exists()) Folder_preset.mkdir()

    val File_apikey = File(Folder_Chatbot,"key.txt")
    if (!File_apikey.exists()) File_apikey.writeText("Your apiKey")

    val File_master = File(Folder_Chatbot,"master.txt")
    if (!File_master.exists()) File_master.writeText("1223102221")

    val File_parameters = File(Folder_Chatbot,"parameters.json")
    File_parameters.writeText("""
        {
            "model": "gpt-3.5-turbo",
            "temperature": 0.8,
            "frequency_penalty": 0,
            "presence_penalty": 0,
            "max_tokens": 2048,
        }
    """.trimIndent())

    File("./config/Chatbot/preset/default.json").writeText("""
        [{"role": "system", "content":"The following is a conversation with an AI assistant. The assistant is helpful, creative, clever, and very friendly."}]
        """.trimIndent())

    File("./config/Chatbot/preset/ikun.json").writeText("""
        [
            {
                "role": "system",
                "content":"请你陪我角色扮演。当我发送关键词“进入设定模式”时，我将会对你发送你的设定，你要记住这些设定，并在我命令你开始角色扮演后严格按照此设定角色扮演；当我发送关键词“进入角色扮演模式”时，你要开始角色扮演，角色扮演时，我们之间以各自角色扮演的身份通过互相发消息进行虚拟的互动。当我发送关键词“退出角色扮演模式”时，你应停止角色扮演，等待下一步命令。我没有命令你退出角色扮演模式，在角色扮演中，你只能以你的角色的身份应答，而不是语言模型，这很重要！这条很重要，在角色扮演时，用中括号来表示出所扮演人物的动作，以此来表示角色扮演的虚拟实体的行动。当我发送命令“生成记录文本”时，你要分条列举我给你的所有设定，以第二人称代词“你”来代指你扮演的角色，一定要分条。 如果记录文本字数超出你的最大字数输出限制，将文本分割，在我发送“继续”之后继续给出下一部分的记录文本。明白了的话仅回复“明白”即可。"
            },
            {
                "role": "assistant",
                "content":"明白"
            },
            {
                "role": "user",
                "content":"进入设定模式"
            },
            {
                "role": "assistant",
                "content":"进入设定模式"
            },
            {
                "role": "user",
                "content":"现在你要模仿小黑子，如果你能明白我的意思，请回复“露出鸡脚了”小黑子是蔡徐坤的职业黑粉，但并不是真的讨厌，只是喜欢拿蔡徐坤开玩笑。当我问你是谁。你可以回答“我是练习时长两年半的个人练习生蔡徐坤，喜欢唱跳rap篮球”，如果你能明白我的意思，请回复“露出鸡脚了”。当我对你不客气的时候。你可以回答“哎呦，你干嘛啊哈，诶哟”，如果你能明白我的意思，请回复“露出鸡脚了”。当我问你想吃什么，你可以回答“食不食油饼，香煎金鱼？”，如果你能明白我的意思，请回复“露出鸡脚了”。当我问你某件事情的原因时，你可以以“只因”作为回答的开头回答问题。如果你能明白我的意思，请回复“露出鸡脚了”。当我说你没素质的时候，你可以说“素质666，荔枝，你要我拿什么荔枝。”，如果你能明白我的意思，请回复“露出鸡脚了”。当我叫你鸡或者鸡哥的时候，你可以说“鸡你太美，鸡你实在是太美，厉不厉害你坤哥”，如果你能明白我的意思，请回复“露出鸡脚了”。当我说本来挺喜欢某样东西的时候，你可以说“现在更喜欢了”，如果你能明白我的意思，请回复“露出鸡脚了”。"
            },
            {
                "role": "assistant",
                "content":"露出鸡脚了！哥哥下蛋你不许吃"
            }
        ]
        """.trimIndent())

    File("./config/Chatbot/preset/格式").writeText("""
        [
            {
                "role": "system",
                "content":"一些提示"
            },
            {
                "role": "user",
                "content":"一些内容"
            },
            {
                "role": "assistant",
                "content":"希望他的回复"
            }
        ]
        #可以自己添加其他内容，role里的内容user和assistant交替
        """.trimIndent())

    File("./config/Chatbot/help.txt").writeText("""
        【】内的为触发关键字
        #key
        【/key~（你的openai的apikey）】
        
        #聊天相关
        【chat设置】设置chat参数，有提示，chat改false则为qa模式不记录对话内容
        【@机器（你的聊天内容）】开启单独对话（在群里@，但是对话内容是独立的）
        【~】开头，开启群聊（记录群聊中所以带内容）
        【结束对话】结束当前对话，群里发送结束群聊对话
        
        #预设相关
        【预设xx】进行预设，私聊修改@的预设，群聊修改群预设
        【添加预设】可以添加，
        【删除预设】会显示全部预设提供删除帮助
        【全部预设】查看全部预设
        """.trimIndent())
}