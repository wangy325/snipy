# A gemini + telegram bot script.

import traceback
import asyncio
import telebot
from google import genai
from google.genai import types
from telebot import TeleBot
from telebot import asyncio_helper
from telebot.async_telebot import AsyncTeleBot
from telebot.types import Message
from md2tgmd import escape
# proxy
# https://www.pythonanywhere.com/forums/topic/32151/
# asyncio_helper.proxy = 'http://proxy.server:3128'

# for local debug only
asyncio_helper.proxy = 'http://127.0.0.1:7890'
import os

os.environ['http_proxy'] = "http://127.0.0.1:7890"
os.environ['https_proxy'] = "http://127.0.0.1:7890"
os.environ['all_proxy'] = "socks5://127.0.0.1:7890"

# global configs
error_info = "⚠️⚠️⚠️\nSomething went wrong !\nplease try to change your prompt or contact the admin !"
before_generate_info = "☁️Generating..."
download_pic_notify = "☁️Loading picture..."
model_1 = "gemini-2.0-flash-exp"
# model_1 = "gemini-2.0-flash"
model_2 = "gemini-2.5-pro-exp-03-25"

max_history = 30  #Number of historical records to keep

# gemini configs
generation_config = types.GenerateContentConfig(
    temperature=0.3,
    top_p=0.5,
    top_k=1,
    max_output_tokens=2048,
    seed=30,
    tools=[types.Tool(google_search=types.GoogleSearch())],
    safety_settings=[
        types.SafetySetting(
            category='HARM_CATEGORY_HARASSMENT',
            threshold='BLOCK_NONE'
        ),
        types.SafetySetting(
            category='HARM_CATEGORY_HATE_SPEECH',
            threshold='BLOCK_NONE'
        ),
        types.SafetySetting(
            category='HARM_CATEGORY_SEXUALLY_EXPLICIT',
            threshold='BLOCK_NONE'
        ),
        types.SafetySetting(
            category='HARM_CATEGORY_DANGEROUS_CONTENT',
            threshold='BLOCK_NONE'
        ),
    ]
)

# gemini
gemini_chat_dict = {}
gemini_pro_chat_dict = {}
default_chat_dict = {}
google_api_key = 'AIzaSyC-EXVc1aDFlJpRfau83XYjb_kTy1_pWZ8'
gemini_client = genai.Client(api_key=google_api_key)

# Prevent "model.generate_content" function from blocking the event loop.
async def async_generate_content(model, contents):
    loop = asyncio.get_running_loop()

    def generate():
        return gemini_client.models.generate_content(model, contents=contents)

    response = await loop.run_in_executor(None, generate)
    return response

async def gemini(bot:TeleBot, message:Message, m:str, model_type:str):
    chat = None
    if model_type == model_1:
        chat_dict = gemini_chat_dict
    else:
        chat_dict = gemini_pro_chat_dict
    if str(message.from_user.id) not in chat_dict:
        chat = gemini_client.aio.chats.create(
            model=model_type,
            config= generation_config
        )
        chat_dict[str(message.from_user.id)] = chat
    else:
        chat = chat_dict[str(message.from_user.id)]
    # new api does not support chat history
    try:
        sent_message = await bot.reply_to(message, before_generate_info)
        response = await chat.send_message(m)
        try:
            await split_and_send(bot,
                                 chat_id=sent_message.chat.id,
                                 text=response.text,
                                 message_id=sent_message.message_id,
                                 parse_mode="MarkdownV2")
        except:
            await split_and_send(bot,
                                 chat_id=sent_message.chat.id,
                                 text=response.text,
                                 message_id=sent_message.message_id)

    except Exception:
        traceback.print_exc()
        await bot.edit_message_text(error_info,
                                    chat_id=sent_message.chat.id,
                                    message_id=sent_message.message_id)


