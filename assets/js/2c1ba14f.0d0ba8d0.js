"use strict";(self.webpackChunkwebsite=self.webpackChunkwebsite||[]).push([[5444],{3905:function(e,t,n){n.d(t,{Zo:function(){return s},kt:function(){return h}});var a=n(7294);function r(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function i(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,a)}return n}function o(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?i(Object(n),!0).forEach((function(t){r(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):i(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function l(e,t){if(null==e)return{};var n,a,r=function(e,t){if(null==e)return{};var n,a,r={},i=Object.keys(e);for(a=0;a<i.length;a++)n=i[a],t.indexOf(n)>=0||(r[n]=e[n]);return r}(e,t);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(a=0;a<i.length;a++)n=i[a],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(r[n]=e[n])}return r}var c=a.createContext({}),u=function(e){var t=a.useContext(c),n=t;return e&&(n="function"==typeof e?e(t):o(o({},t),e)),n},s=function(e){var t=u(e.components);return a.createElement(c.Provider,{value:t},e.children)},d={inlineCode:"code",wrapper:function(e){var t=e.children;return a.createElement(a.Fragment,{},t)}},p=a.forwardRef((function(e,t){var n=e.components,r=e.mdxType,i=e.originalType,c=e.parentName,s=l(e,["components","mdxType","originalType","parentName"]),p=u(n),h=r,k=p["".concat(c,".").concat(h)]||p[h]||d[h]||i;return n?a.createElement(k,o(o({ref:t},s),{},{components:n})):a.createElement(k,o({ref:t},s))}));function h(e,t){var n=arguments,r=t&&t.mdxType;if("string"==typeof e||r){var i=n.length,o=new Array(i);o[0]=p;var l={};for(var c in t)hasOwnProperty.call(t,c)&&(l[c]=t[c]);l.originalType=e,l.mdxType="string"==typeof e?e:r,o[1]=l;for(var u=2;u<i;u++)o[u]=n[u];return a.createElement.apply(null,o)}return a.createElement.apply(null,n)}p.displayName="MDXCreateElement"},5437:function(e,t,n){n.r(t),n.d(t,{assets:function(){return c},contentTitle:function(){return o},default:function(){return d},frontMatter:function(){return i},metadata:function(){return l},toc:function(){return u}});var a=n(3117),r=(n(7294),n(3905));const i={id:"thunk",title:"Thunk",sidebar_label:"Thunk",hide_title:!0},o=void 0,l={unversionedId:"architecture/bloc/thunk",id:"architecture/bloc/thunk",title:"Thunk",description:"Definition",source:"@site/docs/architecture/bloc/thunk.md",sourceDirName:"architecture/bloc",slug:"/architecture/bloc/thunk",permalink:"/Kotlin-Bloc/docs/architecture/bloc/thunk",draft:!1,tags:[],version:"current",frontMatter:{id:"thunk",title:"Thunk",sidebar_label:"Thunk",hide_title:!0},sidebar:"architectureSidebar",previous:{title:"Reducer",permalink:"/Kotlin-Bloc/docs/architecture/bloc/reducer"},next:{title:"Initializer",permalink:"/Kotlin-Bloc/docs/architecture/bloc/initializer"}},c={},u=[{value:"Definition",id:"definition",level:2},{value:"Context",id:"context",level:3},{value:"reduce()",id:"reduce",level:3},{value:"dispatch()",id:"dispatch",level:3},{value:"Execution",id:"execution",level:2},{value:"Example 1",id:"example-1",level:3},{value:"Example 2",id:"example-2",level:3},{value:"Example 3",id:"example-3",level:3}],s={toc:u};function d(e){let{components:t,...n}=e;return(0,r.kt)("wrapper",(0,a.Z)({},s,n,{components:t,mdxType:"MDXLayout"}),(0,r.kt)("h2",{id:"definition"},"Definition"),(0,r.kt)("p",null,"To reiterate:"),(0,r.kt)("blockquote",null,(0,r.kt)("p",{parentName:"blockquote"},'The word "thunk" is a programming term that means "a piece of code that does some delayed work". Rather than execute some logic now, we can write a function body or code that can be used to perform the work later.',(0,r.kt)("br",{parentName:"p"}),"\n",(0,r.kt)("a",{parentName:"p",href:"https://redux.js.org/usage/writing-logic-thunks"},"https://redux.js.org/usage/writing-logic-thunks"))),(0,r.kt)("p",null,"While a Redux thunk is a function, dispatched as an action to a Redux store and processed by the redux-thunk middleware, a ",(0,r.kt)("inlineCode",{parentName:"p"},"Kotlin Bloc")," thunk is not dispatched as an action but triggered the same way a reducer is triggered, by reacting to an ",(0,r.kt)("inlineCode",{parentName:"p"},"Action")," that was sent to the ",(0,r.kt)("inlineCode",{parentName:"p"},"Bloc"),". On top of that it's also:"),(0,r.kt)("ol",null,(0,r.kt)("li",{parentName:"ol"},"a suspending function"),(0,r.kt)("li",{parentName:"ol"},'actions are dispatched to the "next" thunk or reducer in the execution chain ')),(0,r.kt)("h3",{id:"context"},"Context"),(0,r.kt)("p",null,"A thunk is called with a ",(0,r.kt)("inlineCode",{parentName:"p"},"ThunkContext")," as receiver. The context is giving access to the current ",(0,r.kt)("inlineCode",{parentName:"p"},"State"),", the ",(0,r.kt)("inlineCode",{parentName:"p"},"Action")," that triggered the thunk's execution, a ",(0,r.kt)("inlineCode",{parentName:"p"},"Dispatcher"),' and a function to "reduce" state directly:'),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},"public data class ThunkContext<State, Action>(\n    val getState: GetState<State>,\n    val action: Action,\n    val dispatch: Dispatcher<Action>,\n    val reduce: (proposal: Proposal) -> Unit\n)\n")),(0,r.kt)("p",null,"A typical thunk would evaluate the action, run some asynchronous operation and dispatch actions to be processed by other thunks or reducers:"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},"thunk {\n    if (action == Load) {\n        dispatch(Loading)\n        val books = repository.load()\n        dispatch(LoadComplete(books))\n    } else {\n        // without this no action would reach the reducers \n        // below because this is a catch-all thunk\n        dispatch(action)\n    }\n}\n\nreduce<Loading> { \n    state.copy(loading = true)\n}\n\nreduce<LoadComplete> { \n    state.copy(loading = false, books = action.books)\n}\n")),(0,r.kt)("admonition",{type:"tip"},(0,r.kt)("p",{parentName:"admonition"},"The catch-all ",(0,r.kt)("inlineCode",{parentName:"p"},"thunk { }")," needs to call ",(0,r.kt)("inlineCode",{parentName:"p"},"dispatch(action)")," explicitly or no reducers will be executed (see ",(0,r.kt)("a",{parentName:"p",href:"#execution"},"Execution"),"). ")),(0,r.kt)("p",null,"In this case using a single action thunk would be simpler though and you'd write:"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},"thunk<Load> {\n    dispatch(Loading)\n    val books = repository.load()\n    dispatch(LoadComplete(books))\n}\n")),(0,r.kt)("p",null,"This doesn't require to call ",(0,r.kt)("inlineCode",{parentName:"p"},"dispatch(action)")," explicitly since it only catches a single action (",(0,r.kt)("inlineCode",{parentName:"p"},"Load"),") and then dispatches its own actions."),(0,r.kt)("admonition",{type:"tip"},(0,r.kt)("p",{parentName:"admonition"},"There are extension functions to launch ",(0,r.kt)("inlineCode",{parentName:"p"},"Coroutines")," from a thunk (see ",(0,r.kt)("a",{parentName:"p",href:"coroutine_launcher"},"Coroutine Launcher"),"). ")),(0,r.kt)("h3",{id:"reduce"},"reduce()"),(0,r.kt)("p",null,"Thunks are meant to run asynchronous code and reducers are meant to reduce state. In many cases however the reducers are very simple functions. In the example above we need to add a dedicated action ",(0,r.kt)("inlineCode",{parentName:"p"},"Loading"),', dispatch that action in the thunk in order for a reducer to reduce the current state to one that indicates loading. While that "separation of concerns" is useful in many cases, it adds a good amount of boilerplate code for simple cases like the one we have here. To simplify this we can use the ',(0,r.kt)("inlineCode",{parentName:"p"},"ThunkContext.reduce")," function:"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},"thunk<Load> {\n    reduce(getState().copy(loading = true))\n    val books = repository.load()\n    reduce(getState().copy(loading = false, books = books))\n}\n")),(0,r.kt)("admonition",{type:"tip"},(0,r.kt)("p",{parentName:"admonition"},"Reducers are executed in the order they are added to the reducer queue (see ",(0,r.kt)("a",{parentName:"p",href:"reducer#concurrency"},"Reducer Concurrency"),"). When ",(0,r.kt)("inlineCode",{parentName:"p"},"reduce()")," is called from a thunk or an initializer, that reducer function is also added to the queue to guarantee the correct order of execution but the reduce call itself will suspend till the queued reducer was executed. This guarantees that this always succeeds:"),(0,r.kt)("pre",{parentName:"admonition"},(0,r.kt)("code",{parentName:"pre"},"thunk {\n    reduce(newState)                    // <- suspends till the state reduction is done\n    assertEquals(getState(), newState)  // <- assertion is always true\n}\n"))),(0,r.kt)("h3",{id:"dispatch"},"dispatch()"),(0,r.kt)("p",null,"When dispatching actions to reducers, the dispatch and the subsequent state reduction will happen in a blocking / suspending manner. Similar to ",(0,r.kt)("inlineCode",{parentName:"p"},"reduce()"),", a call to ",(0,r.kt)("inlineCode",{parentName:"p"},"dispatch()")," will suspend and continue once the reducer is done. This is NOT true if the action was dispatched to another thunk though, only actions dispatched to reducers are processed synchronously:"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},"thunk {\n    val state = getState()\n    dispatch(Increment)                 // <- suspends till the reducer has run\n    assertEquals(getState(), state + 1) // <- assertion is always true\n}\nreduce<Increment> {\n    state + 1\n}\n")),(0,r.kt)("h2",{id:"execution"},"Execution"),(0,r.kt)("p",null,"A lot of what was said in the ",(0,r.kt)("a",{parentName:"p",href:"reducer#execution"},"reducer documentation")," applies to ",(0,r.kt)("inlineCode",{parentName:"p"},"thunks")," too but there are important differences. Here are the rules:"),(0,r.kt)("ol",null,(0,r.kt)("li",{parentName:"ol"},"If there's more than one reducer matching an action, only the first one will be executed (can't reduce the same state twice).",(0,r.kt)("br",{parentName:"li"}),(0,r.kt)("strong",{parentName:"li"},"Rule 1"),": for thunks, ",(0,r.kt)("strong",{parentName:"li"},"every matching thunk will be executed"),"."),(0,r.kt)("li",{parentName:"ol"},"Reducers are executed by matching action, the order of declaration only matters when there's more than one matching reducer.",(0,r.kt)("br",{parentName:"li"}),(0,r.kt)("strong",{parentName:"li"},"Rule 2"),": for thunks, ",(0,r.kt)("strong",{parentName:"li"},"the order of declaration is crucial")," in determining which one executes first especially because every matching thunk will be executed (rule 1)."),(0,r.kt)("li",{parentName:"ol"},(0,r.kt)("strong",{parentName:"li"},"Rule 3"),": when a thunk dispatches an action, it's dispatched to all matching thunks declared after the dispatching thunk (or we would end up with an endless loop). If no matching thunk is found, the action is dispatched to the first matching reducer.")),(0,r.kt)("h3",{id:"example-1"},"Example 1"),(0,r.kt)("p",null,"An example of rule 1 and 2:"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},'thunk<Increment> {\n    println("thunk 1")\n}\n\nthunk<Increment> {\n    println("thunk 2")\n}\n\nthunk {\n    println("thunk 3")\n}\n\nreduce {\n    println("reducer")\n    state\n}\n')),(0,r.kt)("p",null,"Above thunks will all be executed in the order of their declaration (sequentially). The output will be:"),(0,r.kt)("blockquote",null,(0,r.kt)("p",{parentName:"blockquote"},"thunk 1",(0,r.kt)("br",{parentName:"p"}),"\n","thunk 2",(0,r.kt)("br",{parentName:"p"}),"\n","thunk 3")),(0,r.kt)("p",null,"The reducer won't be executed as none of the thunks dispatches any action. "),(0,r.kt)("h3",{id:"example-2"},"Example 2"),(0,r.kt)("p",null,"Try to guess what happens when the first thunk dispatches an ",(0,r.kt)("inlineCode",{parentName:"p"},"Increment")," action (all three rules apply):"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},'thunk<Increment> {\n    println("thunk 1")\n    dispatch(Increment)\n}\n\nthunk<Increment> {\n    println("thunk 2")\n}\n\nthunk {\n    println("thunk 3")\n}\n\nreduce {\n    println("reducer")\n    state\n}\n')),(0,r.kt)("p",null,"The output will be:"),(0,r.kt)("blockquote",null,(0,r.kt)("p",{parentName:"blockquote"},"thunk 1",(0,r.kt)("br",{parentName:"p"}),"\n","thunk 2",(0,r.kt)("br",{parentName:"p"}),"\n","thunk 3",(0,r.kt)("br",{parentName:"p"}),"\n","thunk 2",(0,r.kt)("br",{parentName:"p"}),"\n","thunk 3")),(0,r.kt)("ul",null,(0,r.kt)("li",{parentName:"ul"},"the original ",(0,r.kt)("inlineCode",{parentName:"li"},"Increment")," action is dispatched to the first thunk",(0,r.kt)("br",{parentName:"li"}),"-> outputs: ",(0,r.kt)("inlineCode",{parentName:"li"},"thunk 1")),(0,r.kt)("li",{parentName:"ul"},"the first thunk dispatches a second ",(0,r.kt)("inlineCode",{parentName:"li"},"Increment")," action to all matching thunks declared after the dispatching thunk (rule 3)"),(0,r.kt)("li",{parentName:"ul"},"thunk 2 is the first to process the second ",(0,r.kt)("inlineCode",{parentName:"li"},"Increment")," action",(0,r.kt)("br",{parentName:"li"}),"-> outputs: ",(0,r.kt)("inlineCode",{parentName:"li"},"thunk 2")),(0,r.kt)("li",{parentName:"ul"},"thunk 3 is next in line to process the second ",(0,r.kt)("inlineCode",{parentName:"li"},"Increment")," action",(0,r.kt)("br",{parentName:"li"}),"-> outputs: ",(0,r.kt)("inlineCode",{parentName:"li"},"thunk 3")),(0,r.kt)("li",{parentName:"ul"},"the original ",(0,r.kt)("inlineCode",{parentName:"li"},"Increment")," action is dispatched to the second thunk",(0,r.kt)("br",{parentName:"li"}),"-> outputs: ",(0,r.kt)("inlineCode",{parentName:"li"},"thunk 2")),(0,r.kt)("li",{parentName:"ul"},"the original ",(0,r.kt)("inlineCode",{parentName:"li"},"Increment")," action is dispatched to the third thunk",(0,r.kt)("br",{parentName:"li"}),"-> outputs: ",(0,r.kt)("inlineCode",{parentName:"li"},"thunk 3")),(0,r.kt)("li",{parentName:"ul"},"nothing reaches the reducer")),(0,r.kt)("p",null,"While this sounds complicated, the rules are pretty straight forward. Examples like the one above are also rather academic and have little relevance for real applications. "),(0,r.kt)("h3",{id:"example-3"},"Example 3"),(0,r.kt)("p",null,"Here's a more realistic example:"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},"thunk<LoadUser> {\n    dispatch(UserLoading)\n    val result = repo.loadUser()\n    dispatch(UserLoaded(result))\n}\n\nthunk<UserLoading> {\n    dispatch(AccountLoading)\n    val result = repo.loadAccount()\n    dispatch(AccountLoaded(result))\n}\n\nreduce<UserLoading> {\n    // this won't be executed because the second thunk captures the UserLoading action\n}\n\nreduce<AccountLoading> {\n    state.copy(loading = true)\n}\n\nreduce<UserLoaded> {\n    state.copy(user = action.result)\n}\n\nreduce<AccountLoaded> {\n    state.copy(loading = false, account = action.result)\n}\n")))}d.isMDXComponent=!0}}]);