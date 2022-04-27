import UIKit
import SwiftUI
import blocCore
import blocSamples

struct ContentView: View {
	init() {
//		MainMenuCompose.bloc
	}
	var body: some View {
		List {
			Text("Hello World 1")
			Text("Hello World 2")
		}
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
