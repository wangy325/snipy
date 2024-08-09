# author: wangy
# date: 2024/8/8
# description: 条件


"""
再讨论下汽车打蜡-抛光的问题
"""
import random
import sys
import time
from threading import Thread, current_thread, Condition

buffered = False
waxed = True
condition = Condition()


def wax_on():
    """
    打蜡
    """
    global waxed, buffered
    with condition:
        while True:
            if not buffered:
                # print(f"{current_thread().name}: waiting for buffering")
                condition.wait()
            buffered = False
            waxed = True
            time.sleep(1)
            print(f"{current_thread().name}")
            condition.notify()


def buffer_on():
    """
    抛光
    """
    global buffered, waxed
    with condition:
        while True:
            if not waxed:
                # print(f"{current_thread().name}: waiting for waxing")
                condition.wait()
            buffered = True
            waxed = False
            time.sleep(1)
            print(f"{current_thread().name}")
            condition.notify()


def car_polish():
    wax_t = Thread(target=wax_on, args=(), daemon=True)
    wax_t.start()
    buffer_t = Thread(target=buffer_on, args=(), daemon=True)
    buffer_t.start()
    wax_t.join()
    buffer_t.join()


# ########################## #
#   利用锁和条件实现一个阻塞队列  #
# ########################## #

class MyBlockQueue:
    """
    A FIFO blocking queue implemented by list
    """

    def __init__(self, size):
        self.__threshold = size
        self.__condition = Condition()
        self.__queue = []
        self.size = len(self.__queue)

    def put(self, item):
        with self.__condition:
            while self.is_full():
                print(f"{current_thread().name}: waiting for buffering")
                self.__condition.wait()
                continue
            self.__queue.append(item)
            self.size += 1
            print(f"{current_thread().name}: >>>> {item}, queue size: {self.size}")
            self.__condition.notify_all()

    def get(self):
        with self.__condition:
            while self.is_empty():
                print(f"{current_thread().name}: waiting for item in queue")
                self.__condition.wait()
                continue
            pop = self.__queue.pop(0)
            self.size -= 1
            print(f"{current_thread().name}: <<<< {pop}, queue size: {self.size}")
            self.__condition.notify_all()

    def is_full(self):
        return self.size == self.__threshold

    def is_empty(self):
        return self.size == 0

    def print_queue(self):
        print(self.__queue)


queue = MyBlockQueue(1)  # 容量为1的阻塞队列


def in_queue(q):
    # 格式化浮点数
    while True:
        random_uniform = "{:.2f}".format(random.uniform(1, 10))
        q.put(random_uniform)
        time.sleep(1)


def out_queue(q):
    while True:
        q.get()
        time.sleep(2)


def in_out_queue_thread(func1, func2):
    # 粗暴地使用守护线程,
    # 方便主线程结束时, 也结束子线程
    in_t = [Thread(target=func1, name=f"in_t{i}", args=(queue,), daemon=True)
            for i in range(1)]
    out_t = [Thread(target=func2, name=f"ou_t{i}", args=(queue,), daemon=True)
             for i in range(1)]
    for to in in_t:
        to.start()
    for ti in out_t:
        ti.start()


if __name__ == '__main__':
    car_polish()
    # in_out_queue_thread(in_queue, out_queue)
    time.sleep(4)
    sys.exit(-1)
