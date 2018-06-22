#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_org_asl19_qod_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "The Quote of the day is being retrieved...";
    return env->NewStringUTF(hello.c_str());
}
