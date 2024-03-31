#!/bin/bash

## ----------------------
## 環境設定セクション
## ----------------------
#export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64/

echo "-------------------------"
pwd
$JAVA_HOME/bin/java --version
echo "-------------------------"
# JNIヘッダの作成とコンパイル
$JAVA_HOME/bin/javac -h . Main.java
return_value=$?

# 復帰コードが0以外ならばシェルを終了
if [ $return_value -ne 0 ]; then
    echo "Java compilation failed with exit code $return_value"
    exit $return_value
fi
echo "Java compilation successfully"

# namedPipe作成用のCをコンパイル
gcc -shared -fPIC -o libnativeprocess.so -I $JAVA_HOME/include -I $JAVA_HOME/include/linux nativeprocess.c
return_value=$?

# 復帰コードが0以外ならばシェルを終了
if [ $return_value -ne 0 ]; then
    echo "share library compilation failed with exit code $return_value"
    exit $return_value
fi

gcc -o child child.c

echo ""
# 開始時間を記録
start_time=$(date +%s%N)

# Java実行
$JAVA_HOME/bin/java -Djava.library.path=. -cp . Main
return_value=$?
# 復帰コードが0以外ならばシェルを終了
if [ $return_value -ne 0 ]; then
    echo "Java run failed with exit code $return_value"
    exit $return_value
fi

echo "Main Return value: $return_value"

return_value=$?
# 復帰コードが0以外ならばシェルを終了
if [ $return_value -ne 0 ]; then
    echo "Program run failed with exit code $return_value"
    exit $return_value
fi

echo "Program Return value: $return_value"

# 終了時間を記録
end_time=$(date +%s%N)

# 処理時間を計算してミリ秒に変換
duration=$((($end_time - $start_time) / 1000000))

echo "処理時間: $duration ミリ秒"
