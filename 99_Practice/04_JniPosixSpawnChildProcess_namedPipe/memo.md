| 区間               | JDK11 | JDK17 |
| ------------------ | ----- | ----- |
| 子プロセス起動時間 | 54ms  | 34ms  |
| JVM を含む処理時間 | 195ms | 145ms |

# JDK11 での実行

```shell
sh build.sh
-------------------------
openjdk 11.0.21 2023-10-17
OpenJDK Runtime Environment (build 11.0.21+9-post-Ubuntu-0ubuntu122.04)
OpenJDK 64-Bit Server VM (build 11.0.21+9-post-Ubuntu-0ubuntu122.04, mixed mode, sharing)
-------------------------
Java compilation successfully
share library compilation successfully
hello3_child library compilation successfully
mkfifo my_pipe2 Return value: 0

[Java] start
ByteBuffe:20
2024-03-30 23:02:35.946  [jni] start pipeNameStr:my_pipe2.
2024-03-30 23:02:35.946  [jni] start-open named-pile for write.
2024-03-30 23:02:35.946  [jni] chid-process is starting.
2024-03-30 23:02:35.953  [jni] chid-process is started.
2024-03-30 23:02:35.954   [child] START C PROGRAM
result true
[Java] end
[java] response time :54ms
Hello3 Return value: 0
処理時間: 195 ミリ秒
```

```shell
-------------------------
openjdk 17.0.10 2024-01-16
OpenJDK Runtime Environment (build 17.0.10+7-Ubuntu-122.04.1)
OpenJDK 64-Bit Server VM (build 17.0.10+7-Ubuntu-122.04.1, mixed mode, sharing)
-------------------------
Java compilation successfully
share library compilation successfully
hello3_child library compilation successfully
mkfifo my_pipe2 Return value: 0

[Java] start
ByteBuffe:20
2024-03-31 06:56:36.464  [jni] start pipeNameStr:my_pipe2.
2024-03-31 06:56:36.464  [jni] start-open named-pile for write.
2024-03-31 06:56:36.464  [jni] chid-process is starting.
2024-03-31 06:56:36.464  [jni] chid-process is started.
2024-03-31 06:56:36.465   [child] START C PROGRAM
result true
[Java] end
[java] response time :34ms
Hello3 Return value: 0
処理時間: 145 ミリ秒
```

## 結果
