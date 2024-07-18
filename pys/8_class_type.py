"""
Author: wangy325
Date: 2024-07-18 16:28:29
Description: 类
1) 作用域和命名空间 scope.py
2) Class对象
3) 实例对象
4) 方法对象
5) 类变量与实例变量
6) 继承
7) 多继承
8) 变量要"私有"
9) 迭代器与生成器
"""


class EmptyError(BaseException):
    pass


class MyClass:
    # 类变量 (Java静态域)
    property = 'MyClass\'s property'
    trick = []

    # 构造器
    def __init__(self, *args) -> None:
        # 实例变量(Java私有域)
        self.data = []
        if args is not None and len(args) > 0:
            self.data.append(args)
        self.size = len(self.data)

    # 它既是'函数', 也是'方法对象'
    def add(self, e):
        self.data.append(e)

    def remove(self):
        if self.size > 0:
            self.data.pop()
            self.size = self.size - 1
        else:
            raise EmptyError('data is empty.')

    def fun(self):
        # 局部变量
        property = 'Let\'s hava fun!'


# type(MyClass)
print(MyClass.__class__)  # <class 'type'>

# 实例对象
cls, cls2 = MyClass(), MyClass()
print(f'{type(cls)}')

# 类变量可以在模块作用域直接读取, 并且修改
print(MyClass.property)
MyClass.property = f'property changed by {MyClass.__class__}'
print(f'MyClass.property: {MyClass.property}')
MyClass.trick.append('lie')

# 实例对象也可以读取并修改类变量
cls.property = f'property changed by {cls}'
print(f'cls.property: {cls.property}')
cls.trick.append('truth')
print(f'cls.trick: {cls.trick}')
# 注意看, cls2的property没随cls变
# 但是 trick随cls变了
# 这是因为, str 是'不可变常量'
# 而list是引用对象
print(f'cls2.property: {cls2.property}')
print(f'cls2.trick: {cls2.trick}')

# 尽管实例的修改仅仅对自己有效,
# 但是要改动类变量是非常轻松的
# 所以, python中的类变量是非常'不安全'的

# 实例变量是隐藏在构造器中的, MyClass '看不到'它们
try:
    print(MyClass.data)
except Exception as e:
    print(f'ERROR: {e}')
# 但是实例对象可以读取, 并修改
cls.data.append('apple')
print(f'All cls\'s attributes: {cls.data}, {cls.size}')
# 同样地, 仅仅对自身有效
print(f'All cls2\'s attributes: {cls2.data}, {cls2.size}')

# 除了类变量, 模块作用域里还可以访问类里面的函数
f = MyClass.fun
ff = cls.fun()
print(f'{f == ff}: {type(f)} ≠ {type(ff)}')
# 显然, 上面2个是不一样的
# 通过类名获取的是一个函数
# 通过实例获取的是一个方法引用
print(ff == MyClass.fun(cls))  # true

# 需要说明的是, 通过实例调用的'方法',
# 都会隐式地将该实例作为参数, 传递给方法
# 因此, 类的方法总有一个参数: self


# 名称改写
# 由于python没有访问权限修饰符, 所有的变量都是`public`的
# 为了保护'私有'变量
# 约定将以'__'开头的变量和函数认定为私有的
# 可以使用setter/getter方法来访问属性
class Mapping:

    def __init__(self, **args):
        self.__dict = {}
        self.__update(**args)

    def __update(self, **kwargs):
        self.update(**kwargs)

    def update(self, **args):
        if args is not None and len(args.keys()) > 0:
            for k, v in args.items():
                self.__dict[k] = v
            # self.__dict = {k: v for k in args.keys() for v in args.values()}

    def print_map(self):
        print(self.__dict)


mapp = Mapping(name='anna', age=18)
mapp.print_map()
# 名称改写, 将__dict隐藏起来了
try:
    print(mapp.__dict)
except Exception as er:
    print(er)   # 'Mapping' object has no attribute '__dict'
# 不过, 还是可以通过 _Class__attribute的方式访问
print(mapp._Mapping__dict)
# 方法同理
