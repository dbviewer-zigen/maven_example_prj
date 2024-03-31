Java(Hello3.java)から JNI を介して C(hello3.c)を CALL します。
C からは、posix_spawn を使い子プロセス(hello3_child.c)を起動します。

# Hello3.java の実装イメージ

引数
　・名前付きパイプ名(String)
　・hello3.c に渡す入力データ(ByteBuffer)
　・hello3.cg から受け取る出力データ(ByteBuffer)

仕様
　・JNI を介して、hello3.c を call する

禁止事項
　・Hello3.java と hello3 では名前付きパイプは使いません。（メモリ渡し）
　・入出力データは、byte[]ではなく、ByteBuffer を使うこと

# hello3.c の実装イメージ

引数
　・名前付きパイプ名(String)
　・子プロセスに渡す入力データ(ByteBuffer)
　・子プロセスから受け取る出力データ(ByteBuffer)
仕様
　・posix_spawan()で子プロセス(hello3_child.c)を実行

# hello3_child の実装イメージ

```c
#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h> // strlen 関数を使うために追加

#define BUFFER_SIZE 1024

int main(int argc, char *argv[]) {
    printf("@@ START C PROGRAM");

    if (argc != 2) {
        fprintf(stderr, "@@ Usage: %s <pipe_name>\n", argv[0]);
        return 1;
    }

    const char *pipe_name = argv[1];

    int pipe_fd = open(pipe_name, O_RDWR); // Read/Witeモードでオープン
    if (pipe_fd == -1) {
        perror("@@ Failed to open named pipe for reading");
        return 1;
    }

    // Read input data from named pipe
    char buffer[BUFFER_SIZE];
    ssize_t bytes_read = read(pipe_fd, buffer, BUFFER_SIZE);
    if (bytes_read == -1) {
        perror("@@ Error reading from named pipe");
        close(pipe_fd);
        return 1;
    }

    // Process input data (in this example, simply echo the input)
    printf("@@ Received input data: %.*s\n", (int)bytes_read, buffer);

    // Prepare output data
    const char *output_data = "Output data from C program";

    // Write output data to named pipe
    ssize_t bytes_written = write(pipe_fd, output_data, strlen(output_data));
    if (bytes_written == -1) {
        perror("@@ Error writing to named pipe");
        close(pipe_fd);
        return 1;
    }

    close(pipe_fd);
    return 0;
}
```
