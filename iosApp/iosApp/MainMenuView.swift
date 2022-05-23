import UIKit
import SwiftUI
import blocSamples

struct MainMenuView: View {

    private var holder: BlocHolder<MainMenu.ActionState, MainMenu.ActionState, MainMenu.ActionState>

    @ObservedObject
    private var model: BlocObserver<MainMenu.ActionState, MainMenu.ActionState, MainMenu.ActionState>

    init() {
        self.holder = BlocHolder { MainMenu.shared.bloc(context: $0) }
        self.model = BlocObserver(self.holder)
    }

    private let counterView: () -> AnyView = {
        AnyView(CounterView()
            .navigationTitle(NSLocalizedString("main_menu_counter", comment: "Counter"))
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarHidden(false)
            .navigationBarBackButtonHidden(false))
    }
    
    private let calculatorView: () -> AnyView = {
        AnyView(CalculatorView()
            .navigationTitle(NSLocalizedString("main_menu_calculator", comment: "Calculator"))
            .navigationBarTitleDisplayMode(.inline))
    }

    private let postsView: () -> AnyView = {
        AnyView(PostListView()
            .navigationTitle(NSLocalizedString("main_menu_posts", comment: "Posts"))
            .navigationBarTitleDisplayMode(.inline))
    }

    var body: some View {
        func send(_ state: MainMenu.ActionState) -> () -> () {
            return { holder.bloc.send(value: state) }
        }

        return NavigationView {
            VStack {
                NavigationLink(destination: NavigationLazyView(counterView()), tag: MainMenu.ActionState.counter, selection: $model.sideEffect) { EmptyView() }
                NavigationLink(destination: NavigationLazyView(calculatorView()), tag: MainMenu.ActionState.calculator, selection: $model.sideEffect) { EmptyView() }
                NavigationLink(destination: NavigationLazyView(postsView()), tag: MainMenu.ActionState.posts, selection: $model.sideEffect) { EmptyView() }

                Button(action: send(MainMenu.ActionState.counter), label: { Text("\(NSLocalizedString("main_menu_counter", comment: "Counter"))") }
                )
                .tint(.blue)
                .controlSize(.large) // .large, .medium or .small
                .buttonStyle(.borderedProminent)

                Button(action: send(MainMenu.ActionState.calculator), label: { Text("\(NSLocalizedString("main_menu_calculator", comment: "Calculator"))") }
                )
                .tint(.blue)
                .controlSize(.large) // .large, .medium or .small
                .buttonStyle(.borderedProminent)


                Button(action: send(MainMenu.ActionState.posts), label: { Text("\(NSLocalizedString("main_menu_posts", comment: "Posts"))") }
                )
                .tint(.blue)
                .controlSize(.large) // .large, .medium or .small
                .buttonStyle(.borderedProminent)

            }
            .navigationTitle(NSLocalizedString("app_name", comment: "BLoC Framework"))
        }

    }
}

class ContentView_Previews: PreviewProvider {
    static var previews: some View {
        MainMenuView()
    }

    #if DEBUG
    @objc class func injected() {
        let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene
        windowScene?.windows.first?.rootViewController =
                UIHostingController(rootView: ContentView_Previews.previews)
    }
    #endif
}
