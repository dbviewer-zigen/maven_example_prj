#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <unistd.h>
#include <stdbool.h>

JNIEXPORT jboolean JNICALL Java_CreateNamedPipeApp_createNamedPipe(JNIEnv *env, jobject obj, jstring pipeName) {
    const char *pipe_name = (*env)->GetStringUTFChars(env, pipeName, NULL);

    // Create the named pipe using mkfifo
    if (mkfifo(pipe_name, 0666) == -1) {
        perror("Error creating named pipe");
        // exit(EXIT_FAILURE);　// process is down
        return JNI_FALSE;
    }

    (*env)->ReleaseStringUTFChars(env, pipeName, pipe_name);
    return JNI_TRUE;
}
