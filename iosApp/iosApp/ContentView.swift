import UIKit
import SwiftUI
import blocSamples

struct ContentView: View {

    @SwiftUI.State
    private var holder = BlocHolder { MainMenu.shared.bloc(context: $0) }
    
	var body: some View {
        MainMenuView(holder)
            .onAppear { LifecycleRegistryExtKt.resume(holder.lifecycle) }
            .onDisappear { LifecycleRegistryExtKt.stop(holder.lifecycle) }
    }
}

class BlocHolder<State: AnyObject, Action: AnyObject, SideEffect: AnyObject> {
	let lifecycle: LifecycleRegistry
    let bloc: Bloc<State, Action, SideEffect>

    private var observer: ((AnyObject) -> Void)?

	init(factory: (BlocContext) -> Bloc<State, Action, SideEffect>) {
        lifecycle = LifecycleRegistryKt.LifecycleRegistry()
        let context = DefaultBlocContext.init(lifecycle: lifecycle, stateKeeper: nil, instanceKeeper: nil, backPressedHandler: nil)
        bloc = factory(context)
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
