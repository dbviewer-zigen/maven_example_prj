#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <spawn.h>
#include <sys/wait.h>
#include <unistd.h>
#include <string.h>
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

JNIEXPORT jboolean JNICALL Java_Main_executeNativeProcess(JNIEnv *env, jobject obj, jobject inputData, jobject outputData) {
    fprintf(stderr, " [JNI] Starting nativeprocess[jni] execution.\n");

    void *input = (*env)->GetDirectBufferAddress(env, inputData);
    jlong inputSize = (*env)->GetDirectBufferCapacity(env, inputData);
    void *output = (*env)->GetDirectBufferAddress(env, outputData);
    jlong outputSize = (*env)->GetDirectBufferCapacity(env, outputData);
    if (input == NULL || output == NULL) {
        fprintf(stderr, "Failed to get direct buffer address.\n");
        return JNI_FALSE;
    }

    fprintf(stderr, " [JNI] Input size: %ld, Output size: %ld\n", inputSize, outputSize);

    int to_child_pipe[2];
    int from_child_pipe[2];
    pid_t pid;
    posix_spawn_file_actions_t action;

    if (pipe(to_child_pipe) == -1 || pipe(from_child_pipe) == -1) {
        perror("pipe");
        return JNI_FALSE;
    }

    posix_spawn_file_actions_init(&action);
    posix_spawn_file_actions_adddup2(&action, to_child_pipe[0], STDIN_FILENO);
    posix_spawn_file_actions_adddup2(&action, from_child_pipe[1], STDOUT_FILENO);
    posix_spawn_file_actions_addclose(&action, to_child_pipe[1]);
    posix_spawn_file_actions_addclose(&action, from_child_pipe[0]);

    fprintf(stderr, " [JNI] Spawning child process.\n");
    if (posix_spawn(&pid, "./child", &action, NULL, (char *[]){NULL}, environ) != 0) {
        perror("spawn");
        return JNI_FALSE;
    }

    close(to_child_pipe[0]);
    close(from_child_pipe[1]);

    fprintf(stderr, " [JNI] Sending data to child process.\n");
    write(to_child_pipe[1], input, (size_t)inputSize);
    close(to_child_pipe[1]);

    fprintf(stderr, " [JNI] Reading data from child process.\n");
    ssize_t nbytes = read(from_child_pipe[0], output, (size_t)outputSize);
    if (nbytes < 0) {
        perror("read");
        return JNI_FALSE;
    }

    waitpid(pid, NULL, 0);
    posix_spawn_file_actions_destroy(&action);
    close(from_child_pipe[0]);

    fprintf(stderr, " [JNI] Native process execution completed.\n");
    return JNI_TRUE;
}