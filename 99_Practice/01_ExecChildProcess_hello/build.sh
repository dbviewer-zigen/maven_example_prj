#!/bin/bash

# 引数の数をチェック
if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <JAVA_HOME> <FORK|VFORK|posix_spawn>"
    exit 1
fi
# JDK_HOMEと環境変数の値を引数から取得
export JAVA_HOME=$1
export ENV_VALUE=$2

# SCRIPT_DIR=$(pwd)
# echo "現在のディレクトリは: $SCRIPT_DIR"
# スクリプトが配置されているディレクトリを取得
# echo
# script_dir=$(dirname "$0")
# echo "スクリプトが配置されているディレクトリは: $script_dir"


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
$JAVA_HOME/bin/java -Djdk.lang.Process.launchMechanism=$ENV_VALUE Hello
return_value=$?
echo "Return value: $return_value"

# 終了時間を記録
end_time=$(date +%s%N)

# 処理時間を計算してミリ秒に変換
duration=$((($end_time - $start_time) / 1000000))

echo "処理時間: $duration ミリ秒"
