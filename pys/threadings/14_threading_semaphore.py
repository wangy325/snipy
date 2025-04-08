# author: wangy
# date: 2024/8/10
# description: 信号量

"""
信号量允许多个线程同时访问资源.
信号量的目的是为了'保护资源', 而不是共享资源.
比如控制数据库连接数量以控制并发量, 控制线程池大小等等

信号量持有"许可", 任何想访问对象的线程都必须通过信号量
获得许可, 结束后"归还"许可. 许可用完后, 其他线程则无法
再访问对象, 须等待其他线程"归还"许可.

获得许可的线程, 不一定要归还许可

许可其实是一个线程安全的"计数器"
"""
import time
from threading import current_thread, BoundedSemaphore, Thread, Lock


class ConnectionDB:
    init_id = 0

    def __init__(self):
        ConnectionDB.init_id += 1
        self.__connect_id = ConnectionDB.init_id
        self.__connect_name = f"{self.__class__.__name__}-{self.__connect_id}"

    def connect(self):
        print(f"{current_thread().name}: {self.__connect_name} connected ✔️")

    def disconnect(self):
        print(f"{current_thread().name}: {self.__connect_name} disconnected ✖️")


class Pool:

    def __init__(self, cls, pool_size: int = 10):
        self.__pool_size = pool_size
        # 一般使用有界信号量
        self.__semaphore = BoundedSemaphore(self.__pool_size)
        self.__lock = Lock()
        self.__objs = [cls() for x in range(pool_size)]

    def __get_connection(self):
        if self.__semaphore.acquire(timeout=5):
            with self.__lock:
                return self.__objs.pop()
        else:
            raise TimeoutError("too many connections")

    def use_conn(self):
        try:
            conn = self.__get_connection()
        except TimeoutError as e:
            print(f"{current_thread().name}: {e}")
            return
        try:
            conn.connect()
            time.sleep(3)
        finally:
            conn.disconnect()
            with self.__lock:
                self.__objs.append(conn)
            self.__semaphore.release()


def run(p: Pool):
    p.use_conn()


if __name__ == '__main__':
    pool = Pool(ConnectionDB, 4)
    ts = [Thread(target=run, args=(pool,), daemon=True)
          for x in range(10)]
    for t in ts:
        t.start()

    for t in ts:
        t.join()
