package io.github.tsuyosh.jni

internal class Greeting {
    external fun say(name: String): String?

    companion object {
        init {
            System.loadLibrary("greeting")
        }
    }
}
