//
//  PostsView.swift
//  iosApp
//
//  Created by Emanuel Moecklin on 5/6/22.
//  Copyright © 2022 1gravity. All rights reserved.
//

import SwiftUI
import blocSamples

struct RootView: View {
    
    let holder: BlocHolder<PostsRootState, PostsAction, KotlinUnit>
    
    @ObservedObject
    private var model: BlocObserver<PostsRootState, PostsAction, KotlinUnit>

    init() {
        let holder = BlocHolder<PostsRootState, PostsAction, KotlinUnit> { PostsComponentImpl(context: $0).bloc }
        self.holder = holder
        self.model = BlocObserver(holder)
    }

    var body: some View {
        return Text("Posts WIP")
    }
    
}
