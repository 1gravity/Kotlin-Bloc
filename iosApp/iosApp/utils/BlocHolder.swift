//
//  Created by Emanuel Moecklin on 5/6/22.
//  Copyright © 2022 1gravity. All rights reserved.
//

import blocSamples

class BlocHolder<State: AnyObject, Action: AnyObject, SideEffect: AnyObject> {
    let lifecycle: LifecycleRegistry
    let value: Bloc<State, Action, SideEffect>
    
    private let manageFullLifecycle: Bool

    class BlocContextImpl : BlocContext {
        let lifecycle: Lifecycle
        
        init(lifecycle: Lifecycle) {
            self.lifecycle = lifecycle
        }
    }

    init(_ manageFullLifecycle: Bool = true, factory: (BlocContext) -> Bloc<State, Action, SideEffect>) {
        self.lifecycle = LifecycleRegistryKt.LifecycleRegistry()
        let context = BlocContextImpl(lifecycle: lifecycle)
        self.value = factory(context)
        self.manageFullLifecycle = manageFullLifecycle
        
        lifecycle.onCreate()
        if manageFullLifecycle {
            lifecycle.onStart()
            lifecycle.onResume()
        }
    }

    deinit {
        if manageFullLifecycle {
            lifecycle.onPause()
            lifecycle.onStop()
        }
        lifecycle.onDestroy()
    }
}
