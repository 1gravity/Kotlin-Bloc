import UIKit
import SwiftUI
import blocCore
import blocRedux
import blocSamples

struct ContentView: View {
//	@State
//	private var componentHolder = ComponentHolder(factory: CounterRootComponent.init)

	init() {
		MainMenuCompose()
//		MainMenuCompose().bloc(context: <#T##BlocCoreBlocContext#>)
		var test = MainMenuCompose.init
		test()
	}
	var body: some View {
		List {
			Text("Hello World 1")
			Text("Hello World 2")
			Text("Hello World 3")
		}
	}
}

//class ComponentHolder<T> {
//	let lifecycle: LifecycleRegistry
//	let bloc: T
//
//	init(factory: (ComponentContext) -> T) {
//		let lifecycle = LifeC.LifecycleRegistry()
//		let component = factory(DefaultComponentContext(lifecycle: lifecycle))
//		self.lifecycle = lifecycle
//		self.component = component
//
//		lifecycle.onCreate()
//	}
//
//	deinit {
//		lifecycle.onDestroy()
//	}
//}

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
