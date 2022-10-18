# Live Chat
A simple Android app about **a live chat with realtime database**. It was made with Jetpack Compose, and it uses Hilt as Dependence Injection for providing all the products' Firebase dependencies in each module in the app. Also, the app uses Asynchronous Flow to link the ViewModel with UI and Firebase's implementations.

The app includes three main features, such as:

1. Anonymous authentication with firebase auth.
2. Users can change their username to the app.
3. Chat shows the date of the message in format like "time ago".

# Screenshots

![live chat screen shot](https://i.postimg.cc/8CTffHgf/live-chat-screenshot.png)

<div style="display:flex;justify-content: center">

![live chat gif](https://i.postimg.cc/DwMR8CXB/live-chat.gif)

</div>

---

The following is a list of the tools and libraries that it were used in this app.

## Android Components:
* [Jetpack Compose][1]
* [ViewModel][2]

## Firebase's Products:
* [Firebase Authentication][3]
* [Realtime Database][4]

## Dependency Injection:
* [Hilt for Android][5]

## Asynchronous Programming:
* [Kotlin Coroutines][6]
* [Asynchronous Flow][7]

---

If you clone this project, you might have to create a Firebase project and add the **google-services.json** file that is generated it. For more information please visit the official documentation in the next link **"[Add my project in Firebase][9]"**.

[1]: https://developer.android.com/jetpack/compose
[2]: https://developer.android.com/topic/libraries/architecture/viewmodel
[3]: https://firebase.google.com/docs/auth
[4]: https://firebase.google.com/docs/database
[5]: https://developer.android.com/training/dependency-injection/hilt-android
[6]: https://kotlinlang.org/docs/coroutines-overview.html
[7]: https://kotlinlang.org/docs/flow.html
[9]: https://firebase.google.com/docs/android/setup