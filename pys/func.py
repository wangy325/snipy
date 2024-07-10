# author: wangy
# date: 2024/7/9
# description:
"""

"""


def func(length, width):
    s = length * width
    print(f'Square of rectangle is {s}.')


# 函数可以直接赋值给变量
mf = func
mf(5, 3)


def __quit(retry, prompt='ready to quit? y/n', reminder='Please try again!'):
    '''
    形参列表，有多种形式的参数：
    1. 不带默认值的参数，是必填参数
    2. 带默认值的参数是可选参数
    '''
    while True:
        reply = input(prompt)
        if reply in ('y', 'yes', 'yep', 'ye'):
            return True
        elif reply in ('n', 'no', 'nope'):
            return False
        retry -= 1
        if retry < 0:
            raise ValueError('invalid user response')
        print(reminder)
    print('\n')


# 仅仅使用必填参数
# print(__quit(4))
# 使用必填和部分可选参数
# print(__quit(4, 'Do you really want to quit(y/n)?'))
# 使用全部参数
# print(__quit(4, 'Ready to quit(y/n)?', 'Please input yes or no.'))


def __fib(limit):
    '''
    # fibonacci斐波那契数列
    # 形参: limit: 最大数限制
    '''
    a, b = 0, 1
    while a < limit:
        print(a, end=' ')
        # python的解包赋值特性
        a, b = b, a + b
    print(end='\n')

__fib(100)


def __fib2(limit):
    a, b = 0, 1
    res = []
    while a < limit:
        res.append(a)
        a, b = b, a + b
    return res

print(__fib2(1000))