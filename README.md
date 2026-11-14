# Iterable Android SDK Demo

A small Android app (Kotlin + Jetpack Compose) for exercising the
[Iterable Android SDK](https://github.com/Iterable/iterable-android-sdk).
I use it as a test harness for checking SDK behavior end to end:
identify a user, send data, and inspect in-app messages.

## What it does

The app is a single screen with four buttons:

| Button | What it calls |
| --- | --- |
| **Update Profile** | `updateUser` with a few profile fields |
| **Custom Event** | `track` with a custom event and data fields |
| **Get All Messages** | Reads the in-app message queue and logs read vs. unread |
| **Show Message** | Renders the first in-app message's HTML in a WebView dialog |

Everything is logged under the `MainActivityIterable` tag, so you can follow
along in Logcat.

## Setup

1. Open the project in Android Studio.
2. In `MainApplication.kt`, replace the `apiKey` placeholder with a mobile API
   key from your Iterable project.
3. In `MainActivity.kt`, replace the `testUserEmail` and `FirstName`
   placeholders with a test user.
4. Run on an emulator or device (min SDK 24).

The API key and user details are placeholders in this repo. Don't commit real
ones.

## Stack

- Kotlin 2.0, Jetpack Compose, Material 3
- Iterable SDK (`iterableapi` and `iterableapi-ui`) 3.5.3
- compileSdk / targetSdk 36, minSdk 24

## Notes

This is a personal test project, not an official Iterable sample.
