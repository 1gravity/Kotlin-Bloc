//
//  PostObservable.swift
//  iosApp
//
//  Created by Emanuel Moecklin on 5/24/22.
//  Copyright © 2022 1gravity. All rights reserved.
//

import SwiftUI
import blocSamples

// we use the PostObservable to observe the selected post for the NavigationLink / selection
class PostObservable : ObservableObject {
    @Published
    var postId: Int32? = nil

    private var postIdInternal: Int32? = nil

    init(_ component: BlocComponent<PostsComponent>) {
        component.value.bloc.observe(
            lifecycle: component.lifecycle,
            state: { value in
                let selectedId = value.selectedPost()?.int32Value
                if self.postIdInternal != selectedId {
                    self.postIdInternal = selectedId
                    self.postId = selectedId
                }
            },
            sideEffect: nil
        )
    }
}
