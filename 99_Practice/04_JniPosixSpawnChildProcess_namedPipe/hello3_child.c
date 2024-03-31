#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h> // strlen 関数を使うために追加
#include <sys/time.h> // 追加
#include <time.h> // タイムスタンプを取得するために追加
#include <stdlib.h> // タイムスタンプのフォーマットのために追加

#define BUFFER_SIZE 1024
void print_timestamp() {
    struct timeval tv;
    gettimeofday(&tv, NULL); // 現在の時刻とタイムゾーン情報を取得

    char timestamp[26]; // ミリ秒を含むタイムスタンプ用のバッファ
    strftime(timestamp, sizeof(timestamp), "%Y-%m-%d %H:%M:%S", localtime(&tv.tv_sec)); // 秒までの部分をフォーマット

    char millis[8]; // ミリ秒を文字列として格納するバッファ
    sprintf(millis, ".%03d", (int)(tv.tv_usec / 1000)); // ミリ秒の部分をフォーマット
    strcat(timestamp, millis); // タイムスタンプにミリ秒を追加

    printf("%s ", timestamp); // タイムスタンプを出力
}
int main(int argc, char *argv[]) {
    print_timestamp(); // タイムスタンプを出力
    printf("  [child] START C PROGRAM\n");
    if (argc != 2) {
        fprintf(stderr, "  [child] Usage: %s <pipe_name>\n", argv[0]);
        return 1;
    }else{
        // for test
        return 0;
    }

    print_timestamp(); // タイムスタンプを出力
    fprintf(stdout, "  [child] filename:%s pipe_name:%s.\n", argv[0], argv[1]);
    // sleep(5); // これは一時的な解決策です
    const char *pipe_name = argv[1];

    // int pipe_fd = open(pipe_name, O_RDONLY); // Read onlyモード
    int pipe_fd = open(pipe_name, O_RDWR); // Read/Witeモードでオープン
    // int pipe_fd = open(pipe_name, O_WRONLY); // 書き込み専用でオープン
    if (pipe_fd == -1) {
        perror("  [child] Failed to open named pipe for reading");
        return 1;
    }
    print_timestamp(); // タイムスタンプを出力
    fprintf(stdout, "  [child] pipe is opened for read.\n");


    // Read input data from named pipe
    char buffer[BUFFER_SIZE];
    ssize_t bytes_read = read(pipe_fd, buffer, BUFFER_SIZE);
    if (bytes_read == -1) {
        perror("  [child] Error reading from named pipe");
        close(pipe_fd);
        return 1;
    }
    // Process input data (in this example, simply echo the input)
    print_timestamp(); // タイムスタンプを出力
    printf("  [child] Received input data: %.*s\n", (int)bytes_read, buffer);
    close(pipe_fd);
    print_timestamp(); // タイムスタンプを出力
    printf("  [child] pipe(read) is closed.\n");


    pipe_fd = open(pipe_name, O_RDWR); // Witeモードでオープン
    if (pipe_fd == -1) {
        perror("  [child] Failed to open named pipe for writeing");
        return 1;
    }
    print_timestamp(); // タイムスタンプを出力
    fprintf(stdout, "  [child] pipe is opened for write.\n");

    // Prepare output data
    const char *output_data = "Output data from C program";
    
    // Write output data to named pipe
    ssize_t bytes_written = write(pipe_fd, output_data, strlen(output_data));
    if (bytes_written == -1) {
        perror("  [child] Error writing to named pipe");
        close(pipe_fd);
        return 1;
    }
    print_timestamp(); // タイムスタンプを出力
    printf("  [child] return output data: %.*s\n", (int)bytes_written, output_data);

    close(pipe_fd);
    print_timestamp(); // タイムスタンプを出力
    printf("  [child] pipe(write) is closed.\n");
    return 0;
}
