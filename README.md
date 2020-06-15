# Augmented Reality for Android
This is an educational app for testing the Google [ARCore](https://developers.google.com/ar/) library with [Sceneform](https://github.com/google-ar/sceneform-android-sdk). 

Unfortunately _Sceneform_ was discontinued during the project development, so we had to downgrade and use the latest stable version so the features could work properly.

## Working with Augmented Reality

If you want to know the details of how ARCore and Sceneform work, and more details about how this project was done, please checkout this [codelab](http://augmented-reality.anacoimbra.dev/)

> It's important to know that only a few devices have ARCore support. If your device doesn't support you can configure an emulator or test the "No AR" feature of the app. You can check if your device is supported in this [list](https://developers.google.com/ar/discover/supported-devices).

## Installation
Clone this repository and import into **Android Studio**
```bash
git clone https://github.com/anacoimbrag/android-augmented-reality.git
```

### Pre-requisites:
* Android Version 5.0 Lollipop
* Android SDK 29
* [Android Studio 4+](https://developer.android.com/studio/index.html)

## Configuration

### Build variants
Use the Android Studio *Build Variants* button debug and release build types

## Build APK

### Generating signed build from Android Studio
1. ***Build*** menu
2. ***Generate Signed APK...*** 
3. If you are generating a build for publish choose ***App Bundle***, if is for sending a build for installing directly on device choose ***APK***
3. Fill in the keystore information *(you only need to do this once manually and then let Android Studio remember it)*

### Build from command line

#### Build debug APK
To build a debug APK, open a command line and navigate to the root of your project directory—from Android Studio, select **View > Tool Windows > Terminal**. To initiate a debug build, invoke the _assembleDebug_ task:
```ah
gradlew assembleDebug
```
This creates an APK named **_module_name_-debug.apk** in **_project_name_/_module_name_/build/outputs/apk/**. The file is already signed with the debug key and aligned with zipalign, so you can immediately install it on a device.

Or to build the APK and immediately install it on a running emulator or connected device, instead invoke _installDebug_

```
gradlew installDebug
```

#### Build a release APK

When you're ready to release and distribute your app, you must build a release APK that is signed with your private key.
If you don't have a private key or need help, see [Sign Your App](https://developer.android.com/studio/publish/app-signing.html).

____

For more information checkout [Android Developer Documentation](https://developer.android.com/index.html)
