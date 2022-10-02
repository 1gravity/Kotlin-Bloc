import SwiftUI
import blocSamples

@main
struct iOSApp: App {
    init() {
        // Dependency Injection
        KoinKt.doInitKoin(koinAppDeclaration: { _ in })

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
			MainMenuView()
		}
	}
}
