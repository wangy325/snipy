# A gemini + telegram bot script.

import traceback
import asyncio
import google.generativeai as genai
import re
import telebot
from telebot import TeleBot
from telebot.async_telebot import AsyncTeleBot
from telebot.types import Message
from md2tgmd import escape

# proxy
# https://www.pythonanywhere.com/forums/topic/32151/
from telebot import asyncio_helper

asyncio_helper.proxy = 'http://proxy.server:3128'

# global configs
error_info = "⚠️⚠️⚠️\nSomething went wrong !\nplease try to change your prompt or contact the admin !"
before_generate_info = "🤖Generating..."
download_pic_notify = "🤖Loading picture..."
model_1 = "gemini-2.0-flash"
model_2 = "gemini-1.5-pro-latest"

n = 30  #Number of historical records to keep

generation_config = {
    "temperature": 0.7,
    "top_p": 1,
    "top_k": 1,
    "max_output_tokens": 2048,
}

safety_settings = [
    {
        "category": "HARM_CATEGORY_HARASSMENT",
        "threshold": "BLOCK_NONE"
    },
    {
        "category": "HARM_CATEGORY_HATE_SPEECH",
        "threshold": "BLOCK_NONE"
    },
    {
        "category": "HARM_CATEGORY_SEXUALLY_EXPLICIT",
        "threshold": "BLOCK_NONE"
    },
    {
        "category": "HARM_CATEGORY_DANGEROUS_CONTENT",
        "threshold": "BLOCK_NONE"
    },
]

# gemini
gemini_player_dict = {}
gemini_pro_player_dict = {}
default_model_dict = {}


# Prevent "create_canvas" function from blocking the event loop.
async def make_new_gemini_canvas(model_name):
    loop = asyncio.get_running_loop()

    def create_canvas():
        model = genai.GenerativeModel(
            model_name=model_name,
            generation_config=generation_config,
            safety_settings=safety_settings,
        )
        canvas = model.start_chat()
        return canvas

    # Run the synchronous "create_canvas" function in a thread pool
    canvas = await loop.run_in_executor(None, create_canvas)
    return canvas


# Prevent "send_message" function from blocking the event loop.
async def send_message(player, message):
    loop = asyncio.get_running_loop()
    await loop.run_in_executor(None, player.send_message, message)


# Prevent "model.generate_content" function from blocking the event loop.
async def async_generate_content(model, contents):
    loop = asyncio.get_running_loop()

    def generate():
        return model.generate_content(contents=contents)

    response = await loop.run_in_executor(None, generate)
    return response


async def gemini(bot, message, m, model_type):
    player = None
    if model_type == model_1:
        player_dict = gemini_player_dict
    else:
        player_dict = gemini_pro_player_dict
    if str(message.from_user.id) not in player_dict:
        player = await make_new_gemini_canvas(model_1)
        player_dict[str(message.from_user.id)] = player
    else:
        player = player_dict[str(message.from_user.id)]
    if len(player.history) > n:
        player.history = player.history[2:]
    try:
        sent_message = await bot.reply_to(message, before_generate_info)
        await send_message(player, m)
        try:
            await bot.edit_message_text(escape(player.last.text),
                                        chat_id=sent_message.chat.id,
                                        message_id=sent_message.message_id,
                                        parse_mode="MarkdownV2")
        except:
            await bot.edit_message_text(escape(player.last.text),
                                        chat_id=sent_message.chat.id,
                                        message_id=sent_message.message_id)

    except Exception:
        traceback.print_exc()
        await bot.edit_message_text(error_info,
                                    chat_id=sent_message.chat.id,
                                    message_id=sent_message.message_id)

# telebot 400 error:   message too long
async def handle_message(text, size=4096):
    return [text[i:i+size] for i in range(0, len(text), size)]

async def main():
    # Init args
    # parser = argparse.ArgumentParser()
    # parser.add_argument("tg_token", help="telegram token")
    # parser.add_argument("GOOGLE_GEMINI_KEY", help="Google Gemini API key")
    # options = parser.parse_args()
    # print("Arg parse done.")
    bot_token = 'your telegram bot token'
    google_api_key = 'your google api key'
    genai.configure(api_key=google_api_key)

    # Init bot
    bot = AsyncTeleBot(bot_token)
    # await bot.set_webhook()
    await bot.delete_my_commands(scope=None, language_code=None)
    await bot.set_my_commands(commands=[
        telebot.types.BotCommand("start", "Start"),
        telebot.types.BotCommand("gemini", "using gemini-2.0-flash"),
        telebot.types.BotCommand("gemini_pro", "using gemini-1.5-pro"),
        telebot.types.BotCommand("clear", "Clear all history"),
        telebot.types.BotCommand("switch", "switch default model")
    ], )
    print("Bot init done.")

    # Init commands
    bot.register_message_handler(start,                 commands=['start'],         pass_bot=True)
    bot.register_message_handler(gemini_handler,        commands=['gemini'],        pass_bot=True)
    bot.register_message_handler(gemini_pro_handler,    commands=['gemini_pro'],    pass_bot=True)
    bot.register_message_handler(clear,                 commands=['clear'],         pass_bot=True)
    bot.register_message_handler(switch,                commands=['switch'],        pass_bot=True)
    bot.register_message_handler(gemini_photo_handler,  content_types=["photo"],    pass_bot=True)
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
        await bot.reply_to(message , escape("Welcome, you can ask me questions now. \nFor example: `Who is john lennon?`"), parse_mode="MarkdownV2")
    except IndexError:
        await bot.reply_to(message, error_info)

