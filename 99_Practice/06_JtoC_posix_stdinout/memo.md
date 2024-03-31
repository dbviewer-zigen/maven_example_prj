| 区間               | JDK11 | JDK17 |
| ------------------ | ----- | ----- |
| 子プロセス起動時間 | 23ms  | 18ms  |
| JVM を含む処理時間 | 147ms | 110ms |

# JDK11 での実行

## C から C のプロセス起動

```shell
-------------------------
openjdk 11.0.21 2023-10-17
OpenJDK Runtime Environment (build 11.0.21+9-post-Ubuntu-0ubuntu122.04)
OpenJDK 64-Bit Server VM (build 11.0.21+9-post-Ubuntu-0ubuntu122.04, mixed mode, sharing)
-------------------------
Java compilation successfully

Starting nativeprocess[jni] execution.
Input size: 1024, Output size: 1024
Spawning child process.
Sending data to child process.
Reading data from child process.
  [child] Starting child process execution.
  [child] input data : abc
Native process execution completed.
value:ABC
[java] response time :74ms
Main Return value: 0
Program Return value: 0
処理時間: 297 ミリ秒
```

## JDK17

```shell
h build.sh
-------------------------
/root/namedpipe_byteBuffer/06_JtoC_posix_stdinout
openjdk 17.0.10 2024-01-16
OpenJDK Runtime Environment (build 17.0.10+7-Ubuntu-122.04.1)
OpenJDK 64-Bit Server VM (build 17.0.10+7-Ubuntu-122.04.1, mixed mode, sharing)
-------------------------
Java compilation successfully

Starting nativeprocess[jni] execution.
Input size: 1024, Output size: 1024
Spawning child process.
Sending data to child process.
Reading data from child process.
  [child] Starting child process execution.
  [child] input data : abc
Native process execution completed.
value:ABC
[java] response time :23ms
Main Return value: 0
Program Return value: 0
処理時間: 147 ミリ秒
```

```shell
-------------------------
/root/namedpipe_byteBuffer/06_JtoC_posix_stdinout
openjdk 17.0.10 2024-01-16
OpenJDK Runtime Environment (build 17.0.10+7-Ubuntu-122.04.1)
OpenJDK 64-Bit Server VM (build 17.0.10+7-Ubuntu-122.04.1, mixed mode, sharing)
-------------------------
Java compilation successfully

Starting nativeprocess[jni] execution.
Input size: 1024, Output size: 1024
Spawning child process.
Sending data to child process.
Reading data from child process.
  [child] Starting child process execution.
  [child] input data : abc
Native process execution completed.
value:ABC
[java] response time :18ms
Main Return value: 0
Program Return value: 0
処理時間: 110 ミリ秒
```
