# Mirai-Gpt-Plugin

mcl下载链接：https://github.com/iTXTech/mcl-installer/releases

具体看大佬的项目：https://github.com/iTXTech/mirai-console-loader

机器人暂时只支持一个，默认为控制台登录的第一个机器人


# 使用方法

运行mcl后将插件放到plugin文件夹下，重新运行mcl

以下【】内为触发关键字

【/key~（你的key）】进行配置apikey 可以在./config/Chatbot/preset/key.txt中查看你添加的key

【预设】（预设文件名字）可以设置预设，文件在./config/Chatbot/preset内，可以自己添加预设，名字为xx.json，格式要求和已有的预设相同，特别注意“”符号与换行

【添加预设】可以添加预设，在/preset文件夹内可以查看我提供的预设，重名会备份之前的预设为"name.bac"
【删除预设】可删除，并列出已存在的预设

默认为default ，默认开启chat模式（能记住聊天记录），需要手动输入“结束对话”停止（记住这一点，不然key的消耗会很大）

每个对话独立，预设独立 ，在机器人眼中，不同的qq有不同的预设与聊天记录
【@机器人】（你的聊天内容）开始你的单独对话，【~】（你的聊天内容）开启群聊（记录所有以~触发的聊天，可区分人）



通过【~】开启的聊天，会记录为群组聊天，【@机器人】开启的聊天会记为单独聊天
【结束对话】在群聊中输入则结束整个群聊对话，私聊【结束】则结束个人对话

# HELP
【gpt】输入gpt可以查看帮助

# 其他
如果想使用gpt-4模型，给机器人重新配置gpt-4的key，并修改./config/Chatbot文件夹下的parameters.json文件，理论上可以直接把model的值"gpt-3.5-turbo"改成"gpt-4"
具体格式可以参考官方文档https://platform.openai.com/docs/api-reference

