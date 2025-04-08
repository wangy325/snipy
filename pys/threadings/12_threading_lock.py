"""
Author: wangy325
Date: 2024-08-07 19:38:19
Description: 锁
"""
import time
from threading import Thread, Lock, current_thread

"""
python中没有类似Java synchronised同步关键字, 使用锁与条件来实现同步。
"""

# ############### #
#       锁        #
# ############### #

shared_int = 0
lock = Lock()


def incr_n_times(n):
    """
    挺离谱的, 明明可以看到线程竞争都乱成一锅粥了
    最后的结果竟然是正确的 :-)
    """
    global shared_int
    print(f"{current_thread().name}: >>>>{shared_int}")
    for i in range(n):
        shared_int += 1
    print(f"{current_thread().name}: <<<<{shared_int}")


def safe_incr_n_times(n):
    global shared_int
    lock.acquire()
    print(f"{current_thread().name}: >>>>{shared_int}")
    for i in range(n):
        shared_int += 1
    print(f"{current_thread().name}: <<<<{shared_int}")
    lock.release()


def incr_with_x_threads(x, func, n):
    # 列表推导式
    threads = [Thread(target=func, args=(n,)) for i in range(x)]
    start = time.time()
    global shared_int
    shared_int = 0
    for t in threads:
        t.start()
    for t in threads:
        t.join()
    print(f"finished in {time.time() - start}\n"
          f"shared expected: {n * x}\n"
          f"shared actual: {shared_int}\n"
          f"difference: {n * x - shared_int}({100 - shared_int / n / x * 100}%)")


# ##############################  #
#   这个栗子更好证明线程对资源的争夺    #
# ##############################  #
balance = 100


def withdraw_unsafe(money):
    """
    很明显可以看到2个线程同时进来, 同时获得最大余额
    :param money:
    :return:
    """
    global balance
    if balance >= money:
        time.sleep(1)
        balance -= money
        print(f"{current_thread().name}: withdraw {money}")
    else:
        print(f"{current_thread().name}: balance ({balance} < {money}) not enough")


def withdraw_safe(money):
    global balance
    lock.acquire()
    if balance >= money:
        balance -= money
        print(f"{current_thread().name}: withdraw {money}")
    else:
        print(f"{current_thread().name}: balance ({balance} < {money}) not enough")
    lock.release()


def withdraw_multi_t(func, m):
    threads = [Thread(target=func, args=(m,)) for i in range(2)]
    for t in threads:
        t.start()
    for t in threads:
        t.join()
    print(f"balance: {balance}")


if __name__ == '__main__':
    # incr_with_x_threads(10, incr_n_times, 1000000)
    # incr_with_x_threads(10, safe_incr_n_times, 1000000)
    withdraw_multi_t(withdraw_unsafe, 80)
    # withdraw_multi_t(withdraw_safe, 80)
