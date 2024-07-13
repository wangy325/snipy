'''
python的基本数据结构

- 序列
    1. 字符串 str
    2. 列表 list (列表推导式) data_list.py
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