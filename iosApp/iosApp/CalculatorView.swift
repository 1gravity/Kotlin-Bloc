//
//  CalculatorView.swift
//  iosApp
//
//  Created by Emanuel Moecklin on 5/6/22.
//  Copyright © 2022 1gravity. All rights reserved.
//

import SwiftUI
import blocSamples

struct CalculatorView: View {
    let holder: BlocHolder<CalculatorState, CalculatorAction, KotlinUnit>
    
    @ObservedObject
    private var model: BlocObserver<CalculatorState, CalculatorAction, KotlinUnit>

    init() {
        let holder = BlocHolder { CalculatorKt.bloc(context: $0) }
        self.holder = holder
        self.model = BlocObserver(holder)
    }
    
    var body: some View {
        return Text("Calculator WIP")
    }
    
}
