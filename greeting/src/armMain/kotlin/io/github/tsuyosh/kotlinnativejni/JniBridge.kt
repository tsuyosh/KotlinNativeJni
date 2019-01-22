package io.github.tsuyosh.kotlinnativejni

import kotlinx.cinterop.*
import platform.android.*

/**
 * Utility class for JNI interface
 * Referred from following sample:
 * https://github.com/JetBrains/kotlin-native/blob/master/samples/androidNativeActivity/src/arm32Main/kotlin/JniBridge.kt
 */
class JniBridge(private val jniEnv: CPointer<JNIEnvVar>) {
    private val envFunctions: JNINativeInterface = jniEnv.pointed.pointed!!

    private val fNewStringUTF: CPointer<CFunction<(CPointer<JNIEnvVar>?, CPointer<ByteVar>?) -> jstring?>> =
        envFunctions.NewStringUTF!!
    private val fGetStringUTFChars: CPointer<CFunction<(CPointer<JNIEnvVar>?, jstring?, CPointer<jbooleanVar>?) -> CPointer<ByteVar>?>> =
        envFunctions.GetStringUTFChars!!
    private val fReleaseStringUTFChars: CPointer<CFunction<(CPointer<JNIEnvVar>?, jstring?, CPointer<ByteVar>?) -> Unit>> =
        envFunctions.ReleaseStringUTFChars!!

    private val fExceptionCheck: CPointer<CFunction<(CPointer<JNIEnvVar>?) -> jboolean>> =
        envFunctions.ExceptionCheck!!
    private val fExceptionDescribe: CPointer<CFunction<(CPointer<JNIEnvVar>?) -> Unit>> =
        envFunctions.ExceptionDescribe!!
    private val fExceptionClear: CPointer<CFunction<(CPointer<JNIEnvVar>?) -> Unit>> =
        envFunctions.ExceptionClear!!

    fun toString(jstringObj: jstring?): String? {
        jstringObj ?: return null

        val chars: CPointer<ByteVar> = fGetStringUTFChars(
            jniEnv, jstringObj, null
        ) ?: return null

        // https://github.com/JetBrains/kotlin-native/blob/master/Interop/Runtime/src/main/kotlin/kotlinx/cinterop/Utils.kt#L409
        val stringObj = chars.toKString()

        fReleaseStringUTFChars(
            jniEnv, jstringObj, chars
        )

        return stringObj
    }

    fun toJstring(stringObj: String?): jstring? {
        if (stringObj == null) return null

        return memScoped {
            // for definition of String.cstr
            // https://github.com/JetBrains/kotlin-native/blob/master/Interop/Runtime/src/main/kotlin/kotlinx/cinterop/Utils.kt#L358
            // for definition of MemScope
            // https://github.com/JetBrains/kotlin-native/blob/master/Interop/Runtime/src/main/kotlin/kotlinx/cinterop/Utils.kt#L422
            val chars: CPointer<ByteVar> = stringObj.cstr.ptr
            val result = fNewStringUTF(jniEnv, chars)
            check()
            result
        }
    }

    private fun check() {
        if (fExceptionCheck(jniEnv) != 0.toUByte()) {
            fExceptionDescribe(jniEnv)
            fExceptionClear(jniEnv)
            throw Error("JVM exception thrown")
        }
    }
}
