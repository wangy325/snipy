# author: wangy
# date: 2024/8/13 16:23
# description: 事件

"""
事件是2个线程交互的最简单机制

一个线程发出信号

而另一个线程等待信号

"""
import sys
import time
from threading import Thread, Event, current_thread

waxed = Event()
buffered = Event()

'''
还是以 13_threading_condition.py 中的抛光-打蜡为例

可以看到, 使用Event 会简洁一些
'''


def wax_on():
    while True:
        waxed.wait()
        print(f"{current_thread().name} wax")
        time.sleep(1)
        waxed.clear()
        buffered.set()


def buffer_on():
    while True:
        buffered.wait()
        print(f"{current_thread().name}: buffer")
        time.sleep(1)
        buffered.clear()
        waxed.set()


if __name__ == '__main__':
    ws = Thread(target=wax_on, daemon=True)
    bs = Thread(target=buffer_on, daemon=True)
    buffered.set()
    ws.start()
    bs.start()

    time.sleep(10)
    sys.exit(-1)
