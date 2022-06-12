"use strict";(self.webpackChunkwebsite=self.webpackChunkwebsite||[]).push([[803],{3905:function(e,t,n){n.d(t,{Zo:function(){return d},kt:function(){return m}});var a=n(7294);function o(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function r(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,a)}return n}function l(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?r(Object(n),!0).forEach((function(t){o(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):r(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function i(e,t){if(null==e)return{};var n,a,o=function(e,t){if(null==e)return{};var n,a,o={},r=Object.keys(e);for(a=0;a<r.length;a++)n=r[a],t.indexOf(n)>=0||(o[n]=e[n]);return o}(e,t);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);for(a=0;a<r.length;a++)n=r[a],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(o[n]=e[n])}return o}var c=a.createContext({}),s=function(e){var t=a.useContext(c),n=t;return e&&(n="function"==typeof e?e(t):l(l({},t),e)),n},d=function(e){var t=s(e.components);return a.createElement(c.Provider,{value:t},e.children)},p={inlineCode:"code",wrapper:function(e){var t=e.children;return a.createElement(a.Fragment,{},t)}},u=a.forwardRef((function(e,t){var n=e.components,o=e.mdxType,r=e.originalType,c=e.parentName,d=i(e,["components","mdxType","originalType","parentName"]),u=s(n),m=o,f=u["".concat(c,".").concat(m)]||u[m]||p[m]||r;return n?a.createElement(f,l(l({ref:t},d),{},{components:n})):a.createElement(f,l({ref:t},d))}));function m(e,t){var n=arguments,o=t&&t.mdxType;if("string"==typeof e||o){var r=n.length,l=new Array(r);l[0]=u;var i={};for(var c in t)hasOwnProperty.call(t,c)&&(i[c]=t[c]);i.originalType=e,i.mdxType="string"==typeof e?e:o,l[1]=i;for(var s=2;s<r;s++)l[s]=n[s];return a.createElement.apply(null,l)}return a.createElement.apply(null,n)}u.displayName="MDXCreateElement"},2859:function(e,t,n){n.r(t),n.d(t,{assets:function(){return d},contentTitle:function(){return c},default:function(){return m},frontMatter:function(){return i},metadata:function(){return s},toc:function(){return p}});var a=n(7462),o=n(3366),r=(n(7294),n(3905)),l=["components"],i={id:"examples",title:"Examples",sidebar_label:"Examples",hide_title:!0},c=void 0,s={unversionedId:"getting_started/examples",id:"getting_started/examples",title:"Examples",description:"Counter",source:"@site/docs/getting_started/examples.md",sourceDirName:"getting_started",slug:"/getting_started/examples",permalink:"/Kotlin-Bloc/docs/getting_started/examples",draft:!1,tags:[],version:"current",frontMatter:{id:"examples",title:"Examples",sidebar_label:"Examples",hide_title:!0},sidebar:"gettingStartedSidebar",previous:{title:"Setup",permalink:"/Kotlin-Bloc/docs/getting_started/setup"}},d={},p=[{value:"Counter",id:"counter",level:2},{value:"Android",id:"android",level:3},{value:"iOS",id:"ios",level:3},{value:"Single-Action Reducer",id:"single-action-reducer",level:3},{value:"Catch-all Reducer",id:"catch-all-reducer",level:3},{value:"Enums",id:"enums",level:3},{value:"MVVM+ / Orbit Style",id:"mvvm--orbit-style",level:3},{value:"Post List",id:"post-list",level:2}],u={toc:p};function m(e){var t=e.components,n=(0,o.Z)(e,l);return(0,r.kt)("wrapper",(0,a.Z)({},u,n,{components:t,mdxType:"MDXLayout"}),(0,r.kt)("h2",{id:"counter"},"Counter"),(0,r.kt)("p",null,'The "Hello World" example of UI frameworks is the counter app. Creating the "business logic" part of such an app is incredibly simple with ',(0,r.kt)("inlineCode",{parentName:"p"},"Kotlin Bloc"),":"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},"fun bloc(context: BlocContext) = bloc<Int, Int>(context, 1) {\n    reduce { state + action }\n}\n")),(0,r.kt)("p",null,"The view part is very simple too."),(0,r.kt)("h3",{id:"android"},"Android"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},"class CounterActivity : AppCompatActivity() {\n\n    // create or retrieve the lifecycle aware Bloc\n    private val bloc by getOrCreate { bloc(it) }\n")),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},'setContent {\n    // observe the Bloc state\n    val state by bloc.observeState()\n\n    // updates on state / count changes\n    Text("Counter: $state")\n\n    // emit events / actions to update the state / count\n    Button(onClick = { bloc.send(1) }, content = { Text("Increment") })\n    Button(onClick = { bloc.send(-1) }, content = { Text("Decrement") })\n}\n')),(0,r.kt)("p",null,"This is very little code considering the fact that the Bloc is lifecycle aware and will survive configuration changes."),(0,r.kt)("h3",{id:"ios"},"iOS"),(0,r.kt)("p",null,"On iOS there's a bit more boilerplate code (",(0,r.kt)("a",{parentName:"p",href:"https://github.com/1gravity/Kotlin-Bloc/blob/master/iosApp/iosApp/utils/BlocHolder.swift"},"BlocHolder")," and ",(0,r.kt)("a",{parentName:"p",href:"https://github.com/1gravity/Kotlin-Bloc/blob/master/iosApp/iosApp/utils/BlocObserver.swift"},"BlocObserver"),' are omitted here) but it\'s still pretty "lean":'),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-swift"},"// iOS\nstruct CounterView: View {\n    // create the lifecycle aware Bloc\n    private let holder = BlocHolder { CounterKt.bloc(context: $0) }\n    \n    @ObservedObject\n    private var model: BlocObserver<KotlinInt, KotlinInt, KotlinUnit>\n\n    init() {\n        // observe the Bloc state\n        model = BlocObserver(holder.value)\n    }\n")),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-swift"},'var body: some View {\n    return VStack() {    \n        // updates on state / count changes\n        Text("Counter \\(model.value)")\n    \n        // emit events / actions to update the state / count\n        Button(\n            action: { holder.value.send(value:  1) },\n            label: { Text("Increment") }\n        )\n        Button(\n            action: { holder.value.send(value: -1) },\n            label: { Text("Decrement") }\n        )\n')),(0,r.kt)("h3",{id:"single-action-reducer"},"Single-Action Reducer"),(0,r.kt)("p",null,(0,r.kt)("inlineCode",{parentName:"p"},"Kotlin Bloc"),' supports different MVI/MVVM "styles" and above example shows one of many ways to implement the counter app. Here are some alternative approaches to implementing the bloc:'),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},"sealed class Action\nobject Increment : Action()\nobject Decrement : Action()\n\nfun bloc(context: BlocContext) = bloc<Int, Action>(context, 1) {\n    reduce<Increment> { state + 1 }\n    reduce<Decrement> { state - 1 }\n}\n")),(0,r.kt)("h3",{id:"catch-all-reducer"},"Catch-all Reducer"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},"fun bloc(context: BlocContext) = bloc<Int, Action>(context, 1) {\n    reduce {\n        when (action) {\n            Increment -> state + 1\n            Decrement -> state - 1\n        }\n    }\n}\n")),(0,r.kt)("h3",{id:"enums"},"Enums"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},"enum class Action { Increment, Decrement }\n\nfun bloc(context: BlocContext) = bloc<Int, Action>(context, 1) {\n    reduce(Increment) { state + 1 }\n    reduce(Decrement) { state - 1 }\n}\n")),(0,r.kt)("h3",{id:"mvvm--orbit-style"},"MVVM+ / Orbit Style"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},"class CounterActivityCompose : AppCompatActivity(), BlocOwner<Int, Action, Unit, Int> {\n\n    // create or retrieve the lifecycle aware Bloc\n    override val bloc by getOrCreate { bloc<Int, Action>(it, 1) }\n\n    private fun increment() = reduce { state + 1 }\n\n    private fun decrement() = reduce { state - 1 }\n")),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},'setContent {\n    // observe the Bloc state\n    val state by bloc.observeState()\n\n    // updates on state / count changes\n    Text("Counter: $state")\n\n    // emit events / actions to update the state / count\n    Button(onClick = { increment() }, content = { Text("Increment") })\n    Button(onClick = { decrement() }, content = { Text("Decrement") })\n}\n')),(0,r.kt)("div",{className:"admonition admonition-tip alert alert--success"},(0,r.kt)("div",{parentName:"div",className:"admonition-heading"},(0,r.kt)("h5",{parentName:"div"},(0,r.kt)("span",{parentName:"h5",className:"admonition-icon"},(0,r.kt)("svg",{parentName:"span",xmlns:"http://www.w3.org/2000/svg",width:"12",height:"16",viewBox:"0 0 12 16"},(0,r.kt)("path",{parentName:"svg",fillRule:"evenodd",d:"M6.5 0C3.48 0 1 2.19 1 5c0 .92.55 2.25 1 3 1.34 2.25 1.78 2.78 2 4v1h5v-1c.22-1.22.66-1.75 2-4 .45-.75 1-2.08 1-3 0-2.81-2.48-5-5.5-5zm3.64 7.48c-.25.44-.47.8-.67 1.11-.86 1.41-1.25 2.06-1.45 3.23-.02.05-.02.11-.02.17H5c0-.06 0-.13-.02-.17-.2-1.17-.59-1.83-1.45-3.23-.2-.31-.42-.67-.67-1.11C2.44 6.78 2 5.65 2 5c0-2.2 2.02-4 4.5-4 1.22 0 2.36.42 3.22 1.19C10.55 2.94 11 3.94 11 5c0 .66-.44 1.78-.86 2.48zM4 14h5c-.23 1.14-1.3 2-2.5 2s-2.27-.86-2.5-2z"}))),"tip")),(0,r.kt)("div",{parentName:"div",className:"admonition-content"},(0,r.kt)("p",{parentName:"div"},"With ",(0,r.kt)("inlineCode",{parentName:"p"},"Kotlin Bloc")," there's no need for an Android ViewModel which only adds unnecessary boilerplate code (see ",(0,r.kt)("a",{parentName:"p",href:"/Kotlin-Bloc/docs/architecture/blocowner/bloc_owner#blocowner"},"BlocOwner"),")."))),(0,r.kt)("h2",{id:"post-list"},"Post List"),(0,r.kt)("p",null,"The following (artificial) example gives a more comprehensive overview of the different ",(0,r.kt)("inlineCode",{parentName:"p"},"Kotlin Bloc")," functions:"),(0,r.kt)("ul",null,(0,r.kt)("li",{parentName:"ul"},"single-action + catch-all reducer"),(0,r.kt)("li",{parentName:"ul"},"single-action and catch-all side effects"),(0,r.kt)("li",{parentName:"ul"},"reducer with side effects"),(0,r.kt)("li",{parentName:"ul"},"thunks"),(0,r.kt)("li",{parentName:"ul"},"initializer"),(0,r.kt)("li",{parentName:"ul"},"Redux and MVVM+ style")),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},'sealed class Action\nobject Loading : Action()\ndata class Loaded(val posts: List<Post>) : Action()\n\nsealed class SideEffect\ndata class OpenPost(val post: Post) : SideEffect()\nobject PostsLoaded : SideEffect()\nobject NOP : SideEffect()\n\ndata class Post(\n    val id: Int,\n    val title: String,\n    val body: String,\n)\n\ndata class State(\n    val loading: Boolean = false,\n    val posts: List<Post> = emptyList(),\n)\n\nclass PostViewModel : ViewModel(), BlocOwner<State, Action, SideEffect, State> {\n\n    override val bloc = bloc<State, Action, SideEffect, State>(\n        blocContext(), State(false)\n    ) {\n        // initializer\n        onCreate {\n            Log.i("bloc", "Bloc is starting")\n        }\n\n        // NOP thunk\n        thunk {\n            Log.i("bloc", "current state: ${getState()}")\n            dispatch(action)\n        }\n\n        // single-action reducer\n        reduce<Loading> {\n            state.copy(loading = true)\n        }\n\n        // single-action reducer with side effect\n        reduceAnd<Loaded> {\n            state.copy(loading = false, posts = state.posts) and PostsLoaded\n        }\n\n        // catch-all reducer with side effect\n        reduceAnd {\n            when (action) {\n                Loading -> state.copy(loading = true).noSideEffect()\n                is Loaded -> state.copy(loading = false, posts = state.posts) and PostsLoaded\n            }\n        }\n\n        // single-action side effect\n        sideEffect<Loaded> { PostsLoaded }\n\n        // catch-all side effect\n        sideEffect {\n            when (action) {\n                Loading -> NOP\n                is Loaded -> PostsLoaded\n            }\n        }\n    }\n\n    init {\n        // initializer, MVVM+ style\n        onCreate {\n            if (state.posts.isEmpty()) {\n                load()\n            }\n        }\n    }\n\n    // thunks for asynchronous operations, MVVM+ style\n    private fun load() = thunk {\n        dispatch(Loading)\n        // load the posts asynchronously\n        val posts: List<Post> = repository.loadPosts()\n        dispatch(Loaded(posts))\n    }\n\n    // side effects, MVVM+ style\n    fun onPostClicked(post: Post) = sideEffect {\n        OpenPost(post)\n    }\n\n}\n')))}m.isMDXComponent=!0}}]);