# tele_bot 400 error:   message too long
# '你好！很高兴为你服务。有什么我可以帮你的吗？\n'
async def split_and_send(bot,
                         chat_id,
                         text: str,
                         message_id=None,
                         parse_mode=None):
    slice_size = 3072  #默认最长消息长度
    slice_step = 384  #默认长度内没有换行符时，向后查询的步长
    segments = []
    start = 0
    # print(len(text))

    if len(text) > slice_size:
        while start < len(text):
            end = min(start + slice_size, len(text))
            # (start, end)  ~ 3072
            # find closest empty line
            split_point = text.rfind('\n', start, end)
            # 没找到继续向后查找
            while split_point == -1 and end < len(text):
                i_start = end
                end = min(end + slice_step, len(text))
                split_point = text.rfind('\n', i_start, end)
                # print(f"inner: {end} ")

            # print(f"outer: {split_point} ")
            if split_point - start > slice_size:
                segment = text[start:split_point + 2]
                start = split_point + 2
            elif slice_size > split_point - start > 0:
                segment = text[start:split_point + 1]
                start = split_point + 1
            else:  # split_point = -1
                segment = text[start:end]
                break  # Prevent infinite loop if no split point is found
            segments.append(segment)
    else:
        segments.append(text)
    print(segments)
    for index, segment in enumerate(segments):
        if index == 0 and message_id:
            await bot.edit_message_text(escape(segment),
                                        chat_id=chat_id,
                                        message_id=message_id,
                                        parse_mode=parse_mode)
        else:
            await bot.send_message(chat_id,
                                   escape(segment),
                                   parse_mode=parse_mode)


async def main():
    # Init args
    # parser = argparse.ArgumentParser()
    # parser.add_argument("tg_token", help="telegram token")
    # parser.add_argument("GOOGLE_GEMINI_KEY", help="Google Gemini API key")
    # options = parser.parse_args()
    # print("Arg parse done.")

    bot_token = '7436966069:AAGIoO4A5vMGvZHlMF6tMKCxVWifd0iyUaQ'
  

    # Init bot
    bot = AsyncTeleBot(bot_token)
    # await bot.set_webhook()
    await bot.delete_my_commands(scope=None, language_code=None)
    await bot.set_my_commands(commands=[
        telebot.types.BotCommand("start", "Start"),
        telebot.types.BotCommand("gemini", "using gemini-2.0-flash-exp"),
        telebot.types.BotCommand("gemini_pro", "using gemini-2.5-pro-exp"),
        telebot.types.BotCommand("clear", "Clear all history"),
        telebot.types.BotCommand("switch", "switch default model")
    ], )
    print("Bot init done.")

    # Init commands
    bot.register_message_handler(start, commands=['start'], pass_bot=True)
    bot.register_message_handler(gemini_handler,
                                 commands=['gemini'],
                                 pass_bot=True)
    bot.register_message_handler(gemini_pro_handler,
                                 commands=['gemini_pro'],
                                 pass_bot=True)
    bot.register_message_handler(clear, commands=['clear'], pass_bot=True)
    bot.register_message_handler(switch, commands=['switch'], pass_bot=True)
    bot.register_message_handler(gemini_photo_handler,
                                 content_types=["photo"],
                                 pass_bot=True)
    bot.register_message_handler(
        gemini_private_handler,
        func=lambda message: message.chat.type == "private",
        content_types=['text'],
        pass_bot=True)

    # Start bot
    print("Starting Gemini_Telegram_Bot.")
    await bot.polling(none_stop=True)


    ####
async def start(message: Message, bot: TeleBot) -> None:
    try:
        await bot.reply_to(
            message,
            escape(
                "Welcome, you can ask me questions now. \nFor example: `Who is john lennon?`"
            ),
            parse_mode="MarkdownV2")
    except IndexError:
        await bot.reply_to(message, error_info)


async def gemini_handler(message: Message, bot: TeleBot) -> None:
    try:
        m = message.text.strip().split(maxsplit=1)[1].strip()
    except IndexError:
        await bot.reply_to(
            message,
            escape(
                "Please add what you want to say after /gemini. \nFor example: `/gemini Who is john lennon?`"
            ),
            parse_mode="MarkdownV2")
        return
    await gemini(bot, message, m, model_1)


async def gemini_pro_handler(message: Message, bot: TeleBot) -> None:
    try:
        m = message.text.strip().split(maxsplit=1)[1].strip()
    except IndexError:
        await bot.reply_to(
            message,
            escape(
                "Please add what you want to say after /gemini_pro. \nFor example: `/gemini_pro Who is john lennon?`"
            ),
            parse_mode="MarkdownV2")
        return
    await gemini(bot, message, m, model_2)


