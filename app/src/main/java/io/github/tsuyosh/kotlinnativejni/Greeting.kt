package io.github.tsuyosh.kotlinnativejni

internal class Greeting {
    external fun say(name: String): String?

    companion object {
        init {
            System.loadLibrary("greeting")
        }
    }
}