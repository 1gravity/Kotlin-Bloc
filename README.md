[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

# Kotlin Bloc

`Kotlin Bloc` is a Multiplatform UI framework combining the best of MVI, MVVM and [SAM](https://sam.js.org/). It's 
- **simple**: designed from the ground up for simplicity with a super concise syntax
- **adaptable**: supports different programming styles
- **predictable**: write reactive applications that behave consistently and are easy to debug and test
- **composable**: grows with the complexity of app and the size of the team

![Bloc Architecture - Overview](./docs/Bloc%20Architecture%20-%20Bloc%20Overview.svg)

- The **Bloc** (Business Logic Component) encapsulates your application's business logic. It receives **Action(s)** from the view, processes those actions and outputs **Proposals** and optionally **SideEffect(s)**.
- The **BlocState** holds the component's **State**. It's separate from the actual Bloc to support different scenarios like:
  - share state between business logic components
  - persist state (database, network)
  - use a global state container like Redux

:::tip
Note, this readme offers a quick overview of the framework. For more in-depth information please visit:
- [The official website](https://1gravity.github.io/Kotlin-Bloc)
- [The Dokka documentation](https://rawcdn.githack.com/1gravity/Kotlin-Bloc/e6798e8e3a6751d126a9357231ad90830e47f6c3/docs/dokka/index.html)
:::

## Setup

[![Download](https://img.shields.io/maven-central/v/org.orbit-mvi/orbit-viewmodel)](https://search.maven.org/artifact/com.1gravity/bloc-core)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

```kotlin
dependencies {
    // the core library
    implementation("com.1gravity:bloc-core:0.1.2-SNAPSHOT")

    // add to use the framework together with Redux
    implementation("com.1gravity:bloc-redux:0.1.2-SNAPSHOT")

    // useful extensions for Android and Jetpack/JetBrains Compose
    implementation("com.1gravity:bloc-compose:0.1.2-SNAPSHOT")
}
```

## Example

The "Hello World" example of UI frameworks is the counter app. Creating the "business logic" part of such an app is incredibly simple with `Kotlin Bloc`:

```kotlin
fun bloc(context: BlocContext) = bloc<Int, Int>(context, 1) {
    reduce { state + action }
}
```

The view part is very simple too.

### Android

```kotlin
class CounterActivity : AppCompatActivity() {

    // create or retrieve the lifecycle aware Bloc
    private val bloc by getOrCreate { bloc(it) }
```

```kotlin
setContent {
    // observe the Bloc state
    val state by bloc.observeState()

    // updates on state / count changes
    Text("Counter: $state")

    // emit events / actions to update the state / count
    Button(onClick = { bloc.send(1) }, content = { Text("Increment") })
    Button(onClick = { bloc.send(-1) }, content = { Text("Decrement") })
}
```

This is very little code considering the fact that the Bloc is lifecycle aware and will survive configuration changes.

### iOS

On iOS there's a bit more boilerplate code ([BlocHolder](https://github.com/1gravity/Kotlin-Bloc/blob/master/iosApp/iosApp/utils/BlocHolder.swift) and [BlocObserver](https://github.com/1gravity/Kotlin-Bloc/blob/master/iosApp/iosApp/utils/BlocObserver.swift) are omitted here) but it's still pretty "lean":

```swift
// iOS
struct CounterView: View {
    // create the lifecycle aware Bloc
    private let holder = BlocHolder { CounterKt.bloc(context: $0) }
    
    @ObservedObject
    private var model: BlocObserver<KotlinInt, KotlinInt, KotlinUnit>

    init() {
        // observe the Bloc state
        model = BlocObserver(holder.value)
    }
```
```swift
var body: some View {
    return VStack() {    
        // updates on state / count changes
        Text("Counter \(model.value)")
    
        // emit events / actions to update the state / count
        Button(
            action: { holder.value.send(value:  1) },
            label: { Text("Increment") }
        )
        Button(
            action: { holder.value.send(value: -1) },
            label: { Text("Decrement") }
        )
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
