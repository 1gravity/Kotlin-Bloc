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
    
    private let manageFullLifecycle: Bool

    init(_ manageFullLifecycle: Bool = true, factory: (BlocContext) -> Bloc<State, Action, SideEffect>) {
        lifecycle = LifecycleRegistryKt.LifecycleRegistry()
        let context = BlocContextImpl.init(lifecycle: lifecycle)
        bloc = factory(context)
        self.manageFullLifecycle = manageFullLifecycle
        
        lifecycle.onCreate()
        if manageFullLifecycle {
            lifecycle.onStart()
        }
    }

    deinit {
        if manageFullLifecycle {
            lifecycle.onStop()
        }
        lifecycle.onDestroy()
    }
}

// todo make BlocOwner an abstract class
//class ComponentHolder<State: AnyObject, Action: AnyObject, SideEffect: AnyObject, Proposal: AnyObject, Component: BlocOwner<State, Action, SideEffect, SideEffect>> {
//    let lifecycle: LifecycleRegistry
//    let component: Component
//
//    init(factory: (BlocContext) -> Component) {
//        lifecycle = LifecycleRegistryKt.LifecycleRegistry()
//        let context = BlocContextImpl.init(lifecycle: lifecycle)
//        component = factory(context)
//        lifecycle.onCreate()
//    }
//
//    deinit {
//        lifecycle.onDestroy()
//    }
//}
