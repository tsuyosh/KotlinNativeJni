# JNI native function with Kotlin/Native

This is a sample application implements JNI native function with Kotlin/Native

# How to build

## Building a native library

```shell
./gradlew :greeting:linkReleaseSharedArm32
```

If built successfully, copy `greeting/build/bin/arm32/main/release/shared/libgreeting.so` to `app/src/main/jniLibs/armeabi-v7a/`

## Building an android app

As usual :)
