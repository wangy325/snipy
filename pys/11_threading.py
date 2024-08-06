# author: wangy
# date: 2024/7/24
# description: 初窥线程

"""
Python的线程模型参考了Java的线程模型.

Python的线程模型由threading标准库提供:
https://docs.python.org/zh-cn/3/library/threading.html
"""
import time
from threading import Thread
from threading import current_thread


###
##
class MyThread(Thread):
    """
    直接继承Thread类来创建线程
    """

    def run(self):
        time.sleep(1)
        print(f"{current_thread().name} is running...")


def creat_thread_by_extend():
    for i in range(5):
        t = MyThread()
        t.start()
        # thread.join() 在当前线程上等待thread线程运行完成
        t.join()

    # MainThread
    print(f"{current_thread().name} done...")


def thread_func(name):
    time.sleep(1)
    print(f"{name} is running...")


# python 模块的main方法
if __name__ == '__main__':
    # creat_thread_by_extend()
    for i in range(5):
        t = Thread(target=thread_func, args=(f"thread-{i + 1}",))
        t.start()
        t.join()

    print(f"{current_thread().name} done...")
