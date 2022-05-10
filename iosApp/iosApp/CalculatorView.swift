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
        let test: CalculatorState = model.value
    }
    
    var body: some View {
        return VStack(spacing: 8) {
            Text("value: \(model.value)").padding()

            HStack(spacing: 8) {
                Button(action: {
                    holder.bloc.send(value: CalculatorAction.Digit(digit: 7))
                }) {
                    Text("7").textStyle(ButtonStyle())
                }
                Button(action: {
                    holder.bloc.send(value: CalculatorAction.Digit(digit: 8))
                }) {
                    Text("8").textStyle(ButtonStyle())
                }
                Button(action: {
                    holder.bloc.send(value: CalculatorAction.Digit(digit: 9))
                }) {
                    Text("9").textStyle(ButtonStyle())
                }
            }

            HStack(spacing: 8) {
                Button(action: {
                    holder.bloc.send(value: CalculatorAction.Digit(digit: 4))
                }) {
                    Text("4").textStyle(ButtonStyle())
                }
                Button(action: {
                    holder.bloc.send(value: CalculatorAction.Digit(digit: 5))
                }) {
                    Text("5").textStyle(ButtonStyle())
                }
                Button(action: {
                    holder.bloc.send(value: CalculatorAction.Digit(digit: 6))
                }) {
                    Text("6").textStyle(ButtonStyle())
                }
            }

            HStack(spacing: 8) {
                Button(action: {
                    holder.bloc.send(value: CalculatorAction.Digit(digit: 1))
                }) {
                    Text("1").textStyle(ButtonStyle())
                }
                Button(action: {
                    holder.bloc.send(value: CalculatorAction.Digit(digit: 2))
                }) {
                    Text("2").textStyle(ButtonStyle())
                }
                Button(action: {
                    holder.bloc.send(value: CalculatorAction.Digit(digit: 3))
                }) {
                    Text("3").textStyle(ButtonStyle())
                }
            }
        }
        .onAppear { holder.lifecycle.onStart() }
        .onDisappear { holder.lifecycle.onStop() }
    }
    
}

extension Text {
    func textStyle<Style: ViewModifier>(_ style: Style) -> some View {
        ModifiedContent(content: self, modifier: style)
    }
}

struct ButtonStyle: ViewModifier {
    func body(content: Content) -> some View {
        content
            .frame(width: 80, height: 80)
            .foregroundColor(Color.black)
            .background(Color(red: 0.9, green: 0.9, blue: 0.9, opacity: 1.0))
            .clipShape(Circle())
            .font(.largeTitle)
    }
}

class CalculatorView_Previews: PreviewProvider {
    static var previews: some View {
        CalculatorView()
    }
}
