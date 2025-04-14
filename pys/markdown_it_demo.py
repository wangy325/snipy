from markdown_it import MarkdownIt
# from markdown_it.renderer import RendererProtocol
from typing import List

#  利用 markdown-it 库解析 markdown 文本

# 示例用法
markdown_text = (
    """
好的，下面我将用一个简单的 Java 示例来解释饿汉式单例模式的缺点。

首先，我们来看一个典型的饿汉式单例模式的实现：

```java
public class EagerSingleton {

    private static final EagerSingleton instance = new EagerSingleton();

    private EagerSingleton() {
        // 私有构造函数，防止外部实例化
        System.out.println("EagerSingleton is initialized."); // 用于观察初始化时机
    }

    public static EagerSingleton getInstance() {
        return instance;
    }

    public void doSomething() {
        System.out.println("Doing something...");
    }

    public static void main(String[] args) {
        EagerSingleton.getInstance().doSomething();
    }
}
```

在这个例子中，`instance` 静态变量在类加载时就被立即初始化了。 即使你的程序在启动时并不需要使用这个单例，它也会被创建。 这就是饿汉式的主要缺点：

**缺点：资源浪费**

*   **过早初始化：** 无论是否需要，单例实例都会在类加载时被创建。 如果这个单例对象的创建过程比较耗时，或者占用的资源较多，而程序在某些情况下根本不需要用到它，那么就会造成不必要的资源浪费。
*   **无法延迟加载：** 饿汉式无法实现延迟加载（lazy loading）。 延迟加载指的是只有在真正需要使用对象时才创建它。

**示例说明：**

在上面的代码中，`System.out.println("EagerSingleton is initialized.");` 这行代码会在类加载时立即执行，表明单例对象被创建。 即使你只运行程序，但没有调用 `getInstance()` 方法，单例对象仍然会被创建。

**何时不适合使用饿汉式：**

*   当单例对象的创建非常耗时或占用大量资源时。
    
    *   这是2级列表
    *   This is 2nd class List
       
*   当无法确定程序启动时是否一定会用到该单例对象时。

**总结：**

饿汉式单例模式实现简单，线程安全，但在某些情况下可能会造成资源浪费。 如果你确定程序启动时一定会用到该单例对象，并且创建过程不复杂，那么饿汉式是一个不错的选择。 否则，可以考虑使用懒汉式或其他单例模式的变体，以实现延迟加载。

为了更清楚地说明问题，可以考虑以下场景：

假设 `EagerSingleton` 类需要连接到一个数据库，而这个数据库连接的建立需要花费较长时间。 如果程序在启动后的一段时间内并不需要访问数据库，那么使用饿汉式就会导致数据库连接过早建立，浪费资源。

希望这个解释能够帮助你理解饿汉式单例模式的缺点。

    """
)

markdown_text_2 = (
    """
为了更准确地满足你的需求，请告诉我你对哪个方向的 Python 应用更感兴趣，例如 Web 开发、数据分析、人工智能等。这样我可以为你提供更具针对性的学习建议和资源。
非常乐意为您整理一份Python学习大纲。以下是一个更精简、更侧重实用性的学习路径，适合希望快速上手并应用于实际项目的学习者：

**第一阶段：Python快速入门**

1.  **基础语法**
    *   变量、数据类型（字符串、数字、布尔值、列表、字典）
    *   运算符、表达式
    *   输入输出
2.  **流程控制**
    *   条件语句（if/else）
    *   循环语句（for/while）
3.  **函数**
    *   定义函数、调用函数
    *   参数传递
    *   返回值
4.  **常用数据结构**
    *   列表（List）：增删改查、切片
    *   字典（Dictionary）：键值对操作
5.  **模块**
    *   导入模块（import）
    *   常用标准库（如`os`, `datetime`, `random`）

**第二阶段：面向对象编程**

1.  **类与对象**
    *   定义类、创建对象
    *   属性和方法
    *   `self`关键字
2.  **继承**
    *   单继承
    *   方法重写
3.  **简单实践**
    *   编写简单的类来解决实际问题

**第三阶段：常用库与应用**

1.  **数据处理**
    *   Pandas：数据读取、清洗、分析
2.  **Web开发**
    *   Flask：搭建简单Web应用
3.  **爬虫**
    *   Requests：发送HTTP请求
    *   Beautiful Soup：解析HTML
4.  **数据库**
    *   SQLite：基本数据库操作

**第四阶段：项目实践**

1.  **选择项目**
    *   根据兴趣选择小项目（如：简单爬虫、Web应用、数据分析）
2.  **完成项目**
    *   从头到尾完成项目，遇到问题查阅资料
3.  **代码优化**
    *   学习代码规范，优化代码结构

**学习资源**

*   **在线平台**：
    *   Codecademy, Coursera, Udemy
*   **书籍**：
    *   《Python Crash Course》
    *   《Automate the Boring Stuff with Python》
*   **官方文档**：
    *   Python官方网站

**学习建议**

*   **动手实践**：边学边练，多写代码
*   **解决问题**：遇到问题积极搜索、提问
*   **持续学习**：Python生态丰富，不断学习新库和技术

这个大纲更注重实用性和快速上手，可以帮助您在较短时间内掌握Python核心技能，并应用于实际项目中。如果您对某个领域（如Web开发、数据分析）特别感兴趣，可以深入学习相关库和框架。

为了更好地帮助您，请告诉我您对哪个方向的Python应用更感兴趣？例如，Web开发、数据分析、自动化脚本等。这样我可以为您提供更具体的学习建议和资源。
    """
)

#  markdown_text 和markdown_text_2解析结果是一样的
md = MarkdownIt("commonmark", {"html": False, "typographer": True})
tokens = md.parse(markdown_text_2)

# for i, token in enumerate(tokens):
#     print(f"token{i}({token.type}):\n {token.content}")
    
chunks = []
chunk = ""
max_chunk_length = 10000
last_token_tag = ""

for token in tokens:
    last_token_tag = token.tag
    """
    分片长度限制 20 但是要保留完整的markdown格式。
    chunk 总是以 open开头 close结束
    code fence只有一个token 其他内容每一行至少有3个token（open inline close）
    列表项内容至少外层还有2个token包围（list_item_open list_item_close）
    列表顶层还有2个token（bullet_list_open bullet_list_close） 
    保证列表的完整性
    保证code fence的完整性
    """
    if token.type.endswith("_open"):
        if token.tag == 'ul':
            chunk += "\n"
        elif token.tag == 'li':
            if token.level > 1:
                chunk += "\t" + token.info + token.markup + " "
            else:
                chunk += token.info + token.markup + " "
        elif last_token_tag == 'li':
            continue
        else:
            # 判断是否结束token？
            if len(chunk) > max_chunk_length:
                # 开新的chunk
                chunks.append(chunk)
                chunk = ""

    elif token.type == "fence":
        chunk += token.markup + token.info + "\n" + token.content + token.markup + "\n\n"
    elif token.type.endswith("_close"):
        if token.tag == 'ul':
            chunk += "\n"
    else:
        chunk += token.content
        # nested ul li
        if token.level > 1:
            chunk += "\n"
        else:
            chunk += "\n\n"
else:
    chunks.append(chunk)

print(f'chunks size: {len(chunks)}')
for i, c in enumerate(chunks):
    print(f'chunk[{i}]: {c}')
