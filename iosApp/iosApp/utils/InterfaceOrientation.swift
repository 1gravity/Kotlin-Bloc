import Combine
import SwiftUI

final class InterfaceOrientation: ObservableObject {
    enum Orientation {
        case portrait
        case landscape
    }
    
    @Published var orientation: Orientation
    
    private var listener: AnyCancellable?
    
    init() {
        let isLandscape: () -> Bool = {
            // unfortunately this doesn't work properly if the interface orientation is portraitUpsideDown, it still returns landscapeLeft
            // but since this is a demo app only, we just let this slide
            let orientation: UIInterfaceOrientation? = UIApplication.shared.windows.first?.windowScene?.interfaceOrientation
            return orientation == UIInterfaceOrientation.landscapeLeft || orientation == UIInterfaceOrientation.landscapeRight
        }
        
        orientation = isLandscape() ? .landscape : .portrait

        listener = NotificationCenter.default.publisher(for: UIDevice.orientationDidChangeNotification)
            .compactMap { _ in (UIApplication.shared.windows.first?.windowScene?.interfaceOrientation) as UIInterfaceOrientation? }
            .compactMap { deviceOrientation -> Orientation? in
                return isLandscape() ? .landscape : .portrait
            }
            .assign(to: \.orientation, on: self)
    }
    
    deinit {
        listener?.cancel()
    }
}

