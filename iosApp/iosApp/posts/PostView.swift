//
//  Created by Emanuel Moecklin on 5/24/22.
//  Copyright © 2022 1gravity. All rights reserved.
//

import SwiftUI
import blocSamples

struct PostView: View {
    private let component: BlocComponent<PostsComponent>

    @ObservedObject
    private var model: BlocObserver<PostsRootState, PostsAction, KotlinUnit>

    init(_ component: BlocComponent<PostsComponent>) {
        self.component = component
        model = BlocObserver(component.value.bloc)
    }

    var body: some View {
        if model.value.postIsLoading() {
            return AnyView(ActivityIndicator().frame(width: 50, height: 50).foregroundColor(.orange))
        } else if let post: Post = model.value.postState.post?.component1() {
            return AnyView(
                PostDetailView(post).onDisappear { component.value.onClosed() }
            )
        } else {
            return AnyView(Text(model.value.postState.post?.component2()?.description() ?? "Error"))
        }
    }
    
}
