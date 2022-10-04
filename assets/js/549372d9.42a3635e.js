"use strict";(self.webpackChunkwebsite=self.webpackChunkwebsite||[]).push([[4713],{3905:function(e,t,n){n.d(t,{Zo:function(){return s},kt:function(){return f}});var o=n(7294);function a(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function r(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);t&&(o=o.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,o)}return n}function c(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?r(Object(n),!0).forEach((function(t){a(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):r(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function i(e,t){if(null==e)return{};var n,o,a=function(e,t){if(null==e)return{};var n,o,a={},r=Object.keys(e);for(o=0;o<r.length;o++)n=r[o],t.indexOf(n)>=0||(a[n]=e[n]);return a}(e,t);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);for(o=0;o<r.length;o++)n=r[o],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(a[n]=e[n])}return a}var l=o.createContext({}),u=function(e){var t=o.useContext(l),n=t;return e&&(n="function"==typeof e?e(t):c(c({},t),e)),n},s=function(e){var t=u(e.components);return o.createElement(l.Provider,{value:t},e.children)},p={inlineCode:"code",wrapper:function(e){var t=e.children;return o.createElement(o.Fragment,{},t)}},d=o.forwardRef((function(e,t){var n=e.components,a=e.mdxType,r=e.originalType,l=e.parentName,s=i(e,["components","mdxType","originalType","parentName"]),d=u(n),f=a,b=d["".concat(l,".").concat(f)]||d[f]||p[f]||r;return n?o.createElement(b,c(c({ref:t},s),{},{components:n})):o.createElement(b,c({ref:t},s))}));function f(e,t){var n=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var r=n.length,c=new Array(r);c[0]=d;var i={};for(var l in t)hasOwnProperty.call(t,l)&&(i[l]=t[l]);i.originalType=e,i.mdxType="string"==typeof e?e:a,c[1]=i;for(var u=2;u<r;u++)c[u]=n[u];return o.createElement.apply(null,c)}return o.createElement.apply(null,n)}d.displayName="MDXCreateElement"},8544:function(e,t,n){n.r(t),n.d(t,{assets:function(){return l},contentTitle:function(){return c},default:function(){return p},frontMatter:function(){return r},metadata:function(){return i},toc:function(){return u}});var o=n(3117),a=(n(7294),n(3905));const r={id:"bloc_builder",title:"Bloc Builder",sidebar_label:"Bloc Builder",hide_title:!0},c=void 0,i={unversionedId:"architecture/bloc/bloc_builder",id:"architecture/bloc/bloc_builder",title:"Bloc Builder",description:"Bloc DSL",source:"@site/docs/architecture/bloc/bloc_builder.md",sourceDirName:"architecture/bloc",slug:"/architecture/bloc/bloc_builder",permalink:"/Kotlin-Bloc/docs/architecture/bloc/bloc_builder",draft:!1,tags:[],version:"current",frontMatter:{id:"bloc_builder",title:"Bloc Builder",sidebar_label:"Bloc Builder",hide_title:!0},sidebar:"architectureSidebar",previous:{title:"Initializer",permalink:"/Kotlin-Bloc/docs/architecture/bloc/initializer"},next:{title:"Lifecycle",permalink:"/Kotlin-Bloc/docs/architecture/bloc/lifecycle"}},l={},u=[{value:"Bloc DSL",id:"bloc-dsl",level:2},{value:"BlocBuilder",id:"blocbuilder",level:2}],s={toc:u};function p(e){let{components:t,...n}=e;return(0,a.kt)("wrapper",(0,o.Z)({},s,n,{components:t,mdxType:"MDXLayout"}),(0,a.kt)("h2",{id:"bloc-dsl"},"Bloc DSL"),(0,a.kt)("p",null,"There's a DSL to make the definition of ",(0,a.kt)("inlineCode",{parentName:"p"},"Blocs")," easy. We have encountered some of that DSL already in the chapters about ",(0,a.kt)("a",{parentName:"p",href:"./reducer"},"Reducers"),", ",(0,a.kt)("a",{parentName:"p",href:"./thunk"},"Thunks")," and ",(0,a.kt)("a",{parentName:"p",href:"./initializer"},"Initializers"),". Here's a (dummy) example to give an overview of all of possible functions :"),(0,a.kt)("pre",null,(0,a.kt)("code",{parentName:"pre",className:"language-kotlin"},'data class CountState(val count: Int)\n\nsealed class CountAction\nobject Increment : CountAction()\nobject Decrement : CountAction()\n\nsealed class SideEffect\nobject LogEvent : SideEffect()\n\nfun bloc(context: BlocContext) = bloc<CountState, CountAction, SideEffect, CountState>(\n    context,\n    blocState(CountState(1))\n) {\n    // Initializer\n    onCreate {\n        logger.d("Bloc is starting")\n    }\n\n    // Thunk\n    thunk {\n        logger.d("Run an asynchronous operation")\n        dispatch(action)\n    }\n\n    // Reducer with side effect\n    reduceAnd<Increment> {\n        state.copy(count = state.count + 1) and LogEvent\n    }\n    // Reducer without side effect\n    reduceAnd<Decrement> {\n        state.copy(count = state.count + 1).noSideEffect()\n    }\n\n    // Reducers without side effects\n    reduce<Increment> { state.copy(count = state.count + 1) }\n    reduce<Decrement> { state.copy(count = state.count - 1) }\n\n    // side effect\n    sideEffect<Decrement> { LogEvent }\n\n    // catch-all reducer with side effect\n    reduce {\n        when (action) {\n            is Increment -> state.copy(count = state.count + 1)\n            else -> state.copy(count = state.count - 1)\n        }\n    }\n\n    // catch-all reducer without side effect\n    reduceAnd {\n        when (action) {\n            is Increment -> state.copy(count = state.count + 1).noSideEffect()\n            else -> state.copy(count = state.count - 1) and LogEvent\n        }\n    }\n}\n')),(0,a.kt)("h2",{id:"blocbuilder"},"BlocBuilder"),(0,a.kt)("p",null,"While this is great to define the ",(0,a.kt)("inlineCode",{parentName:"p"},"Bloc")," functions, there are also helper functions that make the process of declaring ",(0,a.kt)("inlineCode",{parentName:"p"},"Blocs")," even simpler/shorter. In above example the ",(0,a.kt)("inlineCode",{parentName:"p"},"Bloc")," was declared using the full syntax:"),(0,a.kt)("pre",null,(0,a.kt)("code",{parentName:"pre",className:"language-kotlin"},"bloc<CountState, CountAction, SideEffect, CountState>(\n    context,\n    blocState = blocState(CountState(1))\n) {\n")),(0,a.kt)("p",null,"If we only need a standard ",(0,a.kt)("inlineCode",{parentName:"p"},"BlocState")," (see also ",(0,a.kt)("a",{parentName:"p",href:"../blocstate/bloc_state_builder"},"BlocStateBuilder"),") we can replace the ",(0,a.kt)("inlineCode",{parentName:"p"},"blocState")," parameter by an ",(0,a.kt)("inlineCode",{parentName:"p"},"initialValue"),": "),(0,a.kt)("pre",null,(0,a.kt)("code",{parentName:"pre",className:"language-kotlin"},"bloc<CountState, CountAction, SideEffect, CountState>(\n    context,\n    initialValue = CountState(1)\n) {\n")),(0,a.kt)("p",null,"The framework will create a ",(0,a.kt)("inlineCode",{parentName:"p"},"BlocState")," with that initial value automatically."),(0,a.kt)("p",null,"In many cases the ",(0,a.kt)("inlineCode",{parentName:"p"},"State")," and ",(0,a.kt)("inlineCode",{parentName:"p"},"Proposal")," are identical (like in the example above) so we can get rid of the generic type for the ",(0,a.kt)("inlineCode",{parentName:"p"},"Proposal"),":"),(0,a.kt)("pre",null,(0,a.kt)("code",{parentName:"pre",className:"language-kotlin"},"// with blocState\nbloc<CountState, CountAction, SideEffect>(\n    context,\n    blocState = blocState(CountState(1))\n) {\n\n// with initialValue\nbloc<CountState, CountAction, SideEffect>(\n    context,\n    initialValue = CountState(1)\n) {\n")),(0,a.kt)("p",null,"Using this syntax the type of ",(0,a.kt)("inlineCode",{parentName:"p"},"Proposal")," will be inferred as ",(0,a.kt)("inlineCode",{parentName:"p"},"State"),"."),(0,a.kt)("p",null,"If ",(0,a.kt)("inlineCode",{parentName:"p"},"SideEffects")," aren't needed (more often than not we won't need them), we can simplify the syntax even more:"),(0,a.kt)("pre",null,(0,a.kt)("code",{parentName:"pre",className:"language-kotlin"},"// with blocState\nbloc<CountState, CountAction>(\n    context,\n    blocState = blocState(CountState(1))\n) {\n\n// with initialValue\nbloc<CountState, CountAction>(\n    context,\n    initialValue = CountState(1)\n) {\n")),(0,a.kt)("p",null,"Using this syntax the type of ",(0,a.kt)("inlineCode",{parentName:"p"},"SideEffect")," will be set to ",(0,a.kt)("inlineCode",{parentName:"p"},"Unit")," (we can't use ",(0,a.kt)("inlineCode",{parentName:"p"},"Nothing")," because side effects are of type ",(0,a.kt)("inlineCode",{parentName:"p"},"Any")," and ",(0,a.kt)("inlineCode",{parentName:"p"},"Nothing")," is not a sub type of ",(0,a.kt)("inlineCode",{parentName:"p"},"Any"),")."))}p.isMDXComponent=!0}}]);