#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <spawn.h>
#include <sys/wait.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>

#include <string.h> // strlen 関数を使うために追加
#include <sys/time.h> // 追加
#include <time.h> // タイムスタンプを取得するために追加
#include <stdlib.h> // タイムスタンプのフォーマットのために追加

extern char **environ;


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

JNIEXPORT jboolean JNICALL Java_Hello3_callC(JNIEnv *env, jobject obj, jstring pipeName, jobject inputData, jobject outputData) {
    const char *pipeNameStr = (*env)->GetStringUTFChars(env, pipeName, 0);
    print_timestamp(); // タイムスタンプを出力
    fprintf(stdout, " [jni] start pipeNameStr:%s.\n", pipeNameStr);

    // 名前付きパイプの作成
    // mkfifo(pipeNameStr, 0666);
    // print_timestamp(); // タイムスタンプを出力
    // fprintf(stdout, " [jni] created named-pipe:%s.\n", pipeNameStr);


    print_timestamp(); // タイムスタンプを出力
    fprintf(stdout, " [jni] start-open named-pile for write.\n");
    int pipe_fd = open(pipeNameStr, O_RDWR);

    // sleep(1); // これは一時的な解決策です

    pid_t pid;
    char *argv[] = {"hello3_child", (char *)pipeNameStr, NULL};
    print_timestamp(); // タイムスタンプを出力
    fprintf(stdout, " [jni] chid-process is starting.\n");

    if (posix_spawn(&pid, "./hello3_child", NULL, NULL, argv, environ) != 0) {
        perror("Failed to spawn child process");
        (*env)->ReleaseStringUTFChars(env, pipeName, pipeNameStr);
        return JNI_FALSE;
    }
    print_timestamp(); // タイムスタンプを出力
    fprintf(stdout, " [jni] chid-process is started.\n");

    // 子プロセスが準備できるまで少し待つ
    // sleep(5); // これは一時的な解決策です

    // inputDataを名前付きパイプに書き込む

    // void *inputBuffer = (*env)->GetDirectBufferAddress(env, inputData);
    // write(pipe_fd, inputBuffer, 1024);
    // print_timestamp(); // タイムスタンプを出力
    // fprintf(stdout, " [jni] writed named-pile.");
    // close(pipe_fd);
    // print_timestamp(); // タイムスタンプを出力
    // fprintf(stdout, " [jni] close named-pile.");

    
    // // 子プロセスの終了を待つ
    int status;
    waitpid(pid, &status, 0);

    // 名前付きパイプからoutputDataを読み取る
    // pipe_fd = open(pipeNameStr, O_RDWR);
    // print_timestamp(); // タイムスタンプを出力
    // fprintf(stdout, " [jni] open named-pile for read.");
    // void *outputBuffer = (*env)->GetDirectBufferAddress(env, outputData);
    // read(pipe_fd, outputBuffer, 1024);
    // print_timestamp(); // タイムスタンプを出力
    // fprintf(stdout, " [jni] readed named-pile.");
    // close(pipe_fd);
    // print_timestamp(); // タイムスタンプを出力
    // fprintf(stdout, " [jni] close named-pile for read.");


    // 名前付きパイプの削除
    // unlink(pipeNameStr);
    // print_timestamp(); // タイムスタンプを出力
    // fprintf(stdout, " [jni] delete maned-pipe:%s.\n", pipeNameStr);

    (*env)->ReleaseStringUTFChars(env, pipeName, pipeNameStr);

    return JNI_TRUE;
}