async def gemini_handler(message: Message, bot: TeleBot) -> None:
    try:
        m = message.text.strip().split(maxsplit=1)[1].strip()
    except IndexError:
        await bot.reply_to( message , escape("Please add what you want to say after /gemini. \nFor example: `/gemini Who is john lennon?`"), parse_mode="MarkdownV2")
        return
    await gemini(bot,message,m,model_1)

async def gemini_pro_handler(message: Message, bot: TeleBot) -> None:
    try:
        m = message.text.strip().split(maxsplit=1)[1].strip()
    except IndexError:
        await bot.reply_to( message , escape("Please add what you want to say after /gemini_pro. \nFor example: `/gemini_pro Who is john lennon?`"), parse_mode="MarkdownV2")
        return
    await gemini(bot,message,m,model_2)

async def clear(message: Message, bot: TeleBot) -> None:
    # Check if the player is already in gemini_player_dict.
    if (str(message.from_user.id) in gemini_player_dict):
        del gemini_player_dict[str(message.from_user.id)]
    if (str(message.from_user.id) in gemini_pro_player_dict):
        del gemini_pro_player_dict[str(message.from_user.id)]
    await bot.reply_to(message, "Your history has been cleared")

async def switch(message: Message, bot: TeleBot) -> None:
    if message.chat.type != "private":
        await bot.reply_to( message , "This command is only for private chat !")
        return
    # Check if the player is already in default_model_dict.
    if str(message.from_user.id) not in default_model_dict:
        default_model_dict[str(message.from_user.id)] = False
        await bot.reply_to( message , "Now you are using "+model_2)
        return
    if default_model_dict[str(message.from_user.id)] == True:
        default_model_dict[str(message.from_user.id)] = False
        await bot.reply_to( message , "Now you are using "+model_2)
    else:
        default_model_dict[str(message.from_user.id)] = True
        await bot.reply_to( message , "Now you are using "+model_1)

async def gemini_private_handler(message: Message, bot: TeleBot) -> None:
    m = message.text.strip()
    if str(message.from_user.id) not in default_model_dict:
        default_model_dict[str(message.from_user.id)] = True
        await gemini(bot,message,m,model_1)
    else:
        if default_model_dict[str(message.from_user.id)]:
            await gemini(bot,message,m,model_1)
        else:
            await gemini(bot,message,m,model_2)

async def gemini_photo_handler(message: Message, bot: TeleBot) -> None:
    if message.chat.type != "private":
        s = message.caption
        if not s or not (s.startswith("/gemini")):
            return
        try:
            prompt = s.strip().split(maxsplit=1)[1].strip() if len(s.strip().split(maxsplit=1)) > 1 else ""
            file_path = await bot.get_file(message.photo[-1].file_id)
            sent_message = await bot.reply_to(message, download_pic_notify)
            downloaded_file = await bot.download_file(file_path.file_path)
        except Exception:
            traceback.print_exc()
            await bot.reply_to(message, error_info)
        model = genai.GenerativeModel(model_1)
        contents = {
            "parts": [{"mime_type": "image/jpeg", "data": downloaded_file}, {"text": prompt}]
        }
        try:
            await bot.edit_message_text(before_generate_info, chat_id=sent_message.chat.id, message_id=sent_message.message_id)
            response = await async_generate_content(model, contents)
            await bot.edit_message_text(response.text, chat_id=sent_message.chat.id, message_id=sent_message.message_id)
        except Exception:
            traceback.print_exc()
            await bot.edit_message_text(error_info, chat_id=sent_message.chat.id, message_id=sent_message.message_id)
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
        model = genai.GenerativeModel(model_1)
        contents = {
            "parts": [{"mime_type": "image/jpeg", "data": downloaded_file}, {"text": prompt}]
        }
        try:
            await bot.edit_message_text(before_generate_info, chat_id=sent_message.chat.id, message_id=sent_message.message_id)
            response = await async_generate_content(model, contents)
            await bot.edit_message_text(response.text, chat_id=sent_message.chat.id, message_id=sent_message.message_id)
        except Exception:
            traceback.print_exc()
            await bot.edit_message_text(error_info, chat_id=sent_message.chat.id, message_id=sent_message.message_id)




if __name__ == '__main__':
    asyncio.run(main())

