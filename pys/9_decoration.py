"""
Author: wangy325
Date: 2024-07-22 00:25:10
Description: 闭包(closure)与装饰器(注解 decorator)
"""

import time
from contextlib import contextmanager


# 闭包 closure
# 闭包是一个特殊的函数
# 闭包有一个'内嵌'函数
# 闭包返回这个内嵌函数
# 内置函数可以访问外部的函数的变量

# 以下用闭包解一个一元一次方程 a*x + b = y


def func(a, b):
    def funx(x):
        return a * x + b

    return funx


# 3x + 4 = ?
f = func(3, 4)
# 3 * 2 + 4 = ?
print(f(2))
g = (func(3, 4)(x) for x in range(3))
for e in g:
    print(e)

# ############# #
#     装饰器     #
# ############# #
'''
装饰器是python的一种语法糖
使用@开头, 类似于Java的注解
装饰器的作用是, 很强
可以改变方法的行为, 而不直接修改方法的代码
可以实现类似AOP的功能
'''


# 装饰器应用场景：
# • 日志记录： 在函数执行之前和之后添加日志记录。
# • 计时器：  记录函数执行时间。
# • 权限控制：  检查用户权限，只有满足条件才能执行函数。
# • 缓存：  缓存函数的返回值，避免重复计算。
# • 异常处理：  在函数执行过程中捕获并处理异常。


#  装饰器的基本写法
def dec(func2):
    def wrapper(*args, **kwargs):
        print('函数开始执行...')
        rel = func2(*args, **kwargs)
        print('函数执行结束...')
        return rel

    return wrapper


# 可以看到, 装饰器其实使用了闭包


@dec
def add(x, y):
    return x + y


print(add(1, 3))


def my_timer(func3):
    def calc(*args, **kwargs):
        start = time.perf_counter()
        print('fun start...')
        rel = func3(*args, **kwargs)
        end = time.perf_counter()
        print(f'fun done, taking {end - start} ')
        return rel

    return calc


@my_timer
def cal(x, y):
    time.sleep(1)
    return x * y


print(cal(3.1415, 6.2735))


# ############# #
#   with语句    #
# ############# #

# with语句除了自动关闭文件/资源之外
# 还可以用作'上下文管理器类'
# https://docs.python.org/zh-cn/3/reference/compound_stmts.html#the-with-statement
# 

class MyContextManager:
    def __enter__(self):
        print('进入上下文管理器类')
        return self

    def __exit__(self, ext_type, ext_value, exc_tb):
        print('退出上下文管理器')


with MyContextManager() as m:
    print('在上下文管理器中执行操作')


@contextmanager
def my_context_manager():
    print("进入上下文管理器")
    try:
        yield "上下文管理器中的值"
    finally:
        print("退出上下文管理器")


with my_context_manager() as value:
    print(f"在上下文管理器中获取值：{value}")
