#define _POSIX_C_SOURCE 199309L
#include <stdio.h>
#include <stdlib.h>
#include <spawn.h>
#include <sys/wait.h>
#include <unistd.h>
#include <time.h>

extern char **environ;

int main(void) {
    int to_child_pipe[2];
    int from_child_pipe[2];
    pid_t pid;
    posix_spawn_file_actions_t action;
    char buffer[1024];
    ssize_t nbytes;
    struct timespec start_time, end_time;

    // パイプの作成
    if (pipe(to_child_pipe) == -1 || pipe(from_child_pipe) == -1) {
        perror("pipe");
        exit(EXIT_FAILURE);
    }

    // posix_spawn 用のファイルアクションの初期化
    posix_spawn_file_actions_init(&action);
    posix_spawn_file_actions_adddup2(&action, to_child_pipe[0], STDIN_FILENO);
    posix_spawn_file_actions_adddup2(&action, from_child_pipe[1], STDOUT_FILENO);
    posix_spawn_file_actions_addclose(&action, to_child_pipe[1]);
    posix_spawn_file_actions_addclose(&action, from_child_pipe[0]);

    // 子プロセスの起動
    clock_gettime(CLOCK_MONOTONIC, &start_time);
    if (posix_spawn(&pid, "./child", &action, NULL, (char *[]){NULL}, environ) != 0) {
        perror("spawn");
        exit(EXIT_FAILURE);
    }

    // 親プロセス側の不要なパイプのエンドを閉じる
    close(to_child_pipe[0]);
    close(from_child_pipe[1]);

    // 子プロセスへデータを送信
    // const char data[] = "1234567890"; // 送信するデータ
    // size_t dataSize = sizeof(data) - 1; // データのサイズ（NULL終端を除く）
    // write(to_child_pipe[1], data, dataSize);
    // close(to_child_pipe[1]); // 子プロセスにEOFを送信
    
    // 子プロセスへバイナリデータを送信
    int data[] = {1, 2, 3, 4, 5}; // 送信するバイナリデータ（整数の配列）
    size_t dataSize = sizeof(data);
    write(to_child_pipe[1], data, dataSize);
    close(to_child_pipe[1]); // 子プロセスにEOFを送信


    // 子プロセスからの出力を読み取る
    nbytes = read(from_child_pipe[0], buffer, sizeof(buffer));
    if (nbytes > 0) {
        printf("Received from child: %.*s\n", (int)nbytes, buffer);
    }
    close(from_child_pipe[0]);

    // 子プロセスの終了を待つ
    waitpid(pid, NULL, 0);
    clock_gettime(CLOCK_MONOTONIC, &end_time);

    // 実行時間の計算と出力
    long elapsed_time = (end_time.tv_sec - start_time.tv_sec) * 1000 + (end_time.tv_nsec - start_time.tv_nsec) / 1000000;
    printf("Execution time: %ld ms\n", elapsed_time);

    // リソースのクリーンアップ
    posix_spawn_file_actions_destroy(&action);

    return EXIT_SUCCESS;
}