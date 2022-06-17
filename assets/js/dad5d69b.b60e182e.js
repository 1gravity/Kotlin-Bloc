"use strict";(self.webpackChunkwebsite=self.webpackChunkwebsite||[]).push([[3340],{3905:function(e,t,n){n.d(t,{Zo:function(){return p},kt:function(){return m}});var a=n(7294);function o(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function r(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,a)}return n}function l(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?r(Object(n),!0).forEach((function(t){o(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):r(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function i(e,t){if(null==e)return{};var n,a,o=function(e,t){if(null==e)return{};var n,a,o={},r=Object.keys(e);for(a=0;a<r.length;a++)n=r[a],t.indexOf(n)>=0||(o[n]=e[n]);return o}(e,t);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);for(a=0;a<r.length;a++)n=r[a],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(o[n]=e[n])}return o}var c=a.createContext({}),s=function(e){var t=a.useContext(c),n=t;return e&&(n="function"==typeof e?e(t):l(l({},t),e)),n},p=function(e){var t=s(e.components);return a.createElement(c.Provider,{value:t},e.children)},u={inlineCode:"code",wrapper:function(e){var t=e.children;return a.createElement(a.Fragment,{},t)}},d=a.forwardRef((function(e,t){var n=e.components,o=e.mdxType,r=e.originalType,c=e.parentName,p=i(e,["components","mdxType","originalType","parentName"]),d=s(n),m=o,b=d["".concat(c,".").concat(m)]||d[m]||u[m]||r;return n?a.createElement(b,l(l({ref:t},p),{},{components:n})):a.createElement(b,l({ref:t},p))}));function m(e,t){var n=arguments,o=t&&t.mdxType;if("string"==typeof e||o){var r=n.length,l=new Array(r);l[0]=d;var i={};for(var c in t)hasOwnProperty.call(t,c)&&(i[c]=t[c]);i.originalType=e,i.mdxType="string"==typeof e?e:o,l[1]=i;for(var s=2;s<r;s++)l[s]=n[s];return a.createElement.apply(null,l)}return a.createElement.apply(null,n)}d.displayName="MDXCreateElement"},9033:function(e,t,n){n.r(t),n.d(t,{assets:function(){return p},contentTitle:function(){return c},default:function(){return m},frontMatter:function(){return i},metadata:function(){return s},toc:function(){return u}});var a=n(7462),o=n(3366),r=(n(7294),n(3905)),l=["components"],i={id:"bloc_state_builder",title:"Bloc State Builder",sidebar_label:"Bloc State Builder",hide_title:!0},c=void 0,s={unversionedId:"architecture/blocstate/bloc_state_builder",id:"architecture/blocstate/bloc_state_builder",title:"Bloc State Builder",description:"BlocStateBuilder",source:"@site/docs/architecture/blocstate/bloc_state_builder.md",sourceDirName:"architecture/blocstate",slug:"/architecture/blocstate/bloc_state_builder",permalink:"/Kotlin-Bloc/docs/architecture/blocstate/bloc_state_builder",draft:!1,tags:[],version:"current",frontMatter:{id:"bloc_state_builder",title:"Bloc State Builder",sidebar_label:"Bloc State Builder",hide_title:!0},sidebar:"architectureSidebar",previous:{title:"Overview",permalink:"/Kotlin-Bloc/docs/architecture/blocstate/bloc_state"},next:{title:"Bloc Owner",permalink:"/Kotlin-Bloc/docs/architecture/blocowner/bloc_owner"}},p={},u=[{value:"BlocStateBuilder",id:"blocstatebuilder",level:2}],d={toc:u};function m(e){var t=e.components,n=(0,o.Z)(e,l);return(0,r.kt)("wrapper",(0,a.Z)({},d,n,{components:t,mdxType:"MDXLayout"}),(0,r.kt)("h2",{id:"blocstatebuilder"},"BlocStateBuilder"),(0,r.kt)("p",null,"A ",(0,r.kt)("inlineCode",{parentName:"p"},"BlocState")," can be defined using a ",(0,r.kt)("inlineCode",{parentName:"p"},"BlocStateBuilder")),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},"blocState<CountState, CountState> {  \n    initialState = CountState(1)\n \n    accept { proposal, state ->\n        // make a decision whether to accept or reject the proposal\n        // return Null to reject the proposal\n        // return the proposal to accept it (and update the state)\n        proposal\n    }\n}\n")),(0,r.kt)("ul",null,(0,r.kt)("li",{parentName:"ul"},(0,r.kt)("inlineCode",{parentName:"li"},"initialState")," is obviously the initial state of the ",(0,r.kt)("inlineCode",{parentName:"li"},"BlocState")," (and this of the ",(0,r.kt)("inlineCode",{parentName:"li"},"Bloc"),")"),(0,r.kt)("li",{parentName:"ul"},(0,r.kt)("inlineCode",{parentName:"li"},"accept")," is the function that accepts/rejects a proposal and updates the state if it's accepted")),(0,r.kt)("p",null,(0,r.kt)("inlineCode",{parentName:"p"},"initialState")," and ",(0,r.kt)("inlineCode",{parentName:"p"},"accept")," are both mandatory parameters(unfortunately there's no compile time check for this)."),(0,r.kt)("p",null,"Since ",(0,r.kt)("inlineCode",{parentName:"p"},"State")," and ",(0,r.kt)("inlineCode",{parentName:"p"},"Proposal")," are identical in above example, it can be simplified to (no more ",(0,r.kt)("inlineCode",{parentName:"p"},"accept")," function either):"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},"blocState(CountState(1))\n")),(0,r.kt)("p",null,"These are relatively simple / default ",(0,r.kt)("inlineCode",{parentName:"p"},"BlocState")," implementations.\nTo create your own ",(0,r.kt)("inlineCode",{parentName:"p"},"BlocState"),", extend the ",(0,r.kt)("inlineCode",{parentName:"p"},"BlocStateBase")," class which implements the ",(0,r.kt)("inlineCode",{parentName:"p"},"Sink")," and the ",(0,r.kt)("inlineCode",{parentName:"p"},"StateStream")," (which is good enough to implement the ",(0,r.kt)("inlineCode",{parentName:"p"},"BlocState")," interface).\nTypically you'd want to change:"),(0,r.kt)("ol",null,(0,r.kt)("li",{parentName:"ol"},"how ",(0,r.kt)("inlineCode",{parentName:"li"},"Proposals")," are accepted/rejected"),(0,r.kt)("li",{parentName:"ol"},"how/where ",(0,r.kt)("inlineCode",{parentName:"li"},"State")," is stored"),(0,r.kt)("li",{parentName:"ol"},"how/from where ",(0,r.kt)("inlineCode",{parentName:"li"},"State")," is retrieved")),(0,r.kt)("p",null,"An example of the first case is the ",(0,r.kt)("a",{parentName:"p",href:"https://github.com/1gravity/Kotlin-Bloc/blob/feature/documentation/bloc-core/src/commonMain/kotlin/com/onegravity/bloc/state/DefaultBlocState.kt"},"DefaultBlocState")," which is used by the ",(0,r.kt)("inlineCode",{parentName:"p"},"BlocBuilder")," itself:"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},"internal open class DefaultBlocState<State : Any, Proposal : Any>(\n    initialState: State,\n    private val accept: Acceptor<Proposal, State>,\n) : BlocStateBase<State, Proposal>(initialState) {\n\n    override fun send(proposal: Proposal) {\n        accept(proposal, value)?.also { state.send(it) }\n    }\n    \n}\n")),(0,r.kt)("p",null,"All ",(0,r.kt)("inlineCode",{parentName:"p"},"DefaultBlocState")," does is adding the accept/reject functionality."),(0,r.kt)("p",null,"An example of the second and third cases are the ",(0,r.kt)("a",{parentName:"p",href:"https://github.com/1gravity/Kotlin-Bloc/blob/feature/documentation/bloc-samples/src/commonMain/kotlin/com/onegravity/bloc/sample/todo/PersistingToDoState.kt"},"PersistingToDoState")," and the ",(0,r.kt)("a",{parentName:"p",href:"https://github.com/1gravity/Kotlin-Bloc/blob/feature/documentation/bloc-redux/src/commonMain/kotlin/com/onegravity/bloc/redux/ReduxBlocState.kt"},"ReduxBlocState"),"."),(0,r.kt)("p",null,"The former stores state in a local database and retrieves if from the same db:"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},"// inject the database\nprivate val dao = getKoinInstance<ToDoDao>()\n\n// retrieve the db content and send it to the StateStream\ninit {\n    coroutineScope.launch(Dispatchers.Default) {\n        dao.getFlow().collect { state.send(it) }\n    }\n}\n\n// send state to the database\noverride fun send(proposal: List<ToDo>) {\n    proposal.forEach { newTodo ->\n        val oldTodo = value.firstOrNull { it.uuid == newTodo.uuid }\n        if (newTodo != oldTodo) {\n            dao.upsert(newTodo.uuid, newTodo.description, newTodo.completed)\n        }\n    }\n}\n")),(0,r.kt)("p",null,"The latter stores state in a Redux store:"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-kotlin"},"// subscribe to sub state from the Redux store and send it to the StateStream\ninit {\n    // selectScoped will unsubscribe from the store automatically when the Bloc is destroyed\n    // select is a memoized selector to subscribe to the store's sub state\n    store.selectScoped(disposableScope = this, select = select) { model ->\n        state.send(map(model))\n    }\n}\n\n// send state to the Redux store\noverride fun send(proposal: Proposal) {\n    store.dispatch(proposal)\n}\n")))}m.isMDXComponent=!0}}]);