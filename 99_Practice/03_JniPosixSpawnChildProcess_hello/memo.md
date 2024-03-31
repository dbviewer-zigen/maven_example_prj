## 結果

| 区間               | JDK11 | JDK17 |
| ------------------ | ----- | ----- |
| 子プロセス起動時間 | 53ms  | 25ms  |
| JVM を含む処理時間 | 371ms | 123ms |

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
mkfifo my_pipe_jni_posix Return value: 0

[Java] start
[Child Process] Hello from C!
result true
[Java] end
[java] response time :53ms
JniPosixSpawn Return value: 0
処理時間: 371 ミリ秒
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
mkfifo my_pipe_jni_posix Return value: 0

[Java] start
[Child Process] Hello from C!
result true
[Java] end
[java] response time :25ms
JniPosixSpawn Return value: 0
処理時間: 123 ミリ秒
```
