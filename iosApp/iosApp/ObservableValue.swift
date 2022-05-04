//
//  ObservableValue.swift
//  iosApp
//
//  Created by Emanuel Moecklin on 5/3/22.
//  Copyright © 2022 1gravity. All rights reserved.
//

import SwiftUI
import blocSamples

class Collector<T : Any>: ObservableObject, FlowCollector {
    @Published var currentValue = ""

    func emit(value: Any?, completionHandler: @escaping (KotlinUnit?, Error?) -> Void) {
        print("ios received " + (value as! String))
        currentValue = value as! String
        completionHandler(KotlinUnit(), nil)
    }
}


public class ObservableValue<State: AnyObject, Action: AnyObject, SideEffect: AnyObject> : ObservableObject {

    @Published
    var value: State

    init(_ holder: BlocHolder<State, Action, SideEffect>) {
        self.value = holder.bloc.value

        let observer: ((State) -> Void) = { value in
            self.value = value
            print("state: \(value)")
        }
        
        holder.bloc.observe(
            lifecycle: holder.lifecycle,
            state: observer,
            sideEffect: { sideEffect in
                print("sideEffect: \(sideEffect)")
            }
        )

    }

}
