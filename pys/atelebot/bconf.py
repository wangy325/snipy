import os
import argparse
import logging
from google.genai import types
from telebot import asyncio_helper

logger = logging.getLogger(__name__)
log_handler = logging.StreamHandler()
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
log_handler.setFormatter(formatter)
logger.addHandler(log_handler)
logger.setLevel(logging.INFO)

# global configs
prompts = {
    'error_info':
    "⚠️⚠️⚠️\nSomething went wrong !\nplease try to change your prompt or contact the admin !",
    'before_generate_info': "☁️Generating...",
    'download_pic_notify': "☁️Loading picture..."
}

models = {
    "model_1": "gemini-2.0-flash-exp",
    "model_2": "gemini-2.5-pro-exp-03-25"
}

# Init args
parser = argparse.ArgumentParser(
    description="Telegram bot args for Google gemini")
parser.add_argument("--token",
                    help="telegram token",
                    default=os.environ.get("BOT_TOKEN"))
parser.add_argument("--key",
                    help="Google Gemini API key",
                    default=os.environ.get("API_KEY"))
parser.add_argument("--botName",
                    help="telegram bot name",
                    default=os.environ.get("BOT_NAME"))
parser.add_argument("--webhook",
                    help="telegram bot deploy webhook. optional",
                    default=os.environ.get("WEB_HOOK"))
args = parser.parse_args()
if not args.token:
    parser.error(
        "--token is required. Please provide it as a command-line argument or set the BOT_TOKEN environment variable. "
    )
BOT_TOKEN = args.token
if not args.key:
    parser.error(
        "--key is required. Please provide it as a command-line argument or set the API_KEY environment variable. "
    )
API_KEY = args.key
if not args.botName:
    logger.warning(
        "--botName is not provided. Using default value '@wygemibot'. You have to set it if you want deploy your own bot with full features work fine."
    )
BOT_NAME = args.botName or '@wygemibot'
WEB_HOOK = args.webhook or None
WEBHOOK_PORT = 8443
WEBHOOK_LISTEN = '0.0.0.0'
#  no ssl used for koy'eb
WEBHOOK_URL = f"{WEB_HOOK}/{BOT_TOKEN}/"

## Gemini model configs
generation_config = types.GenerateContentConfig(
    temperature=0.3,
    top_p=0.5,
    top_k=1,
    max_output_tokens=1024,
    seed=30,
    tools=[types.Tool(google_search=types.GoogleSearch())],
    safety_settings=[
        types.SafetySetting(category='HARM_CATEGORY_HARASSMENT',
                            threshold='BLOCK_NONE'),
        types.SafetySetting(category='HARM_CATEGORY_HATE_SPEECH',
                            threshold='BLOCK_NONE'),
        types.SafetySetting(category='HARM_CATEGORY_SEXUALLY_EXPLICIT',
                            threshold='BLOCK_NONE'),
        types.SafetySetting(category='HARM_CATEGORY_DANGEROUS_CONTENT',
                            threshold='BLOCK_NONE'),
    ])

#  chat dicts 
gemini_chat_dict = {}
gemini_pro_chat_dict = {}
default_chat_dict = {}

# for local debug only
def setLocalProxies():
    asyncio_helper.proxy = 'http://127.0.0.1:7890'
    os.environ['http_proxy'] = "http://127.0.0.1:7890"
    os.environ['https_proxy'] = "http://127.0.0.1:7890"
    os.environ['all_proxy'] = "socks5://127.0.0.1:7890"
    logger.info('Using local proxy.')

logger.info("Arg parse done.")
