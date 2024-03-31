## 結果

| 区間               | JDK11 | JDK17 |
| ------------------ | ----- | ----- |
| 子プロセス起動時間 | 180ms | 155ms |
| JVM を含む処理時間 | 312ms | 268ms |

# JDK11 での実行

```shell
sh build.sh
-------------------------
openjdk 11.0.21 2023-10-17
OpenJDK Runtime Environment (build 11.0.21+9-post-Ubuntu-0ubuntu122.04)
OpenJDK 64-Bit Server VM (build 11.0.21+9-post-Ubuntu-0ubuntu122.04, mixed mode, sharing)
-------------------------
mkfifo my_pipe Return value: 0
2024-03-30 22:57:59.383 [java] start execute process
2024-03-30 22:57:59.445 [java] Data written to named pipe data = Hello from Java!
2024-03-30 22:57:59.447 [Java] Data read from named pipe data = Output data from C program, data size = 26
[java] === Standard Output ===
2024-03-30 22:57:59.404   [child] START C PROGRAM
2024-03-30 22:57:59.404   [child] filename:./hello2 pipe_name:my_pipe.
2024-03-30 22:57:59.437   [child] pipe is opened for read.
2024-03-30 22:57:59.444   [child] Received input data: Hello from Java!
2024-03-30 22:57:59.444   [child] pipe(read) is closed.
2024-03-30 22:57:59.446   [child] pipe is opened for write.
2024-03-30 22:57:59.446   [child] return output data: Output data from C program
2024-03-30 22:57:59.446   [child] pipe(write) is closed.
[java] === Standard Error ===
2024-03-30 22:57:59.477 [java] C program exited with code: 0
2024-03-30 22:57:59.487 [java] response time :180ms
java Hello2 my_pipe Return value: 0
処理時間: 312 ミリ秒
```

## JDK17

```shell
-------------------------
openjdk 17.0.10 2024-01-16
OpenJDK Runtime Environment (build 17.0.10+7-Ubuntu-122.04.1)
OpenJDK 64-Bit Server VM (build 17.0.10+7-Ubuntu-122.04.1, mixed mode, sharing)
-------------------------
mkfifo my_pipe Return value: 0
2024-03-31 06:52:20.236 [java] start execute process
2024-03-31 06:52:20.297 [java] Data written to named pipe data = Hello from Java!
2024-03-31 06:52:20.299 [Java] Data read from named pipe data = Output data from C program, data size = 26
[java] === Standard Output ===
2024-03-31 06:52:20.263   [child] START C PROGRAM
2024-03-31 06:52:20.263   [child] filename:./hello2 pipe_name:my_pipe.
2024-03-31 06:52:20.291   [child] pipe is opened for read.
2024-03-31 06:52:20.297   [child] Received input data: Hello from Java!
2024-03-31 06:52:20.297   [child] pipe(read) is closed.
2024-03-31 06:52:20.298   [child] pipe is opened for write.
2024-03-31 06:52:20.298   [child] return output data: Output data from C program
2024-03-31 06:52:20.298   [child] pipe(write) is closed.
[java] === Standard Error ===
2024-03-31 06:52:20.316 [java] C program exited with code: 0
2024-03-31 06:52:20.323 [java] response time :155ms
java Hello2 my_pipe Return value: 0
処理時間: 268 ミリ秒
```
