//
//  BlocHolder.swift
//  iosApp
//
//  Created by Emanuel Moecklin on 5/6/22.
//  Copyright © 2022 1gravity. All rights reserved.
//

import Foundation
import blocSamples

class BlocHolder<State: AnyObject, Action: AnyObject, SideEffect: AnyObject> {
    let lifecycle: LifecycleRegistry
    let bloc: Bloc<State, Action, SideEffect>

    init(factory: (BlocContext) -> Bloc<State, Action, SideEffect>) {
        lifecycle = LifecycleRegistryKt.LifecycleRegistry()
        let context = BlocContextImpl.init(lifecycle: lifecycle)
        bloc = factory(context)
        lifecycle.onCreate()
    }

    deinit {
        lifecycle.onDestroy()
    }
}
