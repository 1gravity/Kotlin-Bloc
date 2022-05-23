//
//  PostView.swift
//  iosApp
//
//  Created by Emanuel Moecklin on 5/20/22.
//  Copyright © 2022 1gravity. All rights reserved.
//

import SwiftUI
import blocSamples

struct PostView: View {
    private let holder: BlocHolder<PostsRootState, PostsAction, KotlinUnit>

    @ObservedObject
    private var model: BlocObserver<PostsRootState, PostsAction, KotlinUnit>

    init(_ holder: BlocHolder<PostsRootState, PostsAction, KotlinUnit>) {
        self.holder = holder
        model = BlocObserver(holder)
    }

    var body: some View {
        if model.value.postsState.loading {
            return AnyView(ActivityIndicator().frame(width: 50, height: 50).foregroundColor(.orange))
        } else {
            if let post: Post = model.value.postState.post?.component1() {
                return AnyView(PostHeader(post))
            } else {
                // todo handle errors
                return AnyView(EmptyView())
            }
        }
    }
    
}

struct PostHeader: View {
    private let post: Post

    init(_ post: Post) {
        self.post = post
    }

    var body: some View {
        AnyView(
            VStack {
                Text(post.title)
                    .font(.system(size: 32.0))
                    .frame(maxWidth: .infinity, alignment: .leading)
                Text(post.username)
                    .font(.system(size: 18.0))
                    .frame(maxWidth: .infinity, alignment: .leading)
                Text(post.body)
                    .font(.system(size: 18.0))
                    .frame(maxWidth: .infinity, alignment: .leading)
                
                Divider()
                
                Comments(post)
            }
            .padding()
            .navigationTitle(post.username)
            .navigationBarTitleDisplayMode(.inline)
        )
    }
}

struct Comments: View {
    private let post: Post

    init(_ post: Post) {
        self.post = post
    }

    var body: some View {
        VStack {
            let title = NSLocalizedString("posts_compose_comments", comment: "comments")
            Text(String(format: title, post.comments.count))
                .font(.system(size: 16.0))
                .frame(maxWidth: .infinity, alignment: .leading)
            List(post.comments, id: \.self.id) { comment in
                VStack {
                    Text(comment.name.uppercased())
                        .font(.system(size: 12.0))
                        .frame(maxWidth: .infinity, alignment: .leading)
                    Text(comment.email)
                        .font(.system(size: 10.0))
                        .frame(maxWidth: .infinity, alignment: .leading)
                    Text(comment.body)
                        .font(.system(size: 14.0))
                        .frame(maxWidth: .infinity, alignment: .leading)
                }.listRowInsets(EdgeInsets(top: 8.0, leading: 0.0, bottom: 8.0, trailing: 0.0))
            }.listStyle(PlainListStyle())
        }
    }
    
}
