#include "com_example_app_Hello.h"
#include <stdio.h>
#include <libcob.h>

extern int hello();

JNIEXPORT void JNICALL Java_com_example_app_Hello_hello(JNIEnv *env, jobject obj)
{
  printf("**jni stat\n");
  cob_init(0, NULL);
  hello();
  printf("**jni end\n");
}

