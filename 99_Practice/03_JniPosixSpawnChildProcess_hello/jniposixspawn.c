#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <spawn.h>
#include <sys/wait.h>
#include <string.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>

extern char **environ;


JNIEXPORT jboolean JNICALL Java_JniPosixSpawn_callC(JNIEnv *env, jobject obj, jstring pipeName) {
    const char *pipeNameStr = (*env)->GetStringUTFChars(env, pipeName, 0);
    // 名前付きパイプの作成
    mkfifo(pipeNameStr, 0666);

    pid_t pid;
    char *argv[] = {"hello", (char *)pipeNameStr, NULL};

    // helloを標準出力する子プロセスを起動する
    if (posix_spawn(&pid, "./hello", NULL, NULL, argv, environ) != 0) {
        perror("Failed to spawn child process");
        (*env)->ReleaseStringUTFChars(env, pipeName, pipeNameStr);
        return JNI_FALSE;
    }
    // 子プロセスの終了を待つ
    int status;
    waitpid(pid, &status, 0);

    // 名前付きパイプの削除
    unlink(pipeNameStr);

    (*env)->ReleaseStringUTFChars(env, pipeName, pipeNameStr);

    return JNI_TRUE;
}