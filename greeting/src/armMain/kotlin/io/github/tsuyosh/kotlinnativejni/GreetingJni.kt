package io.github.tsuyosh.kotlinnativejni

import kotlinx.cinterop.*
import platform.android.*

/**
 * Function for JNI function:
 * jstring Java_io_github_tsuyosh_kotlinnativejni_Greeting_say(JNIEnv *env, jobject obj, jstring name)
 */
// for usage of @CName, see https://github.com/JetBrains/kotlin-native/blob/master/backend.native/tests/produce_dynamic/simple/hello.kt#L31
@CName("Java_io_github_tsuyosh_kotlinnativejni_Greeting_say")
fun Java_io_github_tsuyosh_kotlinnativejni_Greeting_say(
    env: CPointer<JNIEnvVar>?,
    obj: jobject?,
    name: jstring?
): jstring? {
    val jniBridge = JniBridge(env!!)
    val ktName = jniBridge.toString(name)
    return ktName?.let {
        val ktGreeting = NativeGreeting().say(it)
        jniBridge.toJstring(ktGreeting)
    }
}