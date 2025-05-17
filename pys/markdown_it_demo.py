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

# super long text
markdown_text_3 = (
    """
好的，我们继续介绍 Java 中其他的并发组件。

**5. Executor 框架和线程池 (Executor Framework and Thread Pools)**

直接创建和管理线程开销较大且容易出错。Executor 框架提供了一种将任务提交与任务执行分离开的机制，通过线程池来管理线程，从而提高性能和资源利用率。


* `Executor`: 一个简单的接口，只有一个 `execute(Runnable command)` 方法，用于提交一个待执行的任务。
* `ExecutorService`: 继承自 `Executor`，提供了更丰富的线程池管理功能，如提交带有返回值的任务 (`submit()`)、关闭线程池 (`shutdown()`, `shutdownNow()`) 等。
* `Executors`: 一个工具类，提供了创建各种类型线程池的静态工厂方法，如 `newFixedThreadPool()` (固定大小线程池)、`newCachedThreadPool()` (缓存线程池)、`newSingleThreadExecutor()` (单线程线程池) 等。
* `ThreadPoolExecutor`: `ExecutorService` 的核心实现类，提供了更细粒度的线程池配置选项。

**示例：使用 `Executors` 创建固定大小线程池**

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorExample {
    public static void main(String[] args) {
        // 创建一个固定大小为 3 的线程池
        ExecutorService executor = Executors.newFixedThreadPool(3); [28]

        // 提交 10 个任务给线程池
        for (int i = 0; i < 10; i++) {
            final int taskNumber = i;
            executor.execute(() -> {
                System.out.println("任务 " + taskNumber + " 正在由线程 " + Thread.currentThread().getName() + " 执行。");
                try {
                    Thread.sleep(500); // 模拟任务执行时间
                } catch (InterruptedException e) {
                    System.out.println("任务 " + taskNumber + " 被中断。");
                }
                System.out.println("任务 " + taskNumber + " 执行完毕。");
            });
        }

        // 关闭线程池，不再接受新任务，但会执行已提交的任务
        executor.shutdown();

        // 等待所有任务执行完毕 (可选)
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("所有任务执行完毕，线程池已关闭。");
    }
}
```

**说明：**


* `Executors.newFixedThreadPool(3)` 创建一个包含 3 个线程的线程池。
* `executor.execute()` 提交一个 `Runnable` 任务给线程池。
* 线程池会复用已有的线程来执行任务，而不是为每个任务创建新线程。
* `executor.shutdown()` 启动线程池的关闭序列。
* `executor.awaitTermination()` 阻塞当前线程，直到线程池中的所有任务执行完毕或超时。

**6. 同步辅助类 (Synchronizer Aids)**

`java.util.concurrent` 包提供了一些同步辅助类，用于协调多个线程之间的活动。


* **`CountDownLatch`:** 一个同步计数器，允许一个或多个线程等待，直到其他线程完成操作并将计数器减为零。 常用于等待多个子任务完成后再执行主任务的场景。

**示例：使用 `CountDownLatch` 等待多个线程完成**

```java
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchExample {
    private static final int NUMBER_OF_TASKS = 5;
    // 创建一个 CountDownLatch，计数器初始化为需要等待的任务数量
    private static final CountDownLatch latch = new CountDownLatch(NUMBER_OF_TASKS); [2, 11, 13]

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_TASKS);

        for (int i = 0; i < NUMBER_OF_TASKS; i++) {
            final int taskNumber = i;
            executor.execute(() -> {
                System.out.println("任务 " + taskNumber + " 开始执行。");
                try {
                    Thread.sleep((long) (Math.random() * 1000)); // 模拟任务执行时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("任务 " + taskNumber + " 执行完毕。");
                latch.countDown(); // 任务完成后，计数器减一 [2, 6, 13]
            });
        }

        System.out.println("主线程等待所有任务完成...");
        latch.await(); // 主线程在此阻塞，直到计数器归零 [2, 6, 11, 13, 14]
        System.out.println("所有任务已完成，主线程继续执行。");

        executor.shutdown();
    }
}
```

**说明：**


* `new CountDownLatch(NUMBER_OF_TASKS)` 创建一个计数器为 5 的 `CountDownLatch`。
* 每个任务执行完毕后调用 `latch.countDown()`，将计数器减一。
* 主线程调用 `latch.await()` 阻塞，直到计数器变为零。
* **`CyclicBarrier`:** 一个可循环使用的同步屏障，允许一组线程相互等待，直到所有线程都到达一个共同的屏障点，然后所有线程才能继续执行。 与 `CountDownLatch` 不同，`CyclicBarrier` 可以重复使用。

**示例：使用 `CyclicBarrier` 实现多线程分阶段执行**

```java
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclicBarrierExample {
    private static final int NUMBER_OF_PARTIES = 3;
    // 创建一个 CyclicBarrier，需要等待 3 个线程，并在所有线程到达后执行一个 Runnable
    private static final CyclicBarrier barrier = new CyclicBarrier(NUMBER_OF_PARTIES, () -> {
        System.out.println("所有线程已到达屏障，继续下一阶段！");
    }); [4, 9]

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_PARTIES);

        for (int i = 0; i < NUMBER_OF_PARTIES; i++) {
            final int workerNumber = i;
            executor.execute(() -> {
                try {
                    System.out.println("工作线程 " + workerNumber + " 正在执行第一阶段任务。");
                    Thread.sleep((long) (Math.random() * 1000));
                    System.out.println("工作线程 " + workerNumber + " 第一阶段任务完成，等待其他线程。");
                    barrier.await(); // 等待所有线程到达屏障 [4, 9, 16]

                    System.out.println("工作线程 " + workerNumber + " 正在执行第二阶段任务。");
                    Thread.sleep((long) (Math.random() * 1000));
                    System.out.println("工作线程 " + workerNumber + " 第二阶段任务完成。");
                    barrier.await(); // 再次等待所有线程到达屏障 [4, 9, 16]

                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
    }
}
```

**说明：**


* `new CyclicBarrier(NUMBER_OF_PARTIES, barrierAction)` 创建一个需要 3 个线程等待的屏障，并在所有线程到达后执行 `barrierAction` 中的代码。
* 每个线程在完成一个阶段的任务后调用 `barrier.await()`，这将使线程阻塞，直到所有线程都调用了 `await()`。
* 当所有线程都到达屏障时，屏障会被“打开”，所有等待的线程会继续执行。
* `CyclicBarrier` 可以重复使用，适用于需要多次同步的场景。
* **`Semaphore`:** 一个计数信号量，用于控制同时访问某个资源的线程数量。 它维护一个许可集，线程在访问资源前需要获取许可，访问完成后释放许可。

**示例：使用 `Semaphore` 限制并发访问数量**

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreExample {
    // 创建一个 Semaphore，只允许最多 3 个线程同时访问
    private static final Semaphore semaphore = new Semaphore(3); [3, 7, 20, 21]

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();

        for (int i = 0; i < 10; i++) {
            final int clientNumber = i;
            executor.execute(() -> {
                try {
                    System.out.println("客户端 " + clientNumber + " 尝试获取访问许可...");
                    semaphore.acquire(); // 获取许可，如果许可不足则阻塞 [3, 7, 21]
                    System.out.println("客户端 " + clientNumber + " 获取到许可，正在访问资源。");
                    Thread.sleep(2000); // 模拟访问资源的时间
                    System.out.println("客户端 " + clientNumber + " 访问资源完毕，释放许可。");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release(); // 释放许可 [3, 7, 21]
                }
            });
        }

        executor.shutdown();
    }
}
```

**说明：**


* `new Semaphore(3)` 创建一个初始许可数量为 3 的 `Semaphore`。
* 线程在访问共享资源前调用 `semaphore.acquire()` 获取许可。 如果当前可用许可数量为零，线程将被阻塞，直到有其他线程释放许可。
* 线程访问完资源后调用 `semaphore.release()` 释放许可。
* `Semaphore` 常用于限制对有限资源的并发访问，例如数据库连接池、文件句柄等。
* **`Exchanger`:** 用于在两个线程之间交换对象的同步点。 当两个线程都到达交换点时，它们会交换彼此持有的对象。

**示例：使用 `Exchanger` 在两个线程之间交换数据**

```java
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExchangerExample {
    // 创建一个 Exchanger，用于交换 String 类型的数据
    private static final Exchanger<String> exchanger = new Exchanger<>(); [1, 10, 15, 17]

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // 线程 A
        executor.execute(() -> {
            try {
                String dataA = "来自线程 A 的数据";
                System.out.println("线程 A 准备交换数据: " + dataA);
                // 到达交换点，并交换数据 [1, 10, 15, 17, 19]
                String receivedData = exchanger.exchange(dataA); [1, 10, 15, 17, 19]
                System.out.println("线程 A 收到数据: " + receivedData);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // 线程 B
        executor.execute(() -> {
            try {
                String dataB = "来自线程 B 的数据";
                System.out.println("线程 B 准备交换数据: " + dataB);
                // 到达交换点，并交换数据 [1, 10, 15, 17, 19]
                String receivedData = exchanger.exchange(dataB); [1, 10, 15, 17, 19]
                System.out.println("线程 B 收到数据: " + receivedData);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        executor.shutdown();
    }
}
```

**说明：**


* `new Exchanger<String>()` 创建一个用于交换 `String` 对象的 `Exchanger`。
* 两个线程都调用 `exchanger.exchange(data)` 方法。 调用 `exchange()` 的线程会阻塞，直到另一个线程也调用了 `exchange()`。
* 当两个线程都到达交换点时，它们会交换彼此传入的数据，并从 `exchange()` 方法返回对方的数据。
* `Exchanger` 常用于基因算法、管道设计等场景，其中两个工作线程需要定期交换数据。

**7. Future 和 CompletableFuture**


* **`Future`:** 表示一个异步计算的结果。 可以用来检查任务是否完成、等待任务完成并获取结果，或者取消任务。
* **`CompletableFuture`:** 在 Java 8 中引入，是对 `Future` 的增强，提供了更丰富的异步编程能力，支持链式调用、组合多个异步操作以及异常处理。

**示例：使用 `CompletableFuture` 进行异步计算和回调**

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CompletableFutureExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("主线程开始。");

        // 创建一个 CompletableFuture，异步执行一个任务并返回结果
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> { [5, 8, 12, 29]
            System.out.println("异步任务开始执行...");
            try {
                TimeUnit.SECONDS.sleep(2); // 模拟耗时操作
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("异步任务执行完毕。");
            return "异步任务的结果";
        });

        // 注册一个回调函数，当异步任务成功完成时执行 [5, 12, 29]
        future.thenAccept(result -> { [5, 12, 29]
            System.out.println("在回调中处理结果: " + result);
        });

        // 注册一个异常处理函数，当异步任务发生异常时执行 [5, 12, 29]
        future.exceptionally(e -> { [5, 12, 29]
            System.err.println("异步任务发生异常: " + e.getMessage());
            return null; // 返回一个默认值或 null
        });

        System.out.println("主线程继续执行其他任务...");

        // 阻塞主线程，等待 CompletableFuture 完成 (可选，通常在需要获取最终结果时使用)
        // String result = future.get(); // 会阻塞直到任务完成并返回结果
        // System.out.println("主线程获取到异步结果: " + result);

        // 为了让异步任务有机会执行，主线程等待一段时间
        TimeUnit.SECONDS.sleep(3);

        System.out.println("主线程结束。");
    }
}
```

**说明：**


* `CompletableFuture.supplyAsync()` 创建一个 `CompletableFuture`，并在默认的线程池中异步执行一个 `Supplier` 任务，该任务会返回一个结果。
* `thenAccept()` 注册一个 `Consumer` 回调，当 `CompletableFuture` 成功完成时执行，并接收任务的结果。
* `exceptionally()` 注册一个 `Function` 回调，当 `CompletableFuture` 发生异常时执行
    """
)


