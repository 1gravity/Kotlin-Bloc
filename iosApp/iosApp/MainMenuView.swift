import UIKit
import SwiftUI
import blocSamples

struct MainMenuView: View {

    @SwiftUI.State
    private var holder: BlocHolder<MainMenu.ActionState, MainMenu.ActionState, MainMenu.ActionState>

    @ObservedObject
    private var model: BlocObserver<MainMenu.ActionState, MainMenu.ActionState, MainMenu.ActionState>

    init() {
        let holder = BlocHolder { MainMenu.shared.bloc(context: $0) }
        self.holder = holder
        self.model = BlocObserver(holder)
    }

    var body: some View {
        func send(_ state: MainMenu.ActionState) -> () -> () {
            return { holder.bloc.send(value: state) }
        }

        let counterView = CounterView()
            .navigationTitle(NSLocalizedString("main_menu_counter", comment: "Counter"))
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarHidden(false)
            .navigationBarBackButtonHidden(false)

        let calculatorView = CalculatorView()
            .navigationTitle(NSLocalizedString("main_menu_calculator", comment: "Calculator"))
            .navigationBarTitleDisplayMode(.inline)

        let postsView = PostsView()

        return NavigationView {
            VStack {
                NavigationLink(destination: counterView, tag: MainMenu.ActionState.counter, selection: $model.sideEffect) { EmptyView() }
                NavigationLink(destination: calculatorView, tag: MainMenu.ActionState.calculator, selection: $model.sideEffect) { EmptyView() }
                NavigationLink(destination: postsView, tag: MainMenu.ActionState.posts, selection: $model.sideEffect) { EmptyView() }

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
            .navigationTitle(NSLocalizedString("app_name", comment: "BLoC Framework"))
        }
        .onAppear { holder.lifecycle.onStart() }
        .onDisappear { holder.lifecycle.onStop() }

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


