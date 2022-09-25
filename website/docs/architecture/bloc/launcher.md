---
id: coroutine_launcher
title: Coroutine Launcher
sidebar_label: Coroutine Launcher
hide_title: true
---

## Coroutine Launcher

There are extension functions for `InitializerContext`, `ThunkContext` and `ReducerContext` to launch coroutines from initializers, thunks and reducers.
They come in two "flavors":

```
// flavor 1
thunk {
    launch(JobConfig(cancelPrevious = true, jobId = "MyJob")) {
        // some asynchronous code
    }
}

// flavor 2
thunk {
    launch {
        // some asynchronous code
    }
}

```
### Flavor 1: JobConfig

The `JobConfig` has two properties:
1. `cancelPrevious`: If set to false (default) a coroutine is simply launched, no additional checks. If set to true however, all previous jobs that were started with the same `jobId`, will be cancelled and the coroutine is suspended till all jobs have finished (cancelAndJoin).
2. `jobId`: if `cancelPrevious` is true, then the jobId can be used to group different jobs together to make sure only one of them is run at a time. The jobId defaults to "DefaultJobId".

Using the `JobConfig` we can e.g. launch/cancel asynchronous operations when a thunk is triggered multiple times like in this example:

```kotlin
// the user can select multiple posts within a brief period of time
fun onSelected(post: Post) = thunk {
    // only load if not already being loaded and if a different post was selected
    if (loadingJob != null && state.id != post.id) {

        // we cancel a previous loading job before starting a new one from the Bloc's CoroutineScope 
        // -> it's also cancelled when the Bloc is stopped
        launch(JobConfig(true)) {
            load(post)
        }

    }
}
```
### Flavor 2: No JobConfig

If a coroutine needs to be launched regardless whether there's already a job running for the same action, just do:
```
fun onSelected(post: Post) = thunk {
    // only load if not already being loaded and if a different post was selected
    if (loadingJob != null && state.id != post.id) {
        
        launch {
            load(post)
        }
        
    }
}
```
:::tip
The `CoroutineScope` could be exposed through the context (InitializerContext, ThunkContext, ReducerContext) in order to facilitate the launch of new coroutines. However I decided to encapsulate that scope to prevent "unauthorized interventions" (like cancellations). This design decision could be changed in the future.
:::