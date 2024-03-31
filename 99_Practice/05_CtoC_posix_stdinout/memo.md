# JDK11 での実行

## C から C のプロセス起動

親プロセス(main.c)から子プロセス(child.c)を posix_spawn()を使い実行する

```shell
-------------------------
openjdk 11.0.21 2023-10-17
OpenJDK Runtime Environment (build 11.0.21+9-post-Ubuntu-0ubuntu122.04)
OpenJDK 64-Bit Server VM (build 11.0.21+9-post-Ubuntu-0ubuntu122.04, mixed mode, sharing)
-------------------------

Received from child: 20 バイト

Execution time: 1 ms
Program Return value: 0
処理時間: 3 ミリ秒
```
