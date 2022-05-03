//
//  MainMenuView.swift
//  iosApp
//
//  Created by Emanuel Moecklin on 5/2/22.
//  Copyright © 2022 1gravity. All rights reserved.
//

import SwiftUI
import blocSamples

struct MainMenuView: View {
    let mainMenu: MainMenu4iOS
    
//    @ObservedObject
//    private var routerState: ObservableValue<RouterState<AnyObject, CounterRootChild>>

//    init(_ holder: BlocHolder<MainMenu.ActionState>) {
//        self.bloc = holder.bloc
//        self.routerState = ObservableValue(counterRoot.routerState)
//    }

    var body: some View {
//        let activeChild = self.routerState.value.activeChild.instance
        
//        let observer: FlowObserver<Int> = { value in
//            print(value)
//        }

//        let v: MainMenu.ActionState = mainMenu.value
//        mainMenu.collect(collector: observer,
//                         completionHandler: { (value, error) in
//            
//        })

    return List {
        Text("Hello World \(mainMenu.value)")
        Text("Hello World 2")
        Text("Hello World 3")
    }
    
//        return VStack(spacing: 8) {
//            CounterView(self.counterRoot.counter)
//
//            Button(action: self.counterRoot.onNextChild, label: { Text("Next Child") })
//
//            Button(action: self.counterRoot.onPrevChild, label: { Text("Prev Child") })
//                .disabled(!activeChild.isBackEnabled)
//
//            CounterInnerView(activeChild.inner)
//        }
    }
}

//struct CounterRootView_Previews: PreviewProvider {
//    static var previews: some View {
//        CounterRootView(CoutnerRootPreview())
//    }
//
//    class CoutnerRootPreview : CounterRoot {
//        let counter: Counter = CounterView_Previews.CounterPreview()
//
//        let routerState: Value<RouterState<AnyObject, CounterRootChild>> = simpleRouterState(
//            CounterRootChild(
//                inner: CounterInnerView_Previews.CounterInnerPreview(),
//                isBackEnabled: true
//            )
//        )
//
//        func onNextChild() {
//        }
//
//        func onPrevChild() {
//        }
//    }
//}
