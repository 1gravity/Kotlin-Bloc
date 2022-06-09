//
//  Created by Emanuel Moecklin on 5/12/22.
//  Copyright © 2022 1gravity. All rights reserved.
//

import SwiftUI
import blocSamples

struct KeypadView: View {
    private let bloc: Bloc<CalculatorState, CalculatorAction, KotlinUnit>
    
    private var buttonSize: CGFloat

    @ObservedObject
    private var model: BlocObserver<CalculatorState, CalculatorAction, KotlinUnit>

    init(_ holder: BlocHolder<CalculatorState, CalculatorAction, KotlinUnit>, _ buttonSize: CGFloat = 80.0) {
        self.bloc = holder.value
        self.buttonSize = buttonSize
        self.model = BlocObserver(holder.value)
    }

    var body: some View {
        return VStack(spacing: 8) {
            
            HStack(spacing: 8) {
                Button(action: {
                    bloc.send(value: CalculatorAction.Clear())
                }) {
                    let text = NSLocalizedString("button_clear", comment: "Clear")
                    Text(text).textStyle(CalculatorButtonStyle(buttonSize: buttonSize))
                }
                Button(action: {
                    bloc.send(value: CalculatorAction.PlusMinus())
                }) {
                    let text = NSLocalizedString("button_plus_minus", comment: "PlusMinus")
                    Text(text).textStyle(CalculatorButtonStyle(buttonSize: buttonSize))
                }
                Button(action: {
                    bloc.send(value: CalculatorAction.Percentage())
                }) {
                    let text = NSLocalizedString("button_percentage", comment: "Percentage")
                    Text(text).textStyle(CalculatorButtonStyle(buttonSize: buttonSize))
                }
                Button(action: {
                    bloc.send(value: CalculatorAction.Divide())
                }) {
                    let text = NSLocalizedString("button_divide", comment: "Divide")
                    Text(text).textStyle(CalculatorButtonStyle(backgroundColor: Color(red:0.61, green:0.15, blue:0.69, opacity: 1.0), textColor: Color.white, buttonSize: buttonSize))
                }
            }

            HStack(spacing: 8) {
                Button(action: {
                    bloc.send(value: CalculatorAction.Digit(digit: 7))
                }) {
                    let text = NSLocalizedString("button_7", comment: "7")
                    Text(text).textStyle(CalculatorButtonStyle(buttonSize: buttonSize))
                }
                Button(action: {
                    bloc.send(value: CalculatorAction.Digit(digit: 8))
                }) {
                    let text = NSLocalizedString("button_8", comment: "8")
                    Text(text).textStyle(CalculatorButtonStyle(buttonSize: buttonSize))
                }
                Button(action: {
                    bloc.send(value: CalculatorAction.Digit(digit: 9))
                }) {
                    let text = NSLocalizedString("button_9", comment: "9")
                    Text(text).textStyle(CalculatorButtonStyle(buttonSize: buttonSize))
                }
                Button(action: {
                    bloc.send(value: CalculatorAction.Multiply())
                }) {
                    let text = NSLocalizedString("button_multiply", comment: "Multiply")
                    Text(text).textStyle(CalculatorButtonStyle(backgroundColor: Color(red:0.96, green:0.26, blue:0.21, opacity: 1.0), textColor: Color.white, buttonSize: buttonSize))
                }
            }

            HStack(spacing: 8) {
                Button(action: {
                    bloc.send(value: CalculatorAction.Digit(digit: 4))
                }) {
                    let text = NSLocalizedString("button_4", comment: "4")
                    Text(text).textStyle(CalculatorButtonStyle(buttonSize: buttonSize))
                }
                Button(action: {
                    bloc.send(value: CalculatorAction.Digit(digit: 5))
                }) {
                    let text = NSLocalizedString("button_5", comment: "5")
                    Text(text).textStyle(CalculatorButtonStyle(buttonSize: buttonSize))
                }
                Button(action: {
                    bloc.send(value: CalculatorAction.Digit(digit: 6))
                }) {
                    let text = NSLocalizedString("button_6", comment: "6")
                    Text(text).textStyle(CalculatorButtonStyle(buttonSize: buttonSize))
                }
                Button(action: {
                    bloc.send(value: CalculatorAction.Subtract())
                }) {
                    let text = NSLocalizedString("button_subtract", comment: "Subtract")
                    Text(text).textStyle(CalculatorButtonStyle(backgroundColor: Color(red:0.01, green:0.66, blue:0.96, opacity: 1.0), textColor: Color.white, buttonSize: buttonSize))
                }
            }

            HStack(spacing: 8) {
                Button(action: {
                    bloc.send(value: CalculatorAction.Digit(digit: 1))
                }) {
                    let text = NSLocalizedString("button_1", comment: "1")
                    Text(text).textStyle(CalculatorButtonStyle(buttonSize: buttonSize))
                }
                Button(action: {
                    bloc.send(value: CalculatorAction.Digit(digit: 2))
                }) {
                    let text = NSLocalizedString("button_2", comment: "2")
                    Text(text).textStyle(CalculatorButtonStyle(buttonSize: buttonSize))
                }
                Button(action: {
                    bloc.send(value: CalculatorAction.Digit(digit: 3))
                }) {
                    let text = NSLocalizedString("button_3", comment: "3")
                    Text(text).textStyle(CalculatorButtonStyle(buttonSize: buttonSize))
                }
                Button(action: {
                    bloc.send(value: CalculatorAction.Add())
                }) {
                    let text = NSLocalizedString("button_add", comment: "Add")
                    Text(text).textStyle(CalculatorButtonStyle(backgroundColor: Color(red:1.00, green:0.60, blue:0.00, opacity: 1.0), textColor: Color.white, buttonSize: buttonSize))
                }
            }
            
            HStack(spacing: 8) {
                Button(action: {
                    bloc.send(value: CalculatorAction.Digit(digit: 0))
                }) {
                    let text = NSLocalizedString("button_0", comment: "0")
                    Text(text).textStyle(CalculatorButtonStyle(buttonSize: buttonSize))
                }

                Spacer().frame(width: buttonSize)
                
                Button(action: {
                    bloc.send(value: CalculatorAction.Period())
                }) {
                    let text = NSLocalizedString("button_period", comment: "Period")
                    Text(text).textStyle(CalculatorButtonStyle(buttonSize: buttonSize))
                }
                Button(action: {
                    bloc.send(value: CalculatorAction.Equals())
                }) {
                    let text = NSLocalizedString("button_equals", comment: "Equals")
                    Text(text).textStyle(CalculatorButtonStyle(backgroundColor: Color(red:0.30, green:0.69, blue:0.31, opacity: 1.0), textColor: Color.white, buttonSize: buttonSize))
                }
            }
            
        }
    }
    
}
