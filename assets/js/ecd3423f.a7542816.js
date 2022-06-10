"use strict";(self.webpackChunkwebsite=self.webpackChunkwebsite||[]).push([[484],{3905:function(e,t,n){n.d(t,{Zo:function(){return p},kt:function(){return f}});var r=n(7294);function o(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function i(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function a(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?i(Object(n),!0).forEach((function(t){o(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):i(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function l(e,t){if(null==e)return{};var n,r,o=function(e,t){if(null==e)return{};var n,r,o={},i=Object.keys(e);for(r=0;r<i.length;r++)n=i[r],t.indexOf(n)>=0||(o[n]=e[n]);return o}(e,t);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(r=0;r<i.length;r++)n=i[r],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(o[n]=e[n])}return o}var s=r.createContext({}),c=function(e){var t=r.useContext(s),n=t;return e&&(n="function"==typeof e?e(t):a(a({},t),e)),n},p=function(e){var t=c(e.components);return r.createElement(s.Provider,{value:t},e.children)},u={inlineCode:"code",wrapper:function(e){var t=e.children;return r.createElement(r.Fragment,{},t)}},d=r.forwardRef((function(e,t){var n=e.components,o=e.mdxType,i=e.originalType,s=e.parentName,p=l(e,["components","mdxType","originalType","parentName"]),d=c(n),f=o,v=d["".concat(s,".").concat(f)]||d[f]||u[f]||i;return n?r.createElement(v,a(a({ref:t},p),{},{components:n})):r.createElement(v,a({ref:t},p))}));function f(e,t){var n=arguments,o=t&&t.mdxType;if("string"==typeof e||o){var i=n.length,a=new Array(i);a[0]=d;var l={};for(var s in t)hasOwnProperty.call(t,s)&&(l[s]=t[s]);l.originalType=e,l.mdxType="string"==typeof e?e:o,a[1]=l;for(var c=2;c<i;c++)a[c]=n[c];return r.createElement.apply(null,a)}return r.createElement.apply(null,n)}d.displayName="MDXCreateElement"},8752:function(e,t,n){n.r(t),n.d(t,{assets:function(){return p},contentTitle:function(){return s},default:function(){return f},frontMatter:function(){return l},metadata:function(){return c},toc:function(){return u}});var r=n(7462),o=n(3366),i=(n(7294),n(3905)),a=["components"],l={id:"overview",title:"Extensions Overview",sidebar_label:"Overview",hide_title:!0},s=void 0,c={unversionedId:"extensions/overview",id:"extensions/overview",title:"Extensions Overview",description:"Overview",source:"@site/docs/extensions/overview.md",sourceDirName:"extensions",slug:"/extensions/overview",permalink:"/Kotlin-Bloc/docs/extensions/overview",draft:!1,tags:[],version:"current",frontMatter:{id:"overview",title:"Extensions Overview",sidebar_label:"Overview",hide_title:!0},sidebar:"extensionsSidebar",next:{title:"BlocContext",permalink:"/Kotlin-Bloc/docs/extensions/android/android_bloc_context"}},p={},u=[{value:"Overview",id:"overview",level:2}],d={toc:u};function f(e){var t=e.components,n=(0,o.Z)(e,a);return(0,i.kt)("wrapper",(0,r.Z)({},d,n,{components:t,mdxType:"MDXLayout"}),(0,i.kt)("h2",{id:"overview"},"Overview"),(0,i.kt)("p",null,"While ",(0,i.kt)("inlineCode",{parentName:"p"},"Kotlin Bloc"),' is a platform-agnostic framework, apps ultimately run on a specific platform with a specific view technology. One of the architectural goals is to be "be as un-opinionated as possible" to support different technologies like ',(0,i.kt)("inlineCode",{parentName:"p"},"Storyboard")," / ",(0,i.kt)("inlineCode",{parentName:"p"},"Swift UI")," on iOS or ",(0,i.kt)("inlineCode",{parentName:"p"},"Jetpack Compose")," / XML layouts + view or data binding on Android. Especially on Android there are many different technologies to implement the user interface: Activities, Fragments, ViewModels, view binding, data binding, RxJava, live data, Kotlin Flow, XML layouts and Jetpack Compose to name the most important ones."),(0,i.kt)("p",null,"The purpose of ",(0,i.kt)("inlineCode",{parentName:"p"},"Kotlin-Bloc")," extensions is to support different technologies because, let's face it, most companies can't afford a greenfield approach but need to improve an existing product with existing technologies. ",(0,i.kt)("inlineCode",{parentName:"p"},"Kotlin-Bloc")," can be added to literally any mobile tech stack, new features can be built using blocs and existing features can be migrated one step at a time.\nWhile ",(0,i.kt)("inlineCode",{parentName:"p"},"Kotlin-Bloc"),' was built with multi-platform in mind, it\'s also a great framework for building "just" a native Android app and the numerous ',(0,i.kt)("a",{parentName:"p",href:"./android/android_bloc_context"},"Android Extensions")," can eliminate lots of existing boilerplate code."))}f.isMDXComponent=!0}}]);