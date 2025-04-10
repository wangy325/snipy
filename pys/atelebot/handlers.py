import asyncio
import re
from telebot.types import Message
from md2tgmd import escape

import bconf
from gemini import chat as gemini_chat
from gemini import gen_text as gemini_gen_text
from bot import bot

logger = bconf.logger
logger.name = __name__

model_1 = bconf.models.get('model_1')
model_2 = bconf.models.get('model_2')
error_info = bconf.prompts.get('error_info')
before_gen_info = bconf.prompts.get('before_generate_info')

url_reg = '(.*)(https?://(?:www\\.)?[a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b[a-zA-Z0-9@:%_+.~#?&//=-]*)(.*)'
yt_reg = 'https?://(www\\.)?(youtube\\.com/watch\\?v=|youtu\\.be/)[A-Za-z0-9_-]+'

pattern_msg = re.compile(url_reg)
pattern_yt =re.compile(yt_reg)

#  message handlers
@bot.message_handler(commands=['start'])
async def start(message: Message) -> None:
    try:
        await bot.reply_to(
            message,
            escape(
                "Welcome, you can ask me questions now. \nFor example: `Who is john lennon?`"
            ),
            parse_mode="MarkdownV2")
    except IndexError:
        await del_err_message(message)


@bot.message_handler(commands=['gemini'])
async def gemini(message: Message) -> None:
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
    await gemini_chat(bot, message, model_1)


@bot.message_handler(commands=['gemini_pro'])
async def gemini_pro(message: Message) -> None:
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
    await gemini_chat(bot, message, model_2)


@bot.message_handler(commands=['clear'])
async def clear(message: Message) -> None:
    # Check if the player is already in gemini_player_dict.
    if str(message.from_user.id) in bconf.gemini_chat_dict:
        del bconf.gemini_chat_dict[str(message.from_user.id)]
    if str(message.from_user.id) in bconf.gemini_pro_chat_dict:
        del bconf.gemini_pro_chat_dict[str(message.from_user.id)]
    await bot.reply_to(message, "Your history has been cleared")


@bot.message_handler(commands=['switch'])
async def switch(message: Message) -> None:
    if message.chat.type != "private":
        await bot.reply_to(message, "This command is only for private chat !")
        return
    # Check if the player is already in default_chat_dict.
    if str(message.from_user.id) not in bconf.default_chat_dict:
        bconf.default_chat_dict[str(message.from_user.id)] = False
        await bot.reply_to(message, "Now you are using " + model_2)
        return
    if bconf.default_chat_dict[str(message.from_user.id)]:
        bconf.default_chat_dict[str(message.from_user.id)] = False
        await bot.reply_to(message, "Now you are using " + model_2)
    else:
        bconf.default_chat_dict[str(message.from_user.id)] = True
        await bot.reply_to(message, "Now you are using " + model_1)


#  handle all other text messages in private chat
@bot.message_handler(func=lambda message: message.chat.type == "private", content_types=['text'])
async def private_text_handler(message: Message) -> None:
    model = await choose_model(message.from_user.id)
    text = message.text.strip()
    rel = pattern_msg.match(text)
    if not rel: 
        # text does not contain url
        await gemini_chat(bot, message, model)
    else:
        #  text contains url
        is_caped = bool(rel.group(1)) or bool(rel.group(3))     # true for text + url
        is_yt_url = bool(pattern_yt.match(rel.group(2)))        # true for yt url
        if is_caped:
            if is_yt_url:
                caption = (rel.group(1) + rel.group(3)).strip()
                await handle_youtube(message, caption)
            else:
                await gemini_chat(bot, message, model)


# handle group/channel '@' text chat
@bot.message_handler(regexp=bconf.BOT_NAME, chat_types=['group', 'supergroup', 'channel'], content_types=['text'])
async def group_at_text_handler(message: Message) -> None:
    """
    Handle group/supergroup and channel chat response. Users must use `@bot` to start a chat.
    
    If group user does not start a private chat with bot, and use /switch to switch model first, 
    this handler will always use default model.
    
    :param message: Instance of :class:`telebot.types.Message`
    """
    logger.info(f'group chat @ed message received: {message.text.strip()}')
    model = await choose_model(message.from_user.id)
    
    text = message.text.strip()
    rel = pattern_msg.match(text)
    if not rel: 
        # text does not contain url
        await gemini_chat(bot, message, model)
    else:
        #  text contains url
        is_caped = bool(rel.group(1)) or bool(rel.group(3))     # true for text + url
        is_yt_url = bool(pattern_yt.match(rel.group(2)))        # true for yt url
        if is_caped:
            if is_yt_url:
                caption = (rel.group(1) + rel.group(3)).strip()
                await handle_youtube(message, caption)
            else:
                await gemini_chat(bot, message, model)


# content_types=['audio', 'photo', 'voice', 'video', 'document','text', 'location', 'contact', 'sticker']
@bot.message_handler(func=lambda message: True, content_types=['photo', 'document', 'video', 'audio'])
async def file_handler(message: Message) -> None:
    return await handle_file(message)


async def handle_youtube(message:Message, caption:str):
    url = pattern_yt.search(message.text.strip()).group()
    model = await choose_model(message.from_user.id)
    await gemini_gen_text(bot, message, caption, model, True, url=url)    


#  handle files
async def handle_file(message: Message, output_img: bool = False):
    """
        Handle file messages for bot
        
        :param message: Instance of :class:`telebot.types.Message`
        
        :param output_img: Is model output image or not, default `False`
        :type : `bool`
    """
    content_type = message.content_type
    chat_type = message.chat.type
    caption = message.caption
    model = model_1
    # you must add caption and start with '@bot' to invoke this function in group chat
    if chat_type != 'private':
        if not caption or not caption.startswith(bconf.BOT_NAME):
            return
        else: 
            caption = caption.strip().split(maxsplit=1)[1].strip() if len(caption.strip().split(maxsplit=1)) > 1 else ""
    else:
        # if caption is not set in private chat...
        if not caption or caption == '':
            caption = bconf.DEFAULT_PROMPT_CN.get(content_type)
    # choose a model?
    model = await choose_model(message.from_user.id)
    await gemini_gen_text(bot, message, caption, model)

        
async def choose_model(user_id:int) -> str:
    # choose model to chat, based on content in bconf.default_chat_dict
    if str(user_id) not in bconf.default_chat_dict:
        # use model_1 by default
        bconf.default_chat_dict[str(user_id)] = True
        return model_1
    else:
        if bconf.default_chat_dict[str(user_id)]:
            return model_1
        else:
            return model_2

async def del_err_message(message: Message, err_info:str = error_info):
    msg = await bot.reply_to(
            message,
            err_info
        )
    await asyncio.sleep(30)
    await bot.delete_message(message.chat.id, msg.message_id)
