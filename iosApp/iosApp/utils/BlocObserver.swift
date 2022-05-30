//
//  ObservableValue.swift
//  iosApp
//
//  Created by Emanuel Moecklin on 5/3/22.
//  Copyright © 2022 1gravity. All rights reserved.
//

import blocSamples

public class BlocObserver<State: AnyObject, Action: AnyObject, SideEffect: AnyObject> : ObservableObject {

    @Published
    var value: State
    
    @Published
    var sideEffect: SideEffect?

    private var lifecycle = LifecycleRegistryKt.LifecycleRegistry()
    
    init(_ bloc: Bloc<State, Action, SideEffect>) {
        self.value = bloc.value

        bloc.observe(
            observerLifecycle: self.lifecycle,
            state: { value in
                self.value = value
            },
            sideEffect: { sideEffect in
                self.sideEffect = sideEffect
            }
        )
        
        lifecycle.onCreate()
        lifecycle.onStart()
   }

    deinit {
        self.lifecycle.onStop()
        self.lifecycle.onDestroy()
    }

}
