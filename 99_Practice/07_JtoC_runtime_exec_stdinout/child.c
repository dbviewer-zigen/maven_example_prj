#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[]) {
    if (argc != 2) {
        fprintf(stderr, "Usage: %s <maxlength>\n", argv[0]);
        return 1;
    }

    int maxlength = atoi(argv[1]);
    if (maxlength <= 0) {
        fprintf(stderr, "Invalid maxlength\n");
        return 1;
    }

    // データを受け取るためのバッファ
    unsigned char buffer[128]; // 最初のバッファサイズ
    unsigned char *data = buffer;
    int dataSize = 0; // 受信したデータのサイズ

    // maxlength までのデータを標準入力から読み取る
    while (dataSize < maxlength) {
        int bytesRead = fread(data + dataSize, 1, maxlength - dataSize, stdin);
        if (bytesRead <= 0) {
            if (dataSize < maxlength) {
                fprintf(stderr, "Data anomaly\n");
                return 1;
            }
            break;
        }
        dataSize += bytesRead;

        // バッファを拡張する
        if (dataSize == sizeof(buffer)) {
            unsigned char *newData = realloc(data, dataSize * 2); // バッファサイズを2倍に拡張
            if (newData == NULL) {
                fprintf(stderr, "Memory allocation failed\n");
                return 1;
            }
            data = newData;
        }
    }

    // 受信したデータを標準出力に表示
    fwrite(data, 1, dataSize, stdout);

    // メモリを解放
    free(data);

    return 0;
}
