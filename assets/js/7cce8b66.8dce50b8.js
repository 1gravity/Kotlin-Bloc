"use strict";(self.webpackChunkwebsite=self.webpackChunkwebsite||[]).push([[3800],{3905:function(e,t,r){r.d(t,{Zo:function(){return p},kt:function(){return f}});var o=r(7294);function n(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function i(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);t&&(o=o.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,o)}return r}function l(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{};t%2?i(Object(r),!0).forEach((function(t){n(e,t,r[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):i(Object(r)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(r,t))}))}return e}function a(e,t){if(null==e)return{};var r,o,n=function(e,t){if(null==e)return{};var r,o,n={},i=Object.keys(e);for(o=0;o<i.length;o++)r=i[o],t.indexOf(r)>=0||(n[r]=e[r]);return n}(e,t);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(o=0;o<i.length;o++)r=i[o],t.indexOf(r)>=0||Object.prototype.propertyIsEnumerable.call(e,r)&&(n[r]=e[r])}return n}var s=o.createContext({}),c=function(e){var t=o.useContext(s),r=t;return e&&(r="function"==typeof e?e(t):l(l({},t),e)),r},p=function(e){var t=c(e.components);return o.createElement(s.Provider,{value:t},e.children)},u={inlineCode:"code",wrapper:function(e){var t=e.children;return o.createElement(o.Fragment,{},t)}},b=o.forwardRef((function(e,t){var r=e.components,n=e.mdxType,i=e.originalType,s=e.parentName,p=a(e,["components","mdxType","originalType","parentName"]),b=c(r),f=n,m=b["".concat(s,".").concat(f)]||b[f]||u[f]||i;return r?o.createElement(m,l(l({ref:t},p),{},{components:r})):o.createElement(m,l({ref:t},p))}));function f(e,t){var r=arguments,n=t&&t.mdxType;if("string"==typeof e||n){var i=r.length,l=new Array(i);l[0]=b;var a={};for(var s in t)hasOwnProperty.call(t,s)&&(a[s]=t[s]);a.originalType=e,a.mdxType="string"==typeof e?e:n,l[1]=a;for(var c=2;c<i;c++)l[c]=r[c];return o.createElement.apply(null,l)}return o.createElement.apply(null,r)}b.displayName="MDXCreateElement"},6888:function(e,t,r){r.r(t),r.d(t,{assets:function(){return s},contentTitle:function(){return l},default:function(){return u},frontMatter:function(){return i},metadata:function(){return a},toc:function(){return c}});var o=r(3117),n=(r(7294),r(3905));const i={id:"ios_bloc_observer",title:"Bloc Observer",sidebar_label:"Bloc Observer",hide_title:!0},l=void 0,a={unversionedId:"extensions/ios/ios_bloc_observer",id:"extensions/ios/ios_bloc_observer",title:"Bloc Observer",description:"BlocObserver",source:"@site/docs/extensions/ios/bloc_observer.md",sourceDirName:"extensions/ios",slug:"/extensions/ios/ios_bloc_observer",permalink:"/Kotlin-Bloc/docs/extensions/ios/ios_bloc_observer",draft:!1,tags:[],version:"current",frontMatter:{id:"ios_bloc_observer",title:"Bloc Observer",sidebar_label:"Bloc Observer",hide_title:!0},sidebar:"extensionsSidebar",previous:{title:"Bloc Component",permalink:"/Kotlin-Bloc/docs/extensions/ios/ios_bloc_component"},next:{title:"Motivation",permalink:"/Kotlin-Bloc/docs/extensions/redux/redux_motivation"}},s={},c=[{value:"BlocObserver",id:"blocobserver",level:2}],p={toc:c};function u(e){let{components:t,...r}=e;return(0,n.kt)("wrapper",(0,o.Z)({},p,r,{components:t,mdxType:"MDXLayout"}),(0,n.kt)("h2",{id:"blocobserver"},"BlocObserver"),(0,n.kt)("p",null,"A bloc exposes a ",(0,n.kt)("inlineCode",{parentName:"p"},"StateStream")," and a ",(0,n.kt)("inlineCode",{parentName:"p"},"SideEffectStream")," which are essentially Kotlin Flows. The question is how can a Kotlin Flow be observed in Swift? Different solutions have been proposed:"),(0,n.kt)("ul",null,(0,n.kt)("li",{parentName:"ul"},(0,n.kt)("a",{parentName:"li",href:"https://betterprogramming.pub/using-kotlin-flow-in-swift-3e7b53f559b6"},"https://betterprogramming.pub/using-kotlin-flow-in-swift-3e7b53f559b6")),(0,n.kt)("li",{parentName:"ul"},(0,n.kt)("a",{parentName:"li",href:"https://johnoreilly.dev/posts/kotlinmultiplatform-swift-combine_publisher-flow/"},"https://johnoreilly.dev/posts/kotlinmultiplatform-swift-combine_publisher-flow/")),(0,n.kt)("li",{parentName:"ul"},(0,n.kt)("a",{parentName:"li",href:"https://dev.to/touchlab/working-with-kotlin-coroutines-and-rxswift-24fa"},"https://dev.to/touchlab/working-with-kotlin-coroutines-and-rxswift-24fa")),(0,n.kt)("li",{parentName:"ul"},(0,n.kt)("a",{parentName:"li",href:"https://github.com/FutureMind/koru"},"https://github.com/FutureMind/koru")),(0,n.kt)("li",{parentName:"ul"},(0,n.kt)("a",{parentName:"li",href:"https://touchlab.co/kotlin-coroutines-swift-revisited/"},"https://touchlab.co/kotlin-coroutines-swift-revisited/"))),(0,n.kt)("p",null,"Some of these solutions are more generic than what we need for our purpose which is simply be able to observe the streams in SwiftUI and update the view when state changes."),(0,n.kt)("p",null,"The ",(0,n.kt)("a",{parentName:"p",href:"https://github.com/1gravity/Kotlin-Bloc/blob/master/iosApp/iosApp/utils/BlocObserver.swift"},"BlocObserver")," class:"),(0,n.kt)("ul",null,(0,n.kt)("li",{parentName:"ul"},"observes both streams and exposes them as ",(0,n.kt)("a",{parentName:"li",href:"https://developer.apple.com/documentation/combine/published"},"@Published")," properties (-> the ",(0,n.kt)("inlineCode",{parentName:"li"},"BlocObserver")," needs to be an ",(0,n.kt)("a",{parentName:"li",href:"https://developer.apple.com/documentation/combine/observableobject"},"ObservableObject"),")"),(0,n.kt)("li",{parentName:"ul"},(0,n.kt)("inlineCode",{parentName:"li"},"state")," and ",(0,n.kt)("inlineCode",{parentName:"li"},"sideEffect")," are the @Published properties than can be observed by the view"),(0,n.kt)("li",{parentName:"ul"},"creates a lifecycle used to ",(0,n.kt)("inlineCode",{parentName:"li"},"unsubscribe"),' from the bloc, the lifecycle is tied to the "lifecycle" of the ',(0,n.kt)("inlineCode",{parentName:"li"},"BlocObserver")," object itself")),(0,n.kt)("pre",null,(0,n.kt)("code",{parentName:"pre",className:"language-swift"},"    private let holder = BlocHolder { CalculatorKt.bloc(context: $0) }\n    \n    @ObservedObject\n    private var model: BlocObserver<CalculatorState, CalculatorAction, KotlinUnit>\n\n    init() {\n        self.model = BlocObserver(self.holder.value)\n    }\n")),(0,n.kt)("admonition",{type:"tip"},(0,n.kt)("p",{parentName:"admonition"},"The lifecycle is tied to the lifecycle of the ",(0,n.kt)("inlineCode",{parentName:"p"},"BlocObserver")," object itself. As with ",(0,n.kt)("inlineCode",{parentName:"p"},"BlocHolder")," that means that you need to keep an explicit reference to that object like in the example above. See also: ",(0,n.kt)("a",{parentName:"p",href:"/Kotlin-Bloc/docs/extensions/ios/ios_bloc_holder#"},"BlocHolder"))))}u.isMDXComponent=!0}}]);