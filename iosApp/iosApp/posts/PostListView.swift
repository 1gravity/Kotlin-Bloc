//
//  PostsView.swift
//  iosApp
//
//  Created by Emanuel Moecklin on 5/20/22.
//  Copyright © 2022 1gravity. All rights reserved.
//

import SwiftUI
import blocSamples

struct PostListView: View {
    private let holder: BlocHolder<PostsRootState, PostsAction, KotlinUnit>

    @ObservedObject
    private var model: BlocObserver<PostsRootState, PostsAction, KotlinUnit>

    @ObservedObject
    private var postObservable: PostObservable

    init() {
        self.holder = BlocHolder<PostsRootState, PostsAction, KotlinUnit>(true) { PostsComponentImpl(context: $0).bloc }
        model = BlocObserver(self.holder)
        postObservable = PostObservable(self.holder)
    }

    var body: some View {
        if model.value.postsState.loading {
            return AnyView(ActivityIndicator().frame(width: 50, height: 50).foregroundColor(.orange))
        } else {
            let array: NSArray? = model.value.postsState.posts.component1()
            if let posts: [Post] = array as? [Post] {
                return AnyView(postListView(posts))
            } else {
                // todo implement error handling
                return AnyView(EmptyView())
            }
        }
    }

    private func postListView(_ posts: [Post]) -> some View {
        List(posts, id: \.self.id) { post in
            VStack {
                NavigationLink(
                    destination: NavigationLazyView(
                        PostView(holder)
                            .navigationBarTitleDisplayMode(.inline)
                            .navigationBarHidden(false)
                            .navigationBarBackButtonHidden(false)
                    ),
                    tag: post.id,
                    selection: $postObservable.postId
                ) {
                    PostItemView(post: post, onClick: {
                        holder.bloc.send(value: PostsAction.OnClicked(post: post))
                    })
                }
            }
        }
    }

}

private class PostObservable : ObservableObject {

    @Published
    var postId: Int32? = nil

    private var postIdTmp: Int32? = nil

    init(_ holder: BlocHolder<PostsRootState, PostsAction, KotlinUnit>) {
        holder.bloc.observe(
            lifecycle: holder.lifecycle,
            state: { value in
                if self.postIdTmp != value.selectedPost?.int32Value {
                    self.postId = value.selectedPost?.int32Value
                    self.postIdTmp = value.selectedPost?.int32Value
                }
            },
            sideEffect: nil
        )
    }

}

class PostView_Previews: PreviewProvider {
    static var previews: some View {
        PostListView()
    }
}

