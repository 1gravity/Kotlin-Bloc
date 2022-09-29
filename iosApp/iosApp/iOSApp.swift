import SwiftUI
import blocSamples

@main
struct iOSApp: App {
    init() {
        // two ways to use the logger, without DI
        LoggerUtilsKt.logger.i(msg: "iOS App started")

        // and with DI
        KoinKt.doInitKoin(koinAppDeclaration: { _ in })
        let logger = LoggeriOS.shared.logger()
        logger.i(msg: "iOS App injected")

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
