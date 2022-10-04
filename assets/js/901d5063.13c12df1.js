"use strict";(self.webpackChunkwebsite=self.webpackChunkwebsite||[]).push([[804],{3905:function(e,t,n){n.d(t,{Zo:function(){return p},kt:function(){return m}});var o=n(7294);function s(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function a(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);t&&(o=o.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,o)}return n}function i(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?a(Object(n),!0).forEach((function(t){s(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):a(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function r(e,t){if(null==e)return{};var n,o,s=function(e,t){if(null==e)return{};var n,o,s={},a=Object.keys(e);for(o=0;o<a.length;o++)n=a[o],t.indexOf(n)>=0||(s[n]=e[n]);return s}(e,t);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);for(o=0;o<a.length;o++)n=a[o],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(s[n]=e[n])}return s}var l=o.createContext({}),c=function(e){var t=o.useContext(l),n=t;return e&&(n="function"==typeof e?e(t):i(i({},t),e)),n},p=function(e){var t=c(e.components);return o.createElement(l.Provider,{value:t},e.children)},d={inlineCode:"code",wrapper:function(e){var t=e.children;return o.createElement(o.Fragment,{},t)}},u=o.forwardRef((function(e,t){var n=e.components,s=e.mdxType,a=e.originalType,l=e.parentName,p=r(e,["components","mdxType","originalType","parentName"]),u=c(n),m=s,f=u["".concat(l,".").concat(m)]||u[m]||d[m]||a;return n?o.createElement(f,i(i({ref:t},p),{},{components:n})):o.createElement(f,i({ref:t},p))}));function m(e,t){var n=arguments,s=t&&t.mdxType;if("string"==typeof e||s){var a=n.length,i=new Array(a);i[0]=u;var r={};for(var l in t)hasOwnProperty.call(t,l)&&(r[l]=t[l]);r.originalType=e,r.mdxType="string"==typeof e?e:s,i[1]=r;for(var c=2;c<a;c++)i[c]=n[c];return o.createElement.apply(null,i)}return o.createElement.apply(null,n)}u.displayName="MDXCreateElement"},9190:function(e,t,n){n.r(t),n.d(t,{assets:function(){return l},contentTitle:function(){return i},default:function(){return d},frontMatter:function(){return a},metadata:function(){return r},toc:function(){return c}});var o=n(3117),s=(n(7294),n(3905));const a={id:"posts",title:"Posts",sidebar_label:"Posts",hide_title:!0},i=void 0,r={unversionedId:"examples/posts",id:"examples/posts",title:"Posts",description:"Posts",source:"@site/docs/examples/posts.md",sourceDirName:"examples",slug:"/examples/posts",permalink:"/Kotlin-Bloc/docs/examples/posts",draft:!1,tags:[],version:"current",frontMatter:{id:"posts",title:"Posts",sidebar_label:"Posts",hide_title:!0},sidebar:"exampleSidebar",previous:{title:"Calculator",permalink:"/Kotlin-Bloc/docs/examples/calculator"},next:{title:"To Do",permalink:"/Kotlin-Bloc/docs/examples/todo"}},l={},c=[{value:"Posts",id:"posts",level:2},{value:"Posts 1",id:"posts-1",level:3},{value:"Android",id:"android",level:4},{value:"iOS",id:"ios",level:4},{value:"Posts 2",id:"posts-2",level:3},{value:"Android",id:"android-1",level:4},{value:"iOS",id:"ios-1",level:4}],p={toc:c};function d(e){let{components:t,...a}=e;return(0,s.kt)("wrapper",(0,o.Z)({},p,a,{components:t,mdxType:"MDXLayout"}),(0,s.kt)("h2",{id:"posts"},"Posts"),(0,s.kt)("p",null,(0,s.kt)("img",{alt:"Posts",src:n(8164).Z,width:"2544",height:"1804"})),(0,s.kt)("p",null,"The two posts sample apps are also adapted from ",(0,s.kt)("a",{parentName:"p",href:"https://orbit-mvi.org"},"Orbit"),". They demonstrate:"),(0,s.kt)("ul",null,(0,s.kt)("li",{parentName:"ul"},"how to implement a dual pane layout"),(0,s.kt)("li",{parentName:"ul"},"how to run more complex asynchronous operations (this time real ones) with loading spinners and error handling"),(0,s.kt)("li",{parentName:"ul"},"how to use initializers"),(0,s.kt)("li",{parentName:"ul"},"how to use side effects"),(0,s.kt)("li",{parentName:"ul"},"how to combine Redux and ",(0,s.kt)("inlineCode",{parentName:"li"},"BlocOwner")," / MVVM+ syntax"),(0,s.kt)("li",{parentName:"ul"},"how to use Blocs with Fragments on Android"),(0,s.kt)("li",{parentName:"ul"},"how to define a contract for the view component")),(0,s.kt)("h3",{id:"posts-1"},"Posts 1"),(0,s.kt)("p",null,"The ",(0,s.kt)("a",{parentName:"p",href:"https://github.com/1gravity/Kotlin-Bloc/tree/master/bloc-samples/src/commonMain/kotlin/com/onegravity/bloc/sample/posts/bloc"},"first example app")," uses two different ",(0,s.kt)("inlineCode",{parentName:"p"},"Blocs")," with separate ",(0,s.kt)("inlineCode",{parentName:"p"},"BlocStates"),". "),(0,s.kt)("pre",null,(0,s.kt)("code",{parentName:"pre",className:"language-kotlin"},"data class PostsState(\n    val loading: Boolean = false,\n    val posts: Result<List<Post>, Throwable> = Ok(emptyList()),\n)\n\nobject Posts {\n    // the Action class\n    sealed class Action {\n        object Load : Action()\n        object Loading : Action()\n        data class Loaded(val posts: Result<List<Post>, Throwable>) : Action()\n        data class Clicked(val post: Post) : Action()\n    }\n\n    // the SideEffect class\n    class OpenPost(val post: Post)\n\n    fun bloc(context: BlocContext) = bloc<PostsState, Action, OpenPost, PostsState>(\n        context,\n        blocState(PostsState())\n    ) {\n        val repository = getKoinInstance<PostRepository>()\n\n        // start loading the posts in the initializer\n        onCreate { \n          if (state.isEmpty()) dispatch(Action.Load) \n        }\n\n        // we could also put the thunk code into the onCreate block \n        // but we want to illustrate the use of thunks\n        thunk<Action.Load> {\n            dispatch(Action.Loading)\n            val result = repository.getOverviews()\n            dispatch(Action.Loaded(result))\n        }\n\n        // this is an anti-pattern according to \n        // https://medium.com/androiddevelopers/viewmodel-one-off-event-antipatterns-16a1da869b95\n        // but we're doing it anyway\n        sideEffect<Action.Clicked> { OpenPost(action.post) }\n\n        reduce<Action.Loading> { \n          state.copy(loading = true) \n        }\n\n        reduce<Action.Loaded> {\n            state.copy(loading = false, posts = action.posts)\n        }\n    }\n}\n")),(0,s.kt)("h4",{id:"android"},"Android"),(0,s.kt)("p",null,"The original sample app in ",(0,s.kt)("a",{parentName:"p",href:"https://orbit-mvi.org"},"Orbit")," uses a ViewModel and a Fragment but ",(0,s.kt)("inlineCode",{parentName:"p"},"Kotlin Bloc")," eliminates the need for a ViewModel:"),(0,s.kt)("pre",null,(0,s.kt)("code",{parentName:"pre",className:"language-kotlin"},'class PostsFragment : \n    Fragment(R.layout.post_list_fragment),\n    BlocOwner<PostsState, Posts.Action, Posts.OpenPost, PostsState> {\n\n    override val bloc by getOrCreate("posts") { Posts.bloc(it) }\n\n    override fun onResume() {\n        super.onResume()\n        \n        // subscribe to the state and the side effects\n        subscribe(this, ::render, ::sideEffect)\n    }\n\n    private fun render(state: PostsState) {\n        // render the view\n    }\n\n    private fun sideEffect(sideEffect: Posts.OpenPost) {\n        // navigate to the detail view using a side effect\n        findNavController().navigate(\n            PostsFragmentDirections.actionListFragmentToDetailFragment(\n                sideEffect.post\n            )\n        )\n    }\n')),(0,s.kt)("admonition",{type:"tip"},(0,s.kt)("p",{parentName:"admonition"},'The bloc in above example is created using the key "posts":'),(0,s.kt)("pre",{parentName:"admonition"},(0,s.kt)("code",{parentName:"pre",className:"language-kotlin"},'override val bloc by getOrCreate("posts") { Posts.bloc(it) }\n')),(0,s.kt)("p",{parentName:"admonition"},(0,s.kt)("a",{parentName:"p",href:"/Kotlin-Bloc/docs/extensions/android/android_bloc_context#activityfragment"},"Activity/Fragment")," explains in detail why we need that key.")),(0,s.kt)("h4",{id:"ios"},"iOS"),(0,s.kt)("p",null,"There's no iOS implementation for the first posts example."),(0,s.kt)("h3",{id:"posts-2"},"Posts 2"),(0,s.kt)("p",null,"The ",(0,s.kt)("a",{parentName:"p",href:"https://github.com/1gravity/Kotlin-Bloc/tree/master/bloc-samples/src/commonMain/kotlin/com/onegravity/bloc/sample/posts/compose"},"second example app")," uses a single bloc for the master and the details view/component and defines a contract between the view and the bloc to use function calls instead of actions:"),(0,s.kt)("pre",null,(0,s.kt)("code",{parentName:"pre",className:"language-kotlin"},"abstract class PostsComponent : BlocOwner<PostsRootState, PostsAction, Unit, PostsRootState> {\n    // we need this for iOS to prevent erasure of generic types\n    abstract override val bloc: Bloc<PostsRootState, PostsAction, Unit>\n\n    abstract fun onSelected(post: Post)\n    abstract fun onClosed()\n}\n")),(0,s.kt)("p",null,"The implementing PostsComponent class is pretty straight forward:"),(0,s.kt)("pre",null,(0,s.kt)("code",{parentName:"pre",className:"language-kotlin"},"sealed class PostsAction\n\nclass PostsComponentImpl(context: BlocContext) : PostsComponent() {\n\n    // internal actions, not exposed to the View\n    private object PostsLoading : PostsAction()\n    private data class PostsLoaded(val result: Result<List<Post>, Throwable>) : PostsAction()\n    private class PostLoading(val postId: Int) : PostsAction()\n    private data class PostLoaded(val result: Result<Post, Throwable>) : PostsAction()\n\n    // we need to lazy initialize the Bloc so that the component is fully initialized before\n    // making any calls to load the posts\n    override val bloc by lazy {\n        bloc<PostsRootState, PostsAction>(context, blocState) {\n            onCreate {\n                // initializer code\n            }\n\n            reduce<PostsLoading> {\n                // reducer code\n            }\n\n            // more reducer here\n        }\n    }\n\n    // using BlocOwner / MVVM+ syntax\n    override fun onSelected(post: Post) = thunk {\n        // load the selected post asynchronously, simplified:\n        dispatch(PostLoading(post.id))\n        val result = repository.getDetail(post.id)\n        dispatch(PostLoaded(result))\n    }\n\n    // using BlocOwner / MVVM+ syntax\n    override fun onClosed() = reduce {\n        state.copy(postState = state.postState.copy(loadingId = null, post = null))\n    }\n}\n")),(0,s.kt)("h4",{id:"android-1"},"Android"),(0,s.kt)("p",null,"The Android implementation doesn't introduce any new concepts compared to previous example apps.\nIf you're interested in implementing a dual pane layout with Jetpack Compose, please consult the source code ",(0,s.kt)("a",{parentName:"p",href:"https://github.com/1gravity/Kotlin-Bloc/tree/master/androidApp/src/main/kotlin/com/onegravity/bloc/posts_compose"},"here"),"."),(0,s.kt)("h4",{id:"ios-1"},"iOS"),(0,s.kt)("p",null,"The iOS implementation introduces one new concept which is the ",(0,s.kt)("a",{parentName:"p",href:"/Kotlin-Bloc/docs/extensions/ios/ios_bloc_component"},"BlocComponent"),". "),(0,s.kt)("p",null,"The difference to using ",(0,s.kt)("a",{parentName:"p",href:"/Kotlin-Bloc/docs/extensions/ios/ios_bloc_holder"},"BlocHolders")," is very small though:"),(0,s.kt)("pre",null,(0,s.kt)("code",{parentName:"pre",className:"language-swift"},"private let component = BlocComponent<PostsComponent> { PostsComponentImpl(context: $0) }\n")),(0,s.kt)("p",null,"compared to e.g.:"),(0,s.kt)("pre",null,(0,s.kt)("code",{parentName:"pre",className:"language-swift"},"private let holder = BlocHolder { SimpleCounter.shared.bloc(context: $0) }\n")))}d.isMDXComponent=!0},8164:function(e,t,n){t.Z=n.p+"assets/images/posts-93dbec38c9c16c3501aff8731290aeca.png"}}]);