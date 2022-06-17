"use strict";(self.webpackChunkwebsite=self.webpackChunkwebsite||[]).push([[9626],{3905:function(e,t,n){n.d(t,{Zo:function(){return p},kt:function(){return m}});var r=n(7294);function a(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function o(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function i(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?o(Object(n),!0).forEach((function(t){a(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):o(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function l(e,t){if(null==e)return{};var n,r,a=function(e,t){if(null==e)return{};var n,r,a={},o=Object.keys(e);for(r=0;r<o.length;r++)n=o[r],t.indexOf(n)>=0||(a[n]=e[n]);return a}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(r=0;r<o.length;r++)n=o[r],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(a[n]=e[n])}return a}var c=r.createContext({}),s=function(e){var t=r.useContext(c),n=t;return e&&(n="function"==typeof e?e(t):i(i({},t),e)),n},p=function(e){var t=s(e.components);return r.createElement(c.Provider,{value:t},e.children)},u={inlineCode:"code",wrapper:function(e){var t=e.children;return r.createElement(r.Fragment,{},t)}},d=r.forwardRef((function(e,t){var n=e.components,a=e.mdxType,o=e.originalType,c=e.parentName,p=l(e,["components","mdxType","originalType","parentName"]),d=s(n),m=a,f=d["".concat(c,".").concat(m)]||d[m]||u[m]||o;return n?r.createElement(f,i(i({ref:t},p),{},{components:n})):r.createElement(f,i({ref:t},p))}));function m(e,t){var n=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var o=n.length,i=new Array(o);i[0]=d;var l={};for(var c in t)hasOwnProperty.call(t,c)&&(l[c]=t[c]);l.originalType=e,l.mdxType="string"==typeof e?e:a,i[1]=l;for(var s=2;s<o;s++)i[s]=n[s];return r.createElement.apply(null,i)}return r.createElement.apply(null,n)}d.displayName="MDXCreateElement"},6585:function(e,t,n){n.r(t),n.d(t,{assets:function(){return p},contentTitle:function(){return c},default:function(){return m},frontMatter:function(){return l},metadata:function(){return s},toc:function(){return u}});var r=n(7462),a=n(3366),o=(n(7294),n(3905)),i=["components"],l={id:"bloc_owner",title:"Bloc Owner",sidebar_label:"Bloc Owner",hide_title:!0},c=void 0,s={unversionedId:"architecture/blocowner/bloc_owner",id:"architecture/blocowner/bloc_owner",title:"Bloc Owner",description:"Motivation",source:"@site/docs/architecture/blocowner/bloc_owner.md",sourceDirName:"architecture/blocowner",slug:"/architecture/blocowner/bloc_owner",permalink:"/Kotlin-Bloc/docs/architecture/blocowner/bloc_owner",draft:!1,tags:[],version:"current",frontMatter:{id:"bloc_owner",title:"Bloc Owner",sidebar_label:"Bloc Owner",hide_title:!0},sidebar:"architectureSidebar",previous:{title:"Bloc State Builder",permalink:"/Kotlin-Bloc/docs/architecture/blocstate/bloc_state_builder"},next:{title:"Bloc Observable",permalink:"/Kotlin-Bloc/docs/architecture/blocowner/bloc_observable"}},p={},u=[{value:"Motivation",id:"motivation",level:2},{value:"Redux Style",id:"redux-style",level:3},{value:"MVVM+ Style",id:"mvvm-style",level:3},{value:"BlocOwner",id:"blocowner",level:2}],d={toc:u};function m(e){var t=e.components,l=(0,a.Z)(e,i);return(0,o.kt)("wrapper",(0,r.Z)({},d,l,{components:t,mdxType:"MDXLayout"}),(0,o.kt)("h2",{id:"motivation"},"Motivation"),(0,o.kt)("p",null,"In ",(0,o.kt)("a",{parentName:"p",href:"../bloc/reducer#a-matter-of-taste"},"A Matter of Taste")," we discussed two different styles to define reducers (catch-all vs single-action reducers). In this chapter we take the idea of single-action reducers one step further. To recap, this is what we have so far:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-kotlin"},"bloc<Int, Action>(context, 1) {\n    reduce<Increment> { state + 1 }\n    reduce<Decrement> { state - 1 }\n\n    // vs.\n    \n    reduce {\n        when(action) {\n            is Increment -> state + 1\n            else -> state -1\n        }\n    }\n}\n")),(0,o.kt)("p",null,"The idea of ",(0,o.kt)("inlineCode",{parentName:"p"},"BlocOwners")," was inspired by the ",(0,o.kt)("a",{parentName:"p",href:"https://orbit-mvi.org/"},"Orbit")," framework which has the concept of a ",(0,o.kt)("inlineCode",{parentName:"p"},"ContainerHost")," used for classes that want to launch Orbit ",(0,o.kt)("inlineCode",{parentName:"p"},"intents"),". Here's an example:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-kotlin"},'class CalculatorViewModel: ContainerHost<CalculatorState, CalculatorSideEffect>, ViewModel() {\n\n    override val container = container<CalculatorState, CalculatorSideEffect>(CalculatorState())\n\n    fun add(number: Int) = intent {\n        postSideEffect(CalculatorSideEffect.Toast("Adding $number to ${state.total}!"))\n\n        reduce {\n            state.copy(total = state.total + number)\n        }\n    }\n}\n')),(0,o.kt)("p",null,"The ",(0,o.kt)("inlineCode",{parentName:"p"},"ContainerHost")," concept is an iteration over the MVI model, enabling us to define the intent / business logic wherever we want. The author of the framework compares the Redux style vs. the MVVM+ style (that's what he calls the Orbit approach) of implementing MVI in ",(0,o.kt)("a",{parentName:"p",href:"https://appmattus.medium.com/top-android-mvi-libraries-in-2021-de1afe890f27"},"this article"),". The following two diagrams summarize the two styles nicely."),(0,o.kt)("h3",{id:"redux-style"},"Redux Style"),(0,o.kt)("p",null,(0,o.kt)("img",{alt:"Reducer Redux Style",src:n(6558).Z,width:"622",height:"330"})),(0,o.kt)("p",null,"Intents are represented by objects, piped through a stream into a transformer and reducer, which produces a single state output."),(0,o.kt)("h3",{id:"mvvm-style"},"MVVM+ Style"),(0,o.kt)("p",null,(0,o.kt)("img",{alt:"Reducer MVVM+ Style",src:n(9512).Z,width:"621",height:"254"})),(0,o.kt)("p",null,"Intents here have their own transformer and reducer (a function) before being combined into the single state output."),(0,o.kt)("h2",{id:"blocowner"},"BlocOwner"),(0,o.kt)("p",null,"A ",(0,o.kt)("inlineCode",{parentName:"p"},"BlocOwner")," in ",(0,o.kt)("inlineCode",{parentName:"p"},"Kotlin Bloc")," is the equivalent of an Orbit ",(0,o.kt)("inlineCode",{parentName:"p"},"ContainerHost"),". It's an interface that can be implemented by any class and has a ",(0,o.kt)("inlineCode",{parentName:"p"},"Bloc"),":"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-kotlin"},"public interface BlocOwner<out State : Any, in Action : Any, SideEffect : Any, Proposal : Any> {\n\n    public val bloc: Bloc<State, Action, SideEffect>\n\n}\n")),(0,o.kt)("p",null,"Extension functions give the ",(0,o.kt)("inlineCode",{parentName:"p"},"BlocOwner"),' the "power" to define initializers, reducers, thunks and side effects. The syntax is even simpler than with Orbit (which requires to wrap everything in an ',(0,o.kt)("inlineCode",{parentName:"p"},"intent")," statement before ",(0,o.kt)("inlineCode",{parentName:"p"},"reduce"),", ",(0,o.kt)("inlineCode",{parentName:"p"},"postSideEffect")," and ",(0,o.kt)("inlineCode",{parentName:"p"},"repeatOnSubscription")," can be used):"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-kotlin"},"class PostListViewModel(context: ActivityBlocContext) : ViewModel(),\n    BlocOwner<PostsState, Nothing, Posts.OpenPost, PostsState>,\n    KoinComponent {\n\n    private val repository = getKoinInstance<PostRepository>()\n\n    override val bloc = bloc<PostsState, Nothing, Posts.OpenPost, PostsState>(\n        blocContext(context),\n        blocState(PostsState())\n    )\n\n    init {\n        onCreate {\n            if (state.isEmpty()) {\n                loading()\n                loaded(repository.getOverviews())\n            }\n        }\n    }\n\n    private fun loading() = reduce {\n        state.copy(loading = true)\n    }\n\n    private fun loaded(posts: List<Post>) = reduce {\n        state.copy(loading = false, posts = posts)\n    }\n\n    fun onPostClicked(post: Post) = sideEffect {\n        Posts.OpenPost(post)\n    }\n}\n")),(0,o.kt)("p",null,"In above example an Android ViewModel implements the ",(0,o.kt)("inlineCode",{parentName:"p"},"BlocOwner"),' interface but any class can implement it to use the "MVVM+ syntax". As a matter of fact with ',(0,o.kt)("a",{parentName:"p",href:"../../extensions/overview"},"Extensions"),", you could put the business logic into an Android Activity and still retain state across configuration changes (of course you'd do that only for simple applications):"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-kotlin"},"class CounterActivity : AppCompatActivity(), BlocOwner<Int, Int, Unit, Int> {\n\n    override val bloc by getOrCreate { bloc<Int, Int>(it, 1) }\n\n    private fun increment() = reduce { state + 1 }\n    \n    private fun decrement() = reduce { state - 1 }\n\n    override fun onCreate(savedInstanceState: Bundle?) {\n        setContent {\n            val state by bloc.observeState()            \n            \n            // here goes the view declaration\n        }\n    }\n")),(0,o.kt)("div",{className:"admonition admonition-tip alert alert--success"},(0,o.kt)("div",{parentName:"div",className:"admonition-heading"},(0,o.kt)("h5",{parentName:"div"},(0,o.kt)("span",{parentName:"h5",className:"admonition-icon"},(0,o.kt)("svg",{parentName:"span",xmlns:"http://www.w3.org/2000/svg",width:"12",height:"16",viewBox:"0 0 12 16"},(0,o.kt)("path",{parentName:"svg",fillRule:"evenodd",d:"M6.5 0C3.48 0 1 2.19 1 5c0 .92.55 2.25 1 3 1.34 2.25 1.78 2.78 2 4v1h5v-1c.22-1.22.66-1.75 2-4 .45-.75 1-2.08 1-3 0-2.81-2.48-5-5.5-5zm3.64 7.48c-.25.44-.47.8-.67 1.11-.86 1.41-1.25 2.06-1.45 3.23-.02.05-.02.11-.02.17H5c0-.06 0-.13-.02-.17-.2-1.17-.59-1.83-1.45-3.23-.2-.31-.42-.67-.67-1.11C2.44 6.78 2 5.65 2 5c0-2.2 2.02-4 4.5-4 1.22 0 2.36.42 3.22 1.19C10.55 2.94 11 3.94 11 5c0 .66-.44 1.78-.86 2.48zM4 14h5c-.23 1.14-1.3 2-2.5 2s-2.27-.86-2.5-2z"}))),"tip")),(0,o.kt)("div",{parentName:"div",className:"admonition-content"},(0,o.kt)("p",{parentName:"div"},"Reducers are always processed sequentially regardless whether they were triggered by an action (Redux style) or by a function (MVVM+ style). See also ",(0,o.kt)("a",{parentName:"p",href:"/Kotlin-Bloc/docs/architecture/bloc/reducer#concurrency"},"Concurrency"),"."))))}m.isMDXComponent=!0},9512:function(e,t,n){t.Z=n.p+"assets/images/reducer_mvvm_style-2f835a01be3599b7926b03e49ed3bcfb.png"},6558:function(e,t,n){t.Z=n.p+"assets/images/reducer_redux_style-436bb8c40fa4949766405fcf3bbc4007.png"}}]);