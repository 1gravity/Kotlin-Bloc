import UIKit
import SwiftUI
import blocSamples

struct ContentView: View {

    @SwiftUI.State
    private var holder: BlocHolder<MainMenu.ActionState, MainMenu.ActionState, MainMenu.ActionState>

    @ObservedObject
    private var model: ObservableValue<MainMenu.ActionState, MainMenu.ActionState, MainMenu.ActionState>

    init() {
        let holder = BlocHolder { MainMenu.shared.bloc(context: $0) }
        self.holder = holder
        self.model = ObservableValue(holder)
    }

    var body: some View {
        let view: some View = ({
            switch model.sideEffect {
            case MainMenu.ActionState.counter:
                return AnyView(CounterView(holder)
                    .onAppear { holder.lifecycle.onStart() }
                    .onDisappear { holder.lifecycle.onStop() }
                )
            case MainMenu.ActionState.calculator:
                return AnyView(CalculatorView()
                    .onAppear { holder.lifecycle.onStart() }
                    .onDisappear { holder.lifecycle.onStop() }
                )
            case MainMenu.ActionState.posts:
                return AnyView(PostsView()
                    .onAppear { holder.lifecycle.onStart() }
                    .onDisappear { holder.lifecycle.onStop() }
                )
            default:
                return AnyView(MainMenuView(holder)
                    .onAppear { holder.lifecycle.onStart() }
                    .onDisappear { holder.lifecycle.onStop() }
                )
            }
        }() as AnyView)

        return view
    }
}

class ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }

    #if DEBUG
    @objc class func injected() {
        let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene
        windowScene?.windows.first?.rootViewController =
                UIHostingController(rootView: ContentView_Previews.previews)
    }
    #endif
}


