# MotusGame

A simple android application developed using Kotlin  based on MVVM clean architecture and some of android jetpack libraries
this App is using [Random word API](http://random-word-api.herokuapp.com/home).

## Used Libraries

* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) : store and manage UI-related data in a lifecycle conscious way.
* [Flow](https://developer.android.com/kotlin/flow) :  flow is a type that can emit multiple values sequentially.
* [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) : Hilt is a dependency injection library for Android.
* [Retrofit](https://square.github.io/retrofit/) :  REST Client library . .
* [coroutines](https://square.github.io/retrofit/) : execution of async (background) tasks.
* [StateFlow/SharedFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow) : StateFlow and SharedFlow are Flow APIs that enable flows to optimally emit state updates and emit values to multiple consumers.


## Features

* The user can choose the game's difficulty (number of letters) ;
* The user can choose the number of attempts ;
* The user can start and play the game ;
* During the game, the user can easily distinguish between the letters they've tried and the remaining ones ;
* the user finds the word to guess, they see a simple success screen ;
* the user fails to find the word to guess, they see a simple failure screen ;
* the user can restart a new game with a new word.
