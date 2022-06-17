import SwiftUI
import blocSamples

let grayColor = Color(red: 0.95, green: 0.95, blue: 0.95, opacity: 1.0)

struct CalculatorView: View {
    private let holder = BlocHolder { CalculatorKt.bloc(context: $0) }
    
    @ObservedObject
    private var model: BlocObserver<CalculatorState, CalculatorAction, KotlinUnit>

    @ObservedObject
    private var orientation = InterfaceOrientation()

    init() {
        self.model = BlocObserver(self.holder.value)
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
            })
        } else {
            return AnyView(HStack(spacing: 8) {
                Text("\(model.value)")
                    .font(.system(size: 48, weight: .light, design: .default))
                    .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .trailing)
                    .background(grayColor)
                    .padding()

                KeypadView(holder, 55.0)
            })
        }
    }
    
}

class CalculatorView_Previews: PreviewProvider {
    static var previews: some View {
        CalculatorView()
    }
}

