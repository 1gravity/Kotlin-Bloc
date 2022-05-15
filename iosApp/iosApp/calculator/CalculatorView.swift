//
//  CalculatorView.swift
//  iosApp
//
//  Created by Emanuel Moecklin on 5/6/22.
//  Copyright © 2022 1gravity. All rights reserved.
//

import SwiftUI
import blocSamples

let grayColor = Color(red: 0.95, green: 0.95, blue: 0.95, opacity: 1.0)

struct CalculatorView: View {
    private let holder: BlocHolder<CalculatorState, CalculatorAction, KotlinUnit>
    
    @ObservedObject
    private var model: BlocObserver<CalculatorState, CalculatorAction, KotlinUnit>

    @ObservedObject
    private var orientation = InterfaceOrientation()

    init() {
        let holder = BlocHolder { CalculatorKt.bloc(context: $0) }
        self.holder = holder
        self.model = BlocObserver(holder)
    }

    var body: some View {
        if orientation.orientation == InterfaceOrientation.Orientation.portrait {
            return AnyView(VStack(spacing: 8) {
                Text("\(model.value)")
                    .font(.system(size: 48, weight: .light, design: .default))
                    .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .trailing)
                    .background(grayColor)
                    .padding()

                KeypadView(holder, 80.0)
            }
            .onAppear { holder.lifecycle.onStart() }
            .onDisappear { holder.lifecycle.onStop() })
        } else {
            return AnyView(HStack(spacing: 8) {
                Text("\(model.value)")
                    .font(.system(size: 48, weight: .light, design: .default))
                    .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .trailing)
                    .background(grayColor)
                    .padding()

                KeypadView(holder, 55.0)
            }
            .onAppear { holder.lifecycle.onStart() }
            .onDisappear { holder.lifecycle.onStop() })
        }
    }
    
}

class CalculatorView_Previews: PreviewProvider {
    static var previews: some View {
        CalculatorView()
    }
}

