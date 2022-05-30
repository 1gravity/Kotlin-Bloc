[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

# Kotlin BLoC Framework

Kotlin BLoC is a Kotlin Multiplatform UI framework inspired by multiple design patterns and frameworks ([KMM UI Architecture - Part 1](https://medium.com/p/6362e14ee52a)).

## Architecture

Note, this readme offers a quick overview of the framework. For more in-depth information please consult:
- [The official website](https://1gravity.github.io/Kotlin-Bloc)
- [The Dokka documentation](https://rawcdn.githack.com/1gravity/Kotlin-Bloc/e6798e8e3a6751d126a9357231ad90830e47f6c3/docs/dokka/index.html)

### Goals
Kotlin BLoC's architectural goals are:
- be platform-agnostic
- be minimalistic / lightweight
- don't be over-engineered
- requires to write very little code
- be as un-opinionated as possible -> scales with app complexity / team size
- be composable

[KMM UI Architecture - Part 2](https://medium.com/p/e52b84aeb94d) elaborates on those goals in more detail.

### Example
To demo the framework's simplicity, here's the Hello World example of UI frameworks (counter app) for Android:
```kotlin
// define the BLoC
fun bloc(context: BlocContext) = bloc<Int, Int>(context, 1) {
    reduce { state + action }
}
```
```kotlin
class CounterActivity : AppCompatActivity() {
    // (lazy) create the lifecycle aware bloc
    private val bloc by getOrCreate { bloc(it) }
```
```kotlin
setContent {
    // observe the bloc state
    val state by bloc.observeState()
    
    // updates on state / count changes
    Text("Counter: $state")
    
    // create events / actions to update the state / count
    Button(onClick = { bloc.send( 1) }, content = { Text("Increment") })            
    Button(onClick = { bloc.send(-1) }, content = { Text("Decrement") })            
```
This is remarkably little code considering the fact that the Bloc is lifecycle aware and will survive configuration changes
(it creates an Android ViewModel under-the-hood).

**Note:** this is only one way to implement the app. Since one of the goals was to be un-opinionated, we can implement it in many ways, depending on the preferences of the developer / team.  

### Inspiration
The architecture was mainly inspired by the following design patterns and UI frameworks:

#### Design patterns
- MVI (Model-View-Intent)
- MVVM (Model-View-ViewModel)
- [SAM](https://sam.js.org)
- Redux

#### Frameworks
- [Orbit](https://orbit-mvi.org)
- [Kotlin MVI](https://arkivanov.github.io/MVIKotlin)
- [Redux Kotlin](https://reduxkotlin.org)
- [Reduce](https://github.com/genaku/Reduce)
- [Decompose](https://arkivanov.github.io/Decompose/)

### Design Overview

<img alt="BLoC Architecture - Overview" src="./docs/BLoC Architecture - BLoC Overview.svg" width="625" />

The framework has two main components:
- The **BLoC** (Business Logic Component) encapsulates your application's business logic. It receives **Action(s)** from the view, processes those actions and outputs **Proposals** (State) and optionally **SideEffect(s)**.
- The **BLoCState** holds the component's **State**. It's separate from the actual BLoC to support different scenarios like:
  - share state between business logic components
  - persist state (database, network)
  - use a global state container like Redux
  - others...

The **View** is obviously an important component too but technically not part of the framework itself (although there are extensions that support/simplify the implementation for different target platforms).

### Design Deep(er)-Dive

<img alt="BLoC Architecture - Overview" src="./docs/BLoC Architecture - BLoC Details.svg" width="625" />

#### BLoC
- tbd

#### BLoC State 
- tbd


- unidirectional data flow
- state is immutable (with some caveats)
- reactive
- coroutine / flow based

## Getting Started

### Gradle

**Step 1.** Add the JitPack repository to your build file. Add it in your root build.gradle at the
end of repositories:

```kotlin
allprojects {
    repositories {
        // ...
        maven("https://jitpack.io")
    }
}
```

**Step 2.** Add the dependency

```kotlin
dependencies {
}
```

## License

```
Copyright 2022 Emanuel Moecklin

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
