//
//  Created by Emanuel Moecklin on 5/15/22.
//  Copyright © 2022 1gravity. All rights reserved.
//

import SwiftUI

struct NavigationLazyView<Content: View>: View {
    private let build: () -> Content

    init(_ build: @autoclosure @escaping () -> Content) {
        self.build = build
    }

    var body: Content {
        build()
    }
}
