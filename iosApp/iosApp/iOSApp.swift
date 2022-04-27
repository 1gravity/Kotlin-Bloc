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

		#if DEBUG
		var injectionBundlePath = "/Applications/InjectionIII.app/Contents/Resources"
		#if targetEnvironment(macCatalyst)
		injectionBundlePath = "\(injectionBundlePath)/macOSInjection.bundle"
		#elseif os(iOS)
		injectionBundlePath = "\(injectionBundlePath)/iOSInjection.bundle"
		#endif
		Bundle(path: injectionBundlePath)?.load()
		#endif
    }
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
