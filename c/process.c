# include <stdio.h>
# include <stdlib.h>
# include <unistd.h>
# include <sys/types.h>

/*
1. 包含头文件： 
   * stdio.h：包含标准输入输出函数，如 printf。
   * unistd.h：包含 fork() 函数和 getpid() 函数。
   * sys/types.h：包含 pid_t 类型，用于存储进程 ID。

2. 调用 fork() 函数： 
   * pid_t pid = fork();  调用 fork() 函数创建子进程，并将子进程的 PID 存储在 pid 变量中。

3. 判断进程类型：
   * if (pid == 0)：子进程中，fork() 函数返回 0。
   * else if (pid > 0)：父进程中，fork() 函数返回子进程的 PID。
   * else：如果 fork() 函数失败，则返回 -1，表示错误。

4. 执行不同代码：
   * 子进程：执行子进程代码。
   * 父进程：执行父进程代码。

5. 错误处理：
   * 使用 perror() 函数打印错误信息。
*/
int main(){

    // 返回0 则子进程创建成功
    int pid = fork();

    if (/* condition */pid == 0)
    {
        /* code */
        printf("child's PID is %d. \n", getpid());
    } else if (pid > 0) {
        wait(NULL);
        printf("parent's PID is %d. \n", getpid());
        printf("child's PID is %d. \n", pid);
    } else {
        perror("fork() failed");
        exit(1);
    }
    exit(0);
    
}