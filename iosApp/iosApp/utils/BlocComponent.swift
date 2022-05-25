//
//  BlocOwnerHolder.swift
//  iosApp
//
//  Created by Emanuel Moecklin on 5/24/22.
//  Copyright © 2022 1gravity. All rights reserved.
//

import blocSamples

class BlocComponent<Component: AnyObject> {
    let lifecycle: LifecycleRegistry
    let value: Component

    private let manageFullLifecycle: Bool

    init(_ manageFullLifecycle: Bool = true, factory: (BlocContext) -> Component) {
        self.lifecycle = LifecycleRegistryKt.LifecycleRegistry()
        let context = BlocContextImpl.init(lifecycle: lifecycle)
        self.value = factory(context)
        self.manageFullLifecycle = manageFullLifecycle
        
        lifecycle.onCreate()
        if manageFullLifecycle {
            lifecycle.onStart()
        }
    }

    deinit {
        print("deinit")
        print("deinit")
        print("deinit")
        print("deinit")
        print("deinit")
        print("deinit")
        print("deinit")
        if manageFullLifecycle {
            lifecycle.onStop()
        }
        lifecycle.onDestroy()
    }
}
