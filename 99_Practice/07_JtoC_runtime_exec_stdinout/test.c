#include <stdio.h>
#include <unistd.h>

int main(void) {
    char buffer[1024];
    ssize_t nbytes;

    // 標準入力からデータを読み取る
    nbytes = read(STDIN_FILENO, buffer, sizeof(buffer));
    if (nbytes < 0) {
        perror("read");
        return 1;
    }

    // 読み取ったデータのサイズを標準出力に書き込む
    // printf("%zd バイト\n", nbytes);
    // 受けたデータをそのまま返す
    printf("%s\n", buffer);
    return 0;
}