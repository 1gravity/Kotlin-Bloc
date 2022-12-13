import SwiftUI
import blocSamples

struct PostListView: View {
     private let component = BlocComponent<PostsComponent> {
         PostsComponentIOS.shared.postsComponent(context: $0)
     }

    @ObservedObject
    private var model: BlocObserver<PostsRootState, PostsAction, KotlinUnit>

    @ObservedObject
    private var postObservable: PostObservable

    init() {
        model = BlocObserver(component.value.bloc)
        postObservable = PostObservable(component)
    }

    var body: some View {
        if model.value.postsState.loading {
            return AnyView(ActivityIndicator().frame(width: 50, height: 50).foregroundColor(.orange))
        } else if let posts = model.value.postsState.posts.component1() as? [Post] {
            return AnyView(postListView(posts))
        } else {
            return AnyView(Text(model.value.postsState.posts.component2()?.description() ?? "Error"))
        }
    }

    private func postListView(_ posts: [Post]) -> some View {
        List(posts, id: \.self.id) { post in
            VStack {
                NavigationLink(
                    destination: NavigationLazyView(
                        PostView(component)
                            .navigationBarTitleDisplayMode(.inline)
                            .navigationBarHidden(false)
                            .navigationBarBackButtonHidden(false)
                    ),
                    tag: post.id,
                    selection: $postObservable.postId
                ) {
                    PostItemView(post: post, onClick: {
                        component.value.onSelected(post: post)
                    })
                }
            }
        }
    }

}


class PostListView_Previews: PreviewProvider {
    static var previews: some View {
        PostListView()
    }
}
