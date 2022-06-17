"use strict";(self.webpackChunkwebsite=self.webpackChunkwebsite||[]).push([[1845],{3905:function(e,t,n){n.d(t,{Zo:function(){return p},kt:function(){return m}});var o=n(7294);function a(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function r(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);t&&(o=o.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,o)}return n}function i(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?r(Object(n),!0).forEach((function(t){a(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):r(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function l(e,t){if(null==e)return{};var n,o,a=function(e,t){if(null==e)return{};var n,o,a={},r=Object.keys(e);for(o=0;o<r.length;o++)n=r[o],t.indexOf(n)>=0||(a[n]=e[n]);return a}(e,t);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);for(o=0;o<r.length;o++)n=r[o],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(a[n]=e[n])}return a}var c=o.createContext({}),s=function(e){var t=o.useContext(c),n=t;return e&&(n="function"==typeof e?e(t):i(i({},t),e)),n},p=function(e){var t=s(e.components);return o.createElement(c.Provider,{value:t},e.children)},u={inlineCode:"code",wrapper:function(e){var t=e.children;return o.createElement(o.Fragment,{},t)}},d=o.forwardRef((function(e,t){var n=e.components,a=e.mdxType,r=e.originalType,c=e.parentName,p=l(e,["components","mdxType","originalType","parentName"]),d=s(n),m=a,v=d["".concat(c,".").concat(m)]||d[m]||u[m]||r;return n?o.createElement(v,i(i({ref:t},p),{},{components:n})):o.createElement(v,i({ref:t},p))}));function m(e,t){var n=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var r=n.length,i=new Array(r);i[0]=d;var l={};for(var c in t)hasOwnProperty.call(t,c)&&(l[c]=t[c]);l.originalType=e,l.mdxType="string"==typeof e?e:a,i[1]=l;for(var s=2;s<r;s++)i[s]=n[s];return o.createElement.apply(null,i)}return o.createElement.apply(null,n)}d.displayName="MDXCreateElement"},4353:function(e,t,n){n.r(t),n.d(t,{assets:function(){return p},contentTitle:function(){return c},default:function(){return m},frontMatter:function(){return l},metadata:function(){return s},toc:function(){return u}});var o=n(7462),a=n(3366),r=(n(7294),n(3905)),i=["components"],l={id:"counter",title:"Counter",sidebar_label:"Counter",hide_title:!0},c=void 0,s={unversionedId:"examples/counter",id:"examples/counter",title:"Counter",description:"Counter",source:"@site/docs/examples/counter.md",sourceDirName:"examples",slug:"/examples/counter",permalink:"/Kotlin-Bloc/docs/examples/counter",draft:!1,tags:[],version:"current",frontMatter:{id:"counter",title:"Counter",sidebar_label:"Counter",hide_title:!0},sidebar:"exampleSidebar",previous:{title:"Introduction",permalink:"/Kotlin-Bloc/docs/examples/"},next:{title:"Books",permalink:"/Kotlin-Bloc/docs/examples/books"}},p={},u=[{value:"Counter",id:"counter",level:2},{value:"Counter 1",id:"counter-1",level:3},{value:"Counter 2",id:"counter-2",level:3},{value:"Counter 3",id:"counter-3",level:3}],d={toc:u};function m(e){var t=e.components,n=(0,a.Z)(e,i);return(0,r.kt)("wrapper",(0,o.Z)({},d,n,{components:t,mdxType:"MDXLayout"}),(0,r.kt)("h2",{id:"counter"},"Counter"),(0,r.kt)("p",null,'The "Hello World" sample of UI frameworks is the counter app. There are three sample implementations in this framework that demonstrate:'),(0,r.kt)("ul",null,(0,r.kt)("li",{parentName:"ul"},"how to use a ",(0,r.kt)("inlineCode",{parentName:"li"},"Bloc")," as ",(0,r.kt)("inlineCode",{parentName:"li"},"BlocState")),(0,r.kt)("li",{parentName:"ul"},"how to use a Redux store as ",(0,r.kt)("inlineCode",{parentName:"li"},"BlocState")),(0,r.kt)("li",{parentName:"ul"},"how to define a ",(0,r.kt)("inlineCode",{parentName:"li"},"Bloc")," in the view itself (Android only)")),(0,r.kt)("h3",{id:"counter-1"},"Counter 1"),(0,r.kt)("p",null,(0,r.kt)("a",{parentName:"p",href:"https://github.com/1gravity/Kotlin-Bloc/blob/master/bloc-samples/src/commonMain/kotlin/com/onegravity/bloc/sample/counter/SimpleCounter.kt"},"SimpleCounter")," demonstrates how a ",(0,r.kt)("inlineCode",{parentName:"p"},"Bloc")," can be used as ",(0,r.kt)("inlineCode",{parentName:"p"},"BlocState")," (see also ",(0,r.kt)("a",{parentName:"p",href:"/Kotlin-Bloc/docs/architecture/blocstate/bloc_state#bloc-isa-blocstate"},"Bloc is a BlocState"),') basically intercepting the "communication" between a ',(0,r.kt)("inlineCode",{parentName:"p"},"Bloc")," and it's ",(0,r.kt)("inlineCode",{parentName:"p"},"BlocState"),". "),(0,r.kt)("p",null,'All it takes to "convert" a ',(0,r.kt)("inlineCode",{parentName:"p"},"Bloc")," to a ",(0,r.kt)("inlineCode",{parentName:"p"},"BlocState")," is the extension function ",(0,r.kt)("inlineCode",{parentName:"p"},"asBlocState()"),":"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},"fun bloc(context: BlocContext) = bloc<Int, Action>(\n  context,\n  interceptorBloc1(context, 1).asBlocState()\n) {\n")),(0,r.kt)("div",{className:"admonition admonition-tip alert alert--success"},(0,r.kt)("div",{parentName:"div",className:"admonition-heading"},(0,r.kt)("h5",{parentName:"div"},(0,r.kt)("span",{parentName:"h5",className:"admonition-icon"},(0,r.kt)("svg",{parentName:"span",xmlns:"http://www.w3.org/2000/svg",width:"12",height:"16",viewBox:"0 0 12 16"},(0,r.kt)("path",{parentName:"svg",fillRule:"evenodd",d:"M6.5 0C3.48 0 1 2.19 1 5c0 .92.55 2.25 1 3 1.34 2.25 1.78 2.78 2 4v1h5v-1c.22-1.22.66-1.75 2-4 .45-.75 1-2.08 1-3 0-2.81-2.48-5-5.5-5zm3.64 7.48c-.25.44-.47.8-.67 1.11-.86 1.41-1.25 2.06-1.45 3.23-.02.05-.02.11-.02.17H5c0-.06 0-.13-.02-.17-.2-1.17-.59-1.83-1.45-3.23-.2-.31-.42-.67-.67-1.11C2.44 6.78 2 5.65 2 5c0-2.2 2.02-4 4.5-4 1.22 0 2.36.42 3.22 1.19C10.55 2.94 11 3.94 11 5c0 .66-.44 1.78-.86 2.48zM4 14h5c-.23 1.14-1.3 2-2.5 2s-2.27-.86-2.5-2z"}))),"tip")),(0,r.kt)("div",{parentName:"div",className:"admonition-content"},(0,r.kt)("p",{parentName:"div"},"Using a ",(0,r.kt)("inlineCode",{parentName:"p"},"Bloc")," as ",(0,r.kt)("inlineCode",{parentName:"p"},"BlocState")," is not recommended. All business logic related to a view component should be implemented in a single bloc. Even the ",(0,r.kt)("inlineCode",{parentName:"p"},"auditTrailBloc")," in the example that just adds some logging, isn't a good example because an audit trail would typically be implemented at the source of truth and that's the ",(0,r.kt)("inlineCode",{parentName:"p"},"BlocState"),", not some intercepting ",(0,r.kt)("inlineCode",{parentName:"p"},"Bloc"),"."))),(0,r.kt)("h3",{id:"counter-2"},"Counter 2"),(0,r.kt)("p",null,(0,r.kt)("a",{parentName:"p",href:"https://github.com/1gravity/Kotlin-Bloc/blob/master/bloc-samples/src/commonMain/kotlin/com/onegravity/bloc/sample/counter/ReduxCounter.kt"},"ReduxCounter")," demonstrates how a ",(0,r.kt)("inlineCode",{parentName:"p"},"Bloc")," connects to a Redux store as it's ",(0,r.kt)("inlineCode",{parentName:"p"},"BlocState"),". The store intentionally has a more complex model than what we need for a simple counter to demonstrate how to compose reducers for different ",(0,r.kt)("inlineCode",{parentName:"p"},"Blocs")," and how to select sub state:"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},"// the model\ndata class Purpose(val title: String, val description: String)\ndata class Counter(val count: Int, val lastValue: Int)\ndata class ReduxModel(val purpose: Purpose, val counter: Counter)\n")),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},"// reducers\nprivate fun purposeReducer(state: Purpose, action: Any) = when (action) {\n    is ReduxAction.UpdateTitle -> state.copy(title = action.value)\n    is ReduxAction.UpdateDescription -> state.copy(description = action.value)\n    else -> state\n}\n\nprivate fun counterReducer(state: CounterStore.Counter, action: Any) = when (action) {\n    is ReduxAction.UpdateCount -> Counter(action.value, state.count)\n    else -> state\n}\n\nprivate fun rootReducer(state: CounterStore.ReduxModel, action: Any) = ReduxModel(\n    purpose = purposeReducer(state.purpose, action),\n    counter = counterReducer(state.counter, action)\n)\n")),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},"// select sub state\nreduxStore.toBlocState(\n  context = context,\n  select = { reduxModel ->  reduxModel.counter },\n  map = { model -> model.count }\n)\n")),(0,r.kt)("h3",{id:"counter-3"},"Counter 3"),(0,r.kt)("p",null,"The ",(0,r.kt)("a",{parentName:"p",href:"https://github.com/1gravity/Kotlin-Bloc/blob/master/androidApp/src/main/kotlin/com/onegravity/bloc/counter/CounterActivityCompose.kt"},"third implementation"),' is Android only and moves the "business logic" right into the ',(0,r.kt)("inlineCode",{parentName:"p"},"View")," / ",(0,r.kt)("inlineCode",{parentName:"p"},"Activity"),":"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},'class CounterActivityCompose : AppCompatActivity() {\n\n    private val bloc by getOrCreate { bloc<Int, Int>(it, 1) { reduce { state + action } } }\n\n    override fun onCreate(savedInstanceState: Bundle?) {\n        super.onCreate(savedInstanceState)\n\n        setContent {\n            // observe State\n            val state by bloc.observeState()\n\n            // display State\n            Text(text = stringResource(R.string.counter_value, state))\n            \n            // send Actions to the Bloc\n            Button(\n              onClick = { bloc.send(1) },\n              content = { Text(text = "Increment") }\n            )\n            Button(\n              onClick = { bloc.send(-1) },\n              content = { Text(text = "Decrement") }\n            )\n        }\n    }\n}\n')),(0,r.kt)("div",{className:"admonition admonition-tip alert alert--success"},(0,r.kt)("div",{parentName:"div",className:"admonition-heading"},(0,r.kt)("h5",{parentName:"div"},(0,r.kt)("span",{parentName:"h5",className:"admonition-icon"},(0,r.kt)("svg",{parentName:"span",xmlns:"http://www.w3.org/2000/svg",width:"12",height:"16",viewBox:"0 0 12 16"},(0,r.kt)("path",{parentName:"svg",fillRule:"evenodd",d:"M6.5 0C3.48 0 1 2.19 1 5c0 .92.55 2.25 1 3 1.34 2.25 1.78 2.78 2 4v1h5v-1c.22-1.22.66-1.75 2-4 .45-.75 1-2.08 1-3 0-2.81-2.48-5-5.5-5zm3.64 7.48c-.25.44-.47.8-.67 1.11-.86 1.41-1.25 2.06-1.45 3.23-.02.05-.02.11-.02.17H5c0-.06 0-.13-.02-.17-.2-1.17-.59-1.83-1.45-3.23-.2-.31-.42-.67-.67-1.11C2.44 6.78 2 5.65 2 5c0-2.2 2.02-4 4.5-4 1.22 0 2.36.42 3.22 1.19C10.55 2.94 11 3.94 11 5c0 .66-.44 1.78-.86 2.48zM4 14h5c-.23 1.14-1.3 2-2.5 2s-2.27-.86-2.5-2z"}))),"tip")),(0,r.kt)("div",{parentName:"div",className:"admonition-content"},(0,r.kt)("p",{parentName:"div"},"Typically the business logic is in a shared component so it can be used in Android and iOS. "))))}m.isMDXComponent=!0}}]);