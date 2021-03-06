import SwiftUI
import blocSamples

struct CounterView: View {
    private let holder = BlocHolder { SimpleCounter.shared.bloc(context: $0) }
    
    @ObservedObject
    private var model: BlocObserver<KotlinInt, SimpleCounter.Action, KotlinUnit>

    init() {
        model = BlocObserver(holder.value)
    }

    var body: some View {
        return VStack(spacing: 8) {
            Text("\(NSLocalizedString("counter_value", comment: "Counter")) \(model.value)").padding()
            
            Button(
                action: { holder.value.send(value: SimpleCounter.Increment(value: 1)) },
                label: { Text("\(NSLocalizedString("counter_increment", comment: "Increment"))") }
            )
            .tint(.blue)
            .controlSize(.large) // .large, .medium or .small
            .buttonStyle(.borderedProminent)

            Button(action: {
                holder.value.send(value: SimpleCounter.Decrement(value: 1)) },
                   label: { Text("\(NSLocalizedString("counter_decrement", comment: "Decrement"))") }
            )
            .tint(.red)
            .controlSize(.large) // .large, .medium or .small
            .buttonStyle(.borderedProminent)
        }
    }
    
}

class CounterView_Previews: PreviewProvider {
    static var previews: some View {
        CounterView()
    }
}
