//
//  CounterView.swift
//  iosApp
//
//  Created by Emanuel Moecklin on 5/6/22.
//  Copyright © 2022 1gravity. All rights reserved.
//

import SwiftUI
import blocSamples

struct CounterView: View {
    let bloc: Bloc<MainMenu.ActionState, MainMenu.ActionState, MainMenu.ActionState>
    
    @ObservedObject
    private var model: ObservableValue<MainMenu.ActionState, MainMenu.ActionState, MainMenu.ActionState>

    init(_ holder: BlocHolder<MainMenu.ActionState, MainMenu.ActionState, MainMenu.ActionState>) {
        self.bloc = holder.bloc
        self.model = ObservableValue(holder)
    }

    var body: some View {
        func send(_ state: MainMenu.ActionState) -> () -> () {
            return { self.bloc.send(value: state) }
        }
        
        return VStack(spacing: 8) {
            Text("\(NSLocalizedString("counter_value", comment: "Main Menu"))").padding()

            Button(action: send(MainMenu.ActionState.calculator), label: { Text("\(NSLocalizedString("counter_increment", comment: "Increment"))") })
                .padding()
                .fixedSize()
                .border(Color.black, width: 2)

            Button(action: send(MainMenu.ActionState.posts), label: { Text("\(NSLocalizedString("counter_decrement", comment: "Decrement"))") })
                .padding()
                .fixedSize()
                .border(Color.black, width: 2)
        }
    }
    
}
