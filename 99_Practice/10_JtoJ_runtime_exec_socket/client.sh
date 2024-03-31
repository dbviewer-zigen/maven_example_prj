#!/bin/bash

JDK11=/usr/lib/jvm/java-11-openjdk-amd64/
JDK17=/usr/lib/jvm/java-17-openjdk-amd64/
ENV_FORK=FORK
ENV_VFORK=VFORK
ENV_posix_spawn=posix_spawn

# JDK_HOMEと環境変数の値を引数から取得
export JAVA_HOME=$JDK17
export ENV_VALUE=$ENV_FORK

echo "-------------------------"
$JAVA_HOME/bin/java --version
echo "-------------------------"
## ----------------------
## コンパイルセクション
## ----------------------

# Javaコンパイル
$JAVA_HOME/bin/javac -encoding UTF-8 Client.java
return_value=$?

# 復帰コードが0以外ならばシェルを終了
if [ $return_value -ne 0 ]; then
    echo "Java compilation failed with exit code $return_value"
    exit $return_value
fi

## ----------------------
## 実行セクション
## ----------------------

# 開始時間を記録
start_time=$(date +%s%N)

# Hello実行
$JAVA_HOME/bin/java -Djdk.lang.Process.launchMechanism=$ENV_VALUE Client
return_value=$?
echo "java Main Return value: $return_value"

# 終了時間を記録
end_time=$(date +%s%N)

# 処理時間を計算してミリ秒に変換
duration=$((($end_time - $start_time) / 1000000))

echo "処理時間: $duration ミリ秒"
