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
    let bloc: Bloc<MainMenu.ActionState, MainMenu.ActionState, MainMenu.ActionState>
    
    @ObservedObject
    private var model: ObservableValue<MainMenu.ActionState, MainMenu.ActionState, MainMenu.ActionState>

    init(_ holder: BlocHolder<MainMenu.ActionState, MainMenu.ActionState, MainMenu.ActionState>) {
        self.bloc = holder.bloc
        self.model = ObservableValue(holder)
        
        bloc.send(value: MainMenu.ActionState.books)

        holder.bloc.observe(lifecycle: holder.lifecycle, state: { state in
            print("state: \(state)")
        }, sideEffect: { sideEffect in
            print("sideEffect: \(sideEffect)")
        })
    }

    var body: some View {
        func send(_ state: MainMenu.ActionState) -> () -> () {
            return { self.bloc.send(value: state) }
        }
        
        return VStack(spacing: 8) {
            Text("\(model.value)")
                .padding()
                .border(Color.black, width: 2)

            Button(action: send(MainMenu.ActionState.calculator), label: { Text("Calculator") })
            Button(action: send(MainMenu.ActionState.books), label: { Text("Books") })
        }
    }
    
}
