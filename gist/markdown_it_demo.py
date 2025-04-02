from markdown_it import MarkdownIt
# from markdown_it.renderer import RendererProtocol
from typing import List

#  利用 markdown-it 库解析 markdown 文本

# 示例用法
markdown_text = ("""
# This is a heading

This is a paragraph of text.  It contains a [link](https://www.example.com) and some *emphasis*.

```python
def hello_world():
    print("Hello, world!")
```

- Item 1
- Item 2
- Item 3

## Another heading

More text here.
                
                """)

markdown_text_2 = (
    """
    好的，我来详细解释一下单例模式。\n\n*什么是单例模式？*\n\n单例模式是一种创建型设计模式，它保证一个类只有一个实例，并提供一个全局访问点来访问该实例。  换句话说，单例模式确保在整个应用程序中，只有一个该类的对象存在，并且提供了一种简单的方法来获取这个唯一的对象。\n\n*为什么使用单例模式？*\n\n•   *资源控制：*  当需要控制对共享资源的访问时，例如数据库连接、线程池、配置对象等。单例模式可以确保只有一个实例负责管理这些资源，避免资源冲突和浪费。\n\n•   *全局访问点：*  当需要在应用程序的多个地方访问同一个对象时，单例模式提供了一个方便的全局访问点，避免了到处传递对象的麻烦。\n\n•   *性能优化：*  如果创建对象的代价很高（例如，需要进行复杂的初始化），使用单例模式可以避免重复创建对象，提高性能。\n\n•   *配置管理：* 单例模式适合管理应用程序的配置信息，确保所有组件使用相同的配置。\n\n•   *日志记录：*  单例模式可以用于创建全局的日志记录器，方便记录应用程序的运行状态。\n\n*单例模式的实现方式*\n\n以下是一些常见的单例模式的实现方式，每种方式都有其优缺点：\n\n1\\.  *饿汉式（Eager Initialization）*\n\n```java\npublic class Singleton \\{\n    private static final Singleton instance \\= new Singleton\\(\\); // 在类加载时创建实例\n    private Singleton\\(\\) \\{\\} // 私有构造函数，防止外部创建实例\n\n    public static Singleton getInstance\\(\\) \\{\n        return instance;\n    \\}\n\\}\n```\n\n    \\*   *优点：*  线程安全，实现简单。\n    \\*   *缺点：*  在类加载时就创建实例，可能会浪费资源，即使该实例没有被使用。\n\n
    """
)

#  markdown_text 和markdown_text_2解析结果是一样的
# md = MarkdownIt("commonmark", {"html": False, "typographer": True})
md = MarkdownIt()
tokens = md.parse(markdown_text_2)

# for i, token in enumerate(tokens):
#     print(f"token{i}({token.type}):\n {token.content}")
    
chunks = []
chunk = ""
max_chunk_length=20

for token in tokens:
    """
    分片长度限制 20 但是要保留完整的markdown格式。
    """
    token_content = ""
    if token.type.endswith("_open") and token.tag != "ul":
        token_content += token.markup + " "
    elif token.type == "fence":
        token_content += token.markup + token.info + "\n" + token.content + token.markup + "\n\n"
    elif token.type.endswith("_close"):
        token_content += "\n"
    else:
        token_content += token.content
    chunk += token_content
    if token.type.endswith("_close") and token.level == 0:
        # 如果分片长度不够也不能结束分片
        if len(chunk)  > max_chunk_length:
            chunks.append(chunk)
            chunk = ""
    # else: 
    
    # if current_length <= max_chunk_length:
    #     chunk += token_content
    # else:
    #     if token.type.endswith("_close"):
    #         chunk += token_content
    #         chunks.append(chunk)
    #         # start a new chunk
    #         chunk = ""
    #         current_length = 0
    #     else:
    #         chunk += token_content
    #         chunks.append(chunk)

print(f'chunks size: {len(chunks)}')
for i, c in enumerate(chunks):
    print(f'chunk[{i}]: {c}')
    
# 无法完成对Gemini返回的超长markdown文本的分割
# 原因在于无法正确分析返回的文本
# Gemini返回的文本带有\n，会影响markdown-it的解析
# 估计要对文本先做处理