def split_markdown(text):

    #  markdown_text 和markdown_text_2解析结果是一样的
    md = MarkdownIt("commonmark", {"html": False, "typographer": True})
    tokens = md.parse(text)

    # for i, token in enumerate(tokens):
    #     print(f"token{i}({token.type}):\n {token.content}")

    chunks = []
    chunk = ""
    max_chunk_length = 2048
    last_token_tag = ""

    print(f'text length: {len(text)}')
    last_open_token_tag = ''

    for token in tokens:
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
            # last_open_token_tag = token.tag
            if token.tag == 'ul':
                chunk += "\n"
            elif token.tag == 'li':
                if token.level > 1:
                    chunk += "\t" + token.info + token.markup + " "
                else:
                    chunk += token.info + token.markup + " "
            elif last_open_token_tag == 'li':
                last_open_token_tag = token.tag
                continue
            else:
                # 判断是否结束token？
                if len(chunk) > max_chunk_length:
                    # 开新的chunk
                    chunks.append(chunk)
                    chunk = ""
            last_open_token_tag = token.tag
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
        chunks.append(chunk[:-2])
    return chunks


if __name__ == '__main__':
    res = split_markdown(markdown_text_3)
    print(f'chunks size: {len(res)}')
    for i, c in enumerate(res):
        print(f'chunk[{i}] length: {len(c)}')
        print(f'chunk[{i}]: {c}')
