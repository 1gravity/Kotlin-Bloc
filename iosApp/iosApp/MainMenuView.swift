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
    let holder: BlocHolder<MainMenu.ActionState, MainMenu.ActionState, MainMenu.ActionState>
    
    @ObservedObject
    private var model: BlocObserver<MainMenu.ActionState, MainMenu.ActionState, MainMenu.ActionState>

    init(_ holder: BlocHolder<MainMenu.ActionState, MainMenu.ActionState, MainMenu.ActionState>) {
        self.holder = holder
        self.model = BlocObserver(holder)
    }

    var body: some View {
        func send(_ state: MainMenu.ActionState) -> () -> () {
            return { holder.bloc.send(value: state) }
        }
        
        return VStack(spacing: 8) {
            Text("\(NSLocalizedString("main_menu_title", comment: "Main Menu"))").padding()

            Button(action: send(MainMenu.ActionState.counter), label: { Text("\(NSLocalizedString("main_menu_counter", comment: "Counter"))") })
                .padding()
                .fixedSize()
                .border(Color.black, width: 2)

            Button(action: send(MainMenu.ActionState.calculator), label: { Text("\(NSLocalizedString("main_menu_calculator", comment: "Calculator"))") })
                .padding()
                .fixedSize()
                .border(Color.black, width: 2)

            Button(action: send(MainMenu.ActionState.posts), label: { Text("\(NSLocalizedString("main_menu_posts", comment: "Posts"))") })
                .padding()
                .fixedSize()
                .border(Color.black, width: 2)

        }
        .onAppear { holder.lifecycle.onStart() }
        .onDisappear { holder.lifecycle.onStop() }
    }
    
}

class MainMenuView_Previews: PreviewProvider {
    static var previews: some View {
        let holder = BlocHolder { MainMenu.shared.bloc(context: $0) }
        MainMenuView(holder)
    }
}
