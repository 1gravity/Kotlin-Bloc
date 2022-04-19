import SwiftUI
import blocCore
import blocRedux
import blocSamples

@main
struct iOSApp: App {
    init() {
		UtilsKt.logger.i(msg: "iOS App started")
		KoinKt.doInitKoin(koinAppDeclaration: { _ in })
		UtilsKt.logger.i(msg: "iOS App injected")
    }
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
