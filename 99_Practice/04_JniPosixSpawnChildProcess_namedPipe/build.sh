#!/bin/bash

## ----------------------
## 環境設定セクション
## ----------------------
#export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64/

echo "-------------------------"
$JAVA_HOME/bin/java --version
echo "-------------------------"
# JNIヘッダの作成とコンパイル
$JAVA_HOME/bin/javac -h . Hello3.java
return_value=$?

# 復帰コードが0以外ならばシェルを終了
if [ $return_value -ne 0 ]; then
    echo "Java compilation failed with exit code $return_value"
    exit $return_value
fi
echo "Java compilation successfully"

# namedPipe作成用のCをコンパイル
gcc -shared -fPIC -o libhello3.so -I $JAVA_HOME/include -I $JAVA_HOME/include/linux hello3.c
return_value=$?

# 復帰コードが0以外ならばシェルを終了
if [ $return_value -ne 0 ]; then
    echo "share library compilation failed with exit code $return_value"
    exit $return_value
fi

echo "share library compilation successfully"

gcc -o hello3_child hello3_child.c
return_value=$?

if [ $return_value -ne 0 ]; then
    echo "hello3_child compilation failed with exit code $return_value"
    exit $return_value
fi

echo "hello3_child library compilation successfully"

named_pipe=my_pipe2
# #名前付きパイプの存在をチェックし、存在した場合は削除する
if [ -p "$named_pipe" ]; then
    rm "$named_pipe"
fi
mkfifo $named_pipe
return_value=$?
echo "mkfifo $named_pipe Return value: $return_value"

echo ""
# 開始時間を記録
start_time=$(date +%s%N)

# Java実行
$JAVA_HOME/bin/java -Djava.library.path=. -cp . Hello3 $named_pipe
return_value=$?
# 復帰コードが0以外ならばシェルを終了
if [ $return_value -ne 0 ]; then
    echo "Java run failed with exit code $return_value"
    exit $return_value
fi

echo "Hello3 Return value: $return_value"

# 終了時間を記録
end_time=$(date +%s%N)

# 処理時間を計算してミリ秒に変換
duration=$((($end_time - $start_time) / 1000000))

echo "処理時間: $duration ミリ秒"