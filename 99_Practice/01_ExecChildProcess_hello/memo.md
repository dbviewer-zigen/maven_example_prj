# JDK の違いによるプロセス起動時間の違いを確認してみた。

JDK13 から、プロセス起動時の内部処理が fork(),exec()から posix_spawn()に変わり、信頼性とパフォーマンスが向上されていると言われているが、実際どれだけパフォーマンスが向上しているか検証してみた。

## 結果

| 区間               | JDK11 | JDK17 |
| ------------------ | ----- | ----- |
| 子プロセス起動時間 | 97ms  | 43ms  |
| JVM を含む処理時間 | 261ms | 132ms |

・JDK11 と JDK17 で比較
・シンプルな C プログラムを作成し、Java から Runtime.exec を実行
・Java から子プロセスに対しては引数や送信するデータは無し
・子プロセスの標準出力を Java から取得し、Java の標準出力から出力する

パフォーマンスが向上しているように見えるが、本当に posix_spawn でプロセスが起動されているのかわからない（確認する方法が必要）
jdk.lang.Process.launchMechanism で FORK/VFORK/POSIX_SPAWN をスイッチできるという記事もあるが、、、本当か

## 背景

JDK13 からプロセス起動時の内部処理が fork(),exec()から posix_spawn()に変わり、信頼性とパフォーマンスが向上されていると記載されている。実際どれだけパフォーマンスが向上しているか検証する。

> JDK13 から Linux のデフォルトのプロセス起動メカニズムは、posix_spawn を使用するようになりました
> Runtime.exec()そして ProcessBuilder、Linux では posix_spawn(3)を使用して子プロセスを生成するようにな>りました。
>
> これにより、メモリの少ない状況での信頼性とパフォーマンスが向上します。
>
> JDK-8213192

[Bug ID: JDK-8213192 (process) Change the Process launch mechanism default on Linux to be posix_spawn](https://bugs.java.com/bugdatabase/view_bug.do?bug_id=JDK-8213192)

## 検証環境

| 分類                        | Version              | Spec                                         |
| --------------------------- | -------------------- | -------------------------------------------- |
| ホスト OS                   | macOS Ventura 13.6.3 | 3.4 GHz クアッドコア Intel Core i5, RAM:40GB |
| ゲスト OS(docker container) | Ubuntu 22.04.3 LTS   | -                                            |

```shell
cat /etc/os-release
PRETTY_NAME="Ubuntu 22.04.3 LTS"
NAME="Ubuntu"
VERSION_ID="22.04"
VERSION="22.04.3 LTS (Jammy Jellyfish)"
VERSION_CODENAME=jammy
ID=ubuntu
ID_LIKE=debian
HOME_URL="https://www.ubuntu.com/"
SUPPORT_URL="https://help.ubuntu.com/"
BUG_REPORT_URL="https://bugs.launchpad.net/ubuntu/"
PRIVACY_POLICY_URL="https://www.ubuntu.com/legal/terms-and-policies/privacy-policy"
UBUNTU_CODENAME=jammy
```

## JDK 11

```shell
sh build.sh
-------------------------
openjdk 11.0.21 2023-10-17
OpenJDK Runtime Environment (build 11.0.21+9-post-Ubuntu-0ubuntu122.04)
OpenJDK 64-Bit Server VM (build 11.0.21+9-post-Ubuntu-0ubuntu122.04, mixed mode, sharing)
-------------------------
[Child Process] Hello from C!
[java] response time :97ms
Return value: 0
処理時間: 261 ミリ秒

```

## JDK17

```shell
sh build.sh
-------------------------
openjdk 17.0.10 2024-01-16
OpenJDK Runtime Environment (build 17.0.10+7-Ubuntu-122.04.1)
OpenJDK 64-Bit Server VM (build 17.0.10+7-Ubuntu-122.04.1, mixed mode, sharing)
-------------------------
[Child Process] Hello from C!
[java] response time :43ms
Return value: 0
処理時間: 132 ミリ秒
```

## ソース

### Hello.java

・Runtime.exec() を使い、子プロセス(hello)を起動する。

・子プロセスの標準出力を取得し、親プロセスの標準出力として出力する。

```java
import java.io.*;

public class Hello {
    public static void main(String[] args) throws IOException, InterruptedException {
        long start = System.currentTimeMillis();
        String cProgramPath = "./hello";

        Process process = Runtime.getRuntime().exec(cProgramPath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        int exitCode = process.waitFor();
        //System.out.println("C program exited with code: " + exitCode);
        long end = System.currentTimeMillis(); // after create instance
        System.out.println("[java] response time :" + (end-start) + "ms");

    }
}

```

### hello.c

純粋なプロセス起動時間を計測するために、最小限の実装としている。

```c
#include <stdio.h>

int main() {
    printf("[Child Process] Hello from C!\n");
    return 0;
}
```

### build.sh

・JAVA_HOME を切り替えることで、JDK のバージョンを変更

```shell
#!/bin/bash

## ----------------------
## 環境設定セクション
## ----------------------
#export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64/

echo "-------------------------"
$JAVA_HOME/bin/java --version
echo "-------------------------"

## ----------------------
## コンパイルセクション
## ----------------------
# Cコンパイル
gcc -o hello hello.c
# Javaコンパイル
$JAVA_HOME/bin/javac Hello.java


## ----------------------
## 実行セクション
## ----------------------

# 開始時間を記録
start_time=$(date +%s%N)

# Hello実行
$JAVA_HOME/bin/java Hello
return_value=$?
echo "Return value: $return_value"

# 終了時間を記録
end_time=$(date +%s%N)

# 処理時間を計算してミリ秒に変換
duration=$((($end_time - $start_time) / 1000000))

echo "処理時間: $duration ミリ秒"

```
