# 環境設定
#export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
#export COB_LIB=/usr/lib/x86_64-linux-gnu
#export LD_LIBRARY_PATH=/usr/lib/x86_64-linux-gnu:$LD_LIBRARY_PATH

SRC_PATH=src/main/cobol
target_folder=target

# フォルダが存在するか確認し、存在する場合は削除
if [ -d "$target_folder" ]; then
    echo "Removing existing folder: $target_folder"
    rm -rf "$target_folder"
fi

# フォルダを作成
echo "Creating folder: $target_folder"
mkdir -p "$target_folder"

# COBOL(OpenCOBOL)のコンパイル
cobc -c -static $SRC_PATH/hello.cob -o target/hello.o
echo "target/hello.oを出力しました"

