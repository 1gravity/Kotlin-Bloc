import UIKit
import SwiftUI
import blocSamples

struct ContentView: View {

    @SwiftUI.State
    private var holder = BlocHolder { context in
		MainMenu.shared.bloc(context: context)
    }
    
	var body: some View {
        MainMenuView(mainMenu: holder.mainMenu)
            .onAppear { LifecycleRegistryExtKt.resume(holder.lifecycle) }
            .onDisappear { LifecycleRegistryExtKt.stop(holder.lifecycle) }
    }
}

class BlocHolder<T : Bloc> {
	let lifecycle: LifecycleRegistry
    let mainMenu: MainMenu4iOS

	init(factory: (BlocContext) -> T) {
        lifecycle = LifecycleRegistryKt.LifecycleRegistry()
        let context = DefaultBlocContext.init(lifecycle: lifecycle, stateKeeper: nil, instanceKeeper: nil, backPressedHandler: nil)
        let bloc = MainMenu.shared.bloc(context: context)

        IOSExtensionsKt.subscribeIOS(bloc, lifecycle: lifecycle, state: { state in
            print("state: \(state)")
        }, sideEffect: { sideEffect in
            print("sideEffect: \(sideEffect)")
        })
        
        bloc.send(value: MainMenu.ActionState.books)

        mainMenu = MainMenu4iOS.init(context: context)
        IOSExtensionsKt.subscribeIOS(mainMenu, lifecycle: lifecycle, state: { state in
            print("mainMenu.state: \(state)")
        }, sideEffect: { sideEffect in
            print("mainMenu.sideEffect: \(sideEffect)")
        })
        mainMenu.send(value: MainMenu.ActionState.calculator)


        lifecycle.onCreate()
	}

	deinit {
		lifecycle.onDestroy()
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
