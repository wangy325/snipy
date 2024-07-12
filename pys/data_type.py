'''
python的基本数据结构

- 序列
    1. 字符串 str
    2. 列表 list (列表推导式)
    3. 元组 tuple
- 集合 set
- 字典 dict
- 其他的编程技巧
'''

def __str():
    '''
    字符串不可变(immutable)
    '''
    print('who are you')
    # using \ to eacape.
    print('I\'m robot')
    # if u don't want to escape a str, like file path, use 'r' before quote.
    print(r'C:\file\path')
    # dispite the + calculation,
    # python also support * calculation, cool
    print('holy sh' + 7 * 'i' + 't!')

    # TypeError
    # str = 'python'
    # str[0] = 'P'


__str()


def __str_index_and_slice():
    '''
    在python中, 字符串是一种序列, 除了基本的操作之外, 还有序列的一些操作. 
    比如索引范访问, 切片等等.
    
    索引会越界, 但是切片不会. 不过不要去在切片里故意越界, 那样不好玩. 
    
    The index coule be understand like:
    
    +---+---+---+---+---+---+
    | p | y | t | h | o | n |
    +---+---+---+---+---+---+
    0   1   2   3   4   5   6
    -6 -5  -4  -3  -2  -1
    '''
    # index access, index could be negtive
    s = 'py' 'thon'
    print(s[0])  # p
    print(s[5])  # n
    print(s[-4])  # t
    print(s[0] == s[-0])  # True

    # Slice 切片
    print(s[2:4])  # th
    print(s[2:])  # thon
    print(s[:5])  # pytho
    print(s[-3:])  # hon
    print(s[:-3])  #pyt
    print(s[-2:] == s[4:])  # True
    print(s[:-2] == s[:4])  # True
    print((s[:1] + s[1:]) == s)  # True


__str_index_and_slice()


def __list():
    '''
    列表是一个元素可重复, 可修改的序列. 
    列表的元素可以包括不同的类型, 甚至是None 但是, 一般也不要那么做. 
    '''
    alist = [1, 1, 2, 3, 5, 8, 13, 21, 34]
    print(type(alist))
    # Join 2 lists together
    blist = alist + [54, 89]
    print(blist)
    # List element can be changed
    blist[9] = 55
    print(blist)
    # Also, list support slice
    clist = ['apple', 'orange', 'grapy', 'stawberry']
    print(clist[2:3])  # grapy
    # Assigning a slice can also modify original list, even clear the list
    clist[2:3] = ['grape']
    print(clist)  # ['apple', 'orange', 'grape', 'stawberry']
    clist[3:] = []
    print(clist)  # ['apple', 'orange', 'grape']
    clist[:] = []
    print(clist)  # []
    # List has many usful APIs
    clist.append('banana')
    clist[len(clist):] = ['peach']
    print(clist)
    # List elements can be dulplicated, and None
    dlist = ['country', 'province', 'state', 'country', 'street', None]
    print(dlist)


__list()


def __list_shalow_copy():
    '''
    列表的切片, 返回一个对列表的浅拷贝. 
    以下操作, 返回了不同的结果.
    '''
    rgba = ["Red", "Green", "Blue", "Alph"]
    # slice will return a shalow copy of a list
    rgba_correct = rgba[:]
    rgba[:] = ["Red", "Green", "Blue", "Alpha"]
    # rgba_correct[-1] = 'alpha'
    print(rgba)  # ['Red', 'Green', 'Blue', 'Alpha']
    print(rgba_correct)  # ['Red', 'Green', 'Blue', 'Alph']


__list_shalow_copy()


class ListStack:
    '''
    使用列表模拟栈
    '''
    elements = []

    # constructor
    def __init__(self, *args):
        self.elements = self.elements + list(args)

    def push(self, ele):
        self.elements.append(ele)

    def pull(self):
        if len(self.elements) > 0:
            return self.elements.pop()
        else:
            raise SystemError("stack is empty!")

    def print_stack(self):
        print(self.elements)

    def size(self):
        return len(self.elements)


stack = ListStack('wind', 'forest', 'fire')
stack.print_stack()
stack.push('mountain')
stack.print_stack()
print(stack.pull() + ', stack length is ' + str(stack.size()))


class ListQueue:
    '''
    使用list实现队列
    更快的实现方式: from collections import deque
    '''
    elements = []

    def __init__(self, *args):
        self.elements.extend(list(args))

    def push(self, ele):
        self.elements.append(ele)

    def pull(self):
        if self.size() > 0:
            return self.elements.pop(0)
        else:
            raise SystemError("queue is empty!")

    def size(self):
        return len(self.elements)

    def print_queue(self):
        print(self.elements)

queue = ListQueue(1,3,5)
queue.print_queue()
queue.push(7)
print(queue.size())
queue.pull()
queue.print_queue()