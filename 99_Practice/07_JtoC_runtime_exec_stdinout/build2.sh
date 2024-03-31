#!/bin/bash

# 引数の数をチェック
if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <JAVA_HOME> <FORK|VFORK|posix_spawn>"
    exit 1
fi
# JDK_HOMEと環境変数の値を引数から取得
export JAVA_HOME=$1
export ENV_VALUE=$2

echo "-------------------------"
$JAVA_HOME/bin/java --version
echo "-------------------------"
## ----------------------
## コンパイルセクション
## ----------------------
# Cコンパイル
gcc -o child child.c
gcc -o test test.c
# Javaコンパイル
$JAVA_HOME/bin/javac -encoding UTF-8 MainProcess.java
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
$JAVA_HOME/bin/java -Djdk.lang.Process.launchMechanism=$ENV_VALUE MainProcess
return_value=$?
echo "java Main Return value: $return_value"

# 終了時間を記録
end_time=$(date +%s%N)

# 処理時間を計算してミリ秒に変換
duration=$((($end_time - $start_time) / 1000000))

echo "処理時間: $duration ミリ秒"