async def clear(message: Message, bot: TeleBot) -> None:
    # Check if the player is already in gemini_player_dict.
    if (str(message.from_user.id) in gemini_chat_dict):
        del gemini_chat_dict[str(message.from_user.id)]
    if (str(message.from_user.id) in gemini_pro_chat_dict):
        del gemini_pro_chat_dict[str(message.from_user.id)]
    await bot.reply_to(message, "Your history has been cleared")


async def switch(message: Message, bot: TeleBot) -> None:
    if message.chat.type != "private":
        await bot.reply_to(message, "This command is only for private chat !")
        return
    # Check if the player is already in default_chat_dict.
    if str(message.from_user.id) not in default_chat_dict:
        default_chat_dict[str(message.from_user.id)] = False
        await bot.reply_to(message, "Now you are using " + model_2)
        return
    if default_chat_dict[str(message.from_user.id)] == True:
        default_chat_dict[str(message.from_user.id)] = False
        await bot.reply_to(message, "Now you are using " + model_2)
    else:
        default_chat_dict[str(message.from_user.id)] = True
        await bot.reply_to(message, "Now you are using " + model_1)


async def gemini_private_handler(message: Message, bot: TeleBot) -> None:
    m = message.text.strip()
    if str(message.from_user.id) not in default_chat_dict:
        default_chat_dict[str(message.from_user.id)] = True
        await gemini(bot, message, m, model_1)
    else:
        if default_chat_dict[str(message.from_user.id)]:
            await gemini(bot, message, m, model_1)
        else:
            await gemini(bot, message, m, model_2)


async def gemini_photo_handler(message: Message, bot: TeleBot) -> None:
    if message.chat.type != "private":
        s = message.caption
        if not s or not (s.startswith("/gemini")):
            return
        try:
            prompt = s.strip().split(maxsplit=1)[1].strip() if len(
                s.strip().split(maxsplit=1)) > 1 else ""
            file_path = await bot.get_file(message.photo[-1].file_id)
            sent_message = await bot.reply_to(message, download_pic_notify)
            downloaded_file = await bot.download_file(file_path.file_path)
        except Exception:
            traceback.print_exc()
            await bot.reply_to(message, error_info)
        contents = {
            "parts": [{
                "mime_type": "image/jpeg",
                "data": downloaded_file
            }, {
                "text": prompt
            }]
        }
        try:
            await bot.edit_message_text(before_generate_info,
                                        chat_id=sent_message.chat.id,
                                        message_id=sent_message.message_id)
            response = await async_generate_content(model_1, contents)
            await bot.edit_message_text(response.text,
                                        chat_id=sent_message.chat.id,
                                        message_id=sent_message.message_id)
        except Exception:
            traceback.print_exc()
            await bot.edit_message_text(error_info,
                                        chat_id=sent_message.chat.id,
                                        message_id=sent_message.message_id)
    else:
        s = message.caption if message.caption else ""
        try:
            prompt = s.strip()
            file_path = await bot.get_file(message.photo[-1].file_id)
            sent_message = await bot.reply_to(message, download_pic_notify)
            downloaded_file = await bot.download_file(file_path.file_path)
        except Exception:
            traceback.print_exc()
            await bot.reply_to(message, error_info)
        contents = {
            "parts": [{
                "mime_type": "image/jpeg",
                "data": downloaded_file
            }, {
                "text": prompt
            }]
        }
        try:
            await bot.edit_message_text(before_generate_info,
                                        chat_id=sent_message.chat.id,
                                        message_id=sent_message.message_id)
            response = await async_generate_content(model_1, contents)
            await bot.edit_message_text(response.text,
                                        chat_id=sent_message.chat.id,
                                        message_id=sent_message.message_id)
        except Exception:
            traceback.print_exc()
            await bot.edit_message_text(error_info,
                                        chat_id=sent_message.chat.id,
                                        message_id=sent_message.message_id)


if __name__ == '__main__':
    asyncio.run(main())
