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
gcc -o hello2 hello2.c
# Javaコンパイル
$JAVA_HOME/bin/javac Hello2.java
return_value=$?

# 復帰コードが0以外ならばシェルを終了
if [ $return_value -ne 0 ]; then
    echo "Java compilation failed with exit code $return_value"
    exit $return_value
fi

## ----------------------
## 実行セクション
## ----------------------

named_pipe=my_pipe
# 名前付きパイプの存在をチェックし、存在した場合は削除する
if [ -p "$named_pipe" ]; then
    rm "$named_pipe"
fi
mkfifo $named_pipe
return_value=$?
echo "mkfifo $named_pipe Return value: $return_value"

# 開始時間を記録
start_time=$(date +%s%N)

# Hello実行
$JAVA_HOME/bin/java Hello2 $named_pipe
return_value=$?
echo "java Hello2 $named_pipe Return value: $return_value"

# 終了時間を記録
end_time=$(date +%s%N)

# 処理時間を計算してミリ秒に変換
duration=$((($end_time - $start_time) / 1000000))

echo "処理時間: $duration ミリ秒"
