#include <stdio.h>
#include <unistd.h>

int main(void) {
    fprintf(stderr, "  [child] Starting child process execution.\n");

    char buffer[1024];
    ssize_t nbytes;

    // 標準入力からデータを読み取る
    nbytes = read(STDIN_FILENO, buffer, sizeof(buffer));
    if (nbytes < 0) {
        perror("read");
        return 1;
    }

    // 子プロセスが受け取った入力データ(max 1024byte)
    fprintf(stderr, "  [child] input data : %s\n", buffer);

    // 読み取ったデータのサイズを標準出力に書き込む
    // printf("%zd\n", nbytes);
    printf("ABC");
    


    return 0;
}