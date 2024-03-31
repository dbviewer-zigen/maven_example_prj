#!/bin/bash

export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/

# JNIヘッダの作成とコンパイル
$JAVA_HOME/bin/javac -h . CreateNamedPipeApp.java

# namedPipe作成用のCをコンパイル
gcc -shared -fPIC -o libccreatenamedpipe.so -I $JAVA_HOME/include -I $JAVA_HOME/include/linux createnamedpipe.c


named_pipe=my_pipe
# 名前付きパイプの存在をチェックし、存在した場合は削除する
if [ -p "$named_pipe" ]; then
    rm "$named_pipe"
fi

# 開始時間を記録
start_time=$(date +%s%N)


$JAVA_HOME/bin/java -Djava.library.path=. -cp . CreateNamedPipeApp $named_pipe
return_value=$?
# 復帰コードが0以外ならばシェルを終了
if [ $return_value -ne 0 ]; then
    echo "Java run failed with exit code $return_value"
    exit $return_value
fi

# 終了時間を記録
end_time=$(date +%s%N)

# 処理時間を計算してミリ秒に変換
duration=$((($end_time - $start_time) / 1000000))

echo "処理時間: $duration ミリ秒"