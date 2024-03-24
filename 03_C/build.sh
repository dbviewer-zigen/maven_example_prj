#bash
JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
COB_LIB=/usr/lib/x86_64-linux-gnu

#コンパイル時は不要
#export LD_LIBRARY_PATH=/usr/lib/x86_64-linux-gnu:$LD_LIBRARY_PATH

# コンパイルするソース
SRC_PATH=src/main/c/main.c

# 作成するネイティブライブラリ名
OUTPUT_NAME=target/libHello.so

# COBBOLのオブジェクト
COB_LIB=../01_COBOL/target/hello.o

# JNIヘッダーの格納場所
JNI_HEADER_DIR=../02_Java/target/headers

# 出力するフォルダ
target_folder=target

# フォルダが存在するか確認し、存在する場合は削除
if [ -d "$target_folder" ]; then
    echo "Removing existing folder: $target_folder"
    rm -rf "$target_folder"
fi
# フォルダを作成
echo "Creating folder: $target_folder"
mkdir -p "$target_folder"
echo "Folder recreation complete."

# cのコンパイル
gcc -Wl,'--no-as-needed' -fPIC -shared -fPIC -I$JAVA_HOME/include/ -I$JAVA_HOME/include/linux/ -I$JNI_HEADER_DIR -L$COB_LIB -lcob $COB_LIB `cob-config --cflags` -o $OUTPUT_NAME $SRC_PATH

echo "$OUTPUT_NAMEを出力しました"

