import re
#  python reg

#  match youtube url and prefix/suffix words
reg = '(.*)(https?://(?:www\\.)?[a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b[a-zA-Z0-9@:%_+.~#?&//=-]*)(.*)'
reg_yt = 'https?://(www\\.)?(youtube\\.com/watch\\?v=|youtu\\.be/)[A-Za-z0-9_-]+'

pattern = re.compile(reg)

pattern2 = re.compile(reg_yt)

str1 = '这是纯文本'
str2 = 'https://youtu2.be/dP4ZcqQE_Bc'
str3 = '请总结这个视频 https://www.youtube.com/watch?v=dP4ZcqQE_Bc please'
str4 = '@genibiot hello 你好'
str5 = '@biot Please 请总结这个视频 https://youtu.be/dP4ZcqQE_Bc'
str6 = '@bot https://youtu.be/dP4ZcqQE_Bc'

rel1 = pattern.search(str1)

rel3 = pattern.match(str3)
urls = rel3.group(2)
yrel = pattern2.search(str3).group()




def fun(x, **kwargs):
    print(x)
    if kwargs:
        print(kwargs.get('name'))
        
fun(8)

fun(10, name= 'google')