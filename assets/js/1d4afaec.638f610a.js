"use strict";(self.webpackChunkwebsite=self.webpackChunkwebsite||[]).push([[4704],{3905:function(e,t,n){n.d(t,{Zo:function(){return p},kt:function(){return d}});var r=n(7294);function o(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function c(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function i(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?c(Object(n),!0).forEach((function(t){o(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):c(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function a(e,t){if(null==e)return{};var n,r,o=function(e,t){if(null==e)return{};var n,r,o={},c=Object.keys(e);for(r=0;r<c.length;r++)n=c[r],t.indexOf(n)>=0||(o[n]=e[n]);return o}(e,t);if(Object.getOwnPropertySymbols){var c=Object.getOwnPropertySymbols(e);for(r=0;r<c.length;r++)n=c[r],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(o[n]=e[n])}return o}var l=r.createContext({}),u=function(e){var t=r.useContext(l),n=t;return e&&(n="function"==typeof e?e(t):i(i({},t),e)),n},p=function(e){var t=u(e.components);return r.createElement(l.Provider,{value:t},e.children)},s={inlineCode:"code",wrapper:function(e){var t=e.children;return r.createElement(r.Fragment,{},t)}},f=r.forwardRef((function(e,t){var n=e.components,o=e.mdxType,c=e.originalType,l=e.parentName,p=a(e,["components","mdxType","originalType","parentName"]),f=u(n),d=o,b=f["".concat(l,".").concat(d)]||f[d]||s[d]||c;return n?r.createElement(b,i(i({ref:t},p),{},{components:n})):r.createElement(b,i({ref:t},p))}));function d(e,t){var n=arguments,o=t&&t.mdxType;if("string"==typeof e||o){var c=n.length,i=new Array(c);i[0]=f;var a={};for(var l in t)hasOwnProperty.call(t,l)&&(a[l]=t[l]);a.originalType=e,a.mdxType="string"==typeof e?e:o,i[1]=a;for(var u=2;u<c;u++)i[u]=n[u];return r.createElement.apply(null,i)}return r.createElement.apply(null,n)}f.displayName="MDXCreateElement"},7383:function(e,t,n){n.r(t),n.d(t,{assets:function(){return l},contentTitle:function(){return i},default:function(){return s},frontMatter:function(){return c},metadata:function(){return a},toc:function(){return u}});var r=n(3117),o=(n(7294),n(3905));const c={id:"bloc_context",title:"Bloc Context",sidebar_label:"Bloc Context",hide_title:!0},i=void 0,a={unversionedId:"architecture/bloc/bloc_context",id:"architecture/bloc/bloc_context",title:"Bloc Context",description:"Definition",source:"@site/docs/architecture/bloc/bloc_context.md",sourceDirName:"architecture/bloc",slug:"/architecture/bloc/bloc_context",permalink:"/Kotlin-Bloc/docs/architecture/bloc/bloc_context",draft:!1,tags:[],version:"current",frontMatter:{id:"bloc_context",title:"Bloc Context",sidebar_label:"Bloc Context",hide_title:!0},sidebar:"architectureSidebar",previous:{title:"Lifecycle",permalink:"/Kotlin-Bloc/docs/architecture/bloc/lifecycle"},next:{title:"Coroutine Launcher",permalink:"/Kotlin-Bloc/docs/architecture/bloc/coroutine_launcher"}},l={},u=[{value:"Definition",id:"definition",level:2},{value:"Creation",id:"creation",level:2}],p={toc:u};function s(e){let{components:t,...n}=e;return(0,o.kt)("wrapper",(0,r.Z)({},p,n,{components:t,mdxType:"MDXLayout"}),(0,o.kt)("h2",{id:"definition"},"Definition"),(0,o.kt)("p",null,"A ",(0,o.kt)("inlineCode",{parentName:"p"},"BlocContext")," is an interface (currently) matching ",(0,o.kt)("a",{parentName:"p",href:"https://github.com/arkivanov/Essenty"},"Essenty's LifecycleOwner")," interface:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-kotlin"},"public interface BlocContext : LifecycleOwner\n")),(0,o.kt)("p",null,"If you read the chapter ",(0,o.kt)("a",{parentName:"p",href:"./lifecycle"},"Lifecycle"),", you'll have realized how crucial the lifecycle is for the bloc. The lifecycle controls all three ",(0,o.kt)("inlineCode",{parentName:"p"},"CoroutineScopes")," and thus  the coroutines / jobs. The ",(0,o.kt)("inlineCode",{parentName:"p"},"BlocContext")," is the object passed into a bloc's constructor holding that very lifecycle object."),(0,o.kt)("p",null,"Future releases of the framework might add more functionality to the ",(0,o.kt)("inlineCode",{parentName:"p"},"BlocContext"),"."),(0,o.kt)("h2",{id:"creation"},"Creation"),(0,o.kt)("p",null,"Every bloc takes a ",(0,o.kt)("inlineCode",{parentName:"p"},"BlocContext")," in its constructor. There are extensions on both ",(0,o.kt)("a",{parentName:"p",href:"../../extensions/android/android_bloc_context"},"Android")," and ",(0,o.kt)("a",{parentName:"p",href:"../../extensions/ios/ios_overview"},"iOS")," to create one."),(0,o.kt)("p",null,"Creating a ",(0,o.kt)("inlineCode",{parentName:"p"},"BlocContext")," and a ",(0,o.kt)("inlineCode",{parentName:"p"},"Bloc")," on Android can be as easy as:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-kotlin"},"private val bloc by getOrCreate { bloc(it) }\n\n// more verbose:\nprivate val bloc by getOrCreate { context -> bloc(context) }\n")),(0,o.kt)("p",null,"Note that this bloc will be retained across configuration changes (Android)."))}s.isMDXComponent=!0}}]);