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
    let holder: BlocHolder<KotlinInt, SimpleCounter.Action, KotlinUnit>
    
    @ObservedObject
    private var model: BlocObserver<KotlinInt, SimpleCounter.Action, KotlinUnit>

    init() {
        let holder = BlocHolder { SimpleCounter.shared.bloc(context: $0) }
        self.holder = holder
        self.model = BlocObserver(holder)
    }

    var body: some View {
        return VStack(spacing: 8) {
            Text("\(NSLocalizedString("counter_value", comment: "Counter")) \(model.value)").padding()
            
            Button(
                action: { holder.bloc.send(value: SimpleCounter.ActionIncrement(value: 1)) },
                label: { Text("\(NSLocalizedString("counter_increment", comment: "Increment"))") }
            )
            .padding()
            .fixedSize()
            .border(Color.black, width: 2)

            Button(action: {
                holder.bloc.send(value: SimpleCounter.ActionDecrement(value: 1)) },
                   label: { Text("\(NSLocalizedString("counter_decrement", comment: "Decrement"))") }
            )
            .padding()
            .fixedSize()
            .border(Color.black, width: 2)
        }
        .onAppear { holder.lifecycle.onStart() }
        .onDisappear { holder.lifecycle.onStop() }
    }
    
}

class CounterView_Previews: PreviewProvider {
    static var previews: some View {
        CounterView()
    }
}
