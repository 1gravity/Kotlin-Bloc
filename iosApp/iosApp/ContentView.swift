import UIKit
import SwiftUI
import blocSamples

struct ContentView: View {

    @SwiftUI.State
    private var holder = BlocHolder { context in
		MainMenu.shared.bloc(context: context)
    }
    
	var body: some View {
        MainMenuView(holder)
            .onAppear { LifecycleRegistryExtKt.resume(holder.lifecycle) }
            .onDisappear { LifecycleRegistryExtKt.stop(holder.lifecycle) }
    }
}

class Collector: ObservableObject, FlowCollector {
    @Published var currentValue = ""

    func emit(value: Any?, completionHandler: @escaping (KotlinUnit?, Error?) -> Void) {
        print("ios received " + (value as! String))
        currentValue = value as! String
        completionHandler(KotlinUnit(), nil)
    }
}

class BlocHolder<T : Bloc<MainMenu.ActionState, MainMenu.ActionState, MainMenu.ActionState>> {
	let lifecycle: LifecycleRegistry
    let bloc: Bloc<MainMenu.ActionState, MainMenu.ActionState, MainMenu.ActionState>

    private var observer: ((AnyObject) -> Void)?

	init(factory: (BlocContext) -> T) {
        lifecycle = LifecycleRegistryKt.LifecycleRegistry()
        let context = DefaultBlocContext.init(lifecycle: lifecycle, stateKeeper: nil, instanceKeeper: nil, backPressedHandler: nil)
        bloc = MainMenu.shared.bloc(context: context)

        bloc.send(value: MainMenu.ActionState.calculator)

        lifecycle.onCreate()
        
        
        let test: Test<NSString, NSString> = TestImpl(initialValue: "Emanuel")
        test.flow().collect(collector: Collector()) { (result: KotlinUnit?, error: Error?) in
            if (error != nil) {
                print("error \(error)")
            } else {
                print("Completion")
            }
        }
        test.send(action: "Hello")
        test.send(action: "World")
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
