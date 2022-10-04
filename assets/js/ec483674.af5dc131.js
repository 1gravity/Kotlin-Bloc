"use strict";(self.webpackChunkwebsite=self.webpackChunkwebsite||[]).push([[7182],{3905:function(e,t,n){n.d(t,{Zo:function(){return s},kt:function(){return m}});var a=n(7294);function l(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function r(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,a)}return n}function o(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?r(Object(n),!0).forEach((function(t){l(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):r(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function i(e,t){if(null==e)return{};var n,a,l=function(e,t){if(null==e)return{};var n,a,l={},r=Object.keys(e);for(a=0;a<r.length;a++)n=r[a],t.indexOf(n)>=0||(l[n]=e[n]);return l}(e,t);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);for(a=0;a<r.length;a++)n=r[a],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(l[n]=e[n])}return l}var c=a.createContext({}),u=function(e){var t=a.useContext(c),n=t;return e&&(n="function"==typeof e?e(t):o(o({},t),e)),n},s=function(e){var t=u(e.components);return a.createElement(c.Provider,{value:t},e.children)},d={inlineCode:"code",wrapper:function(e){var t=e.children;return a.createElement(a.Fragment,{},t)}},p=a.forwardRef((function(e,t){var n=e.components,l=e.mdxType,r=e.originalType,c=e.parentName,s=i(e,["components","mdxType","originalType","parentName"]),p=u(n),m=l,v=p["".concat(c,".").concat(m)]||p[m]||d[m]||r;return n?a.createElement(v,o(o({ref:t},s),{},{components:n})):a.createElement(v,o({ref:t},s))}));function m(e,t){var n=arguments,l=t&&t.mdxType;if("string"==typeof e||l){var r=n.length,o=new Array(r);o[0]=p;var i={};for(var c in t)hasOwnProperty.call(t,c)&&(i[c]=t[c]);i.originalType=e,i.mdxType="string"==typeof e?e:l,o[1]=i;for(var u=2;u<r;u++)o[u]=n[u];return a.createElement.apply(null,o)}return a.createElement.apply(null,n)}p.displayName="MDXCreateElement"},6286:function(e,t,n){n.r(t),n.d(t,{assets:function(){return c},contentTitle:function(){return o},default:function(){return d},frontMatter:function(){return r},metadata:function(){return i},toc:function(){return u}});var a=n(3117),l=(n(7294),n(3905));const r={id:"calculator",title:"Calculator",sidebar_label:"Calculator",hide_title:!0},o=void 0,i={unversionedId:"examples/calculator",id:"examples/calculator",title:"Calculator",description:"Calculator",source:"@site/docs/examples/calculator.md",sourceDirName:"examples",slug:"/examples/calculator",permalink:"/Kotlin-Bloc/docs/examples/calculator",draft:!1,tags:[],version:"current",frontMatter:{id:"calculator",title:"Calculator",sidebar_label:"Calculator",hide_title:!0},sidebar:"exampleSidebar",previous:{title:"Books",permalink:"/Kotlin-Bloc/docs/examples/books"},next:{title:"Posts",permalink:"/Kotlin-Bloc/docs/examples/posts"}},c={},u=[{value:"Calculator",id:"calculator",level:2},{value:"Calculator 1",id:"calculator-1",level:3},{value:"Android",id:"android",level:4},{value:"iOS",id:"ios",level:4},{value:"Calculator 2",id:"calculator-2",level:3},{value:"Android",id:"android-1",level:4},{value:"iOS",id:"ios-1",level:4}],s={toc:u};function d(e){let{components:t,...r}=e;return(0,l.kt)("wrapper",(0,a.Z)({},s,r,{components:t,mdxType:"MDXLayout"}),(0,l.kt)("h2",{id:"calculator"},"Calculator"),(0,l.kt)("p",null,(0,l.kt)("img",{alt:"Calculator",src:n(2228).Z,width:"1950",height:"1804"})),(0,l.kt)("p",null,"The two calculator sample apps (adapted from ",(0,l.kt)("a",{parentName:"p",href:"https://orbit-mvi.org"},"Orbit"),") demonstrate:"),(0,l.kt)("ul",null,(0,l.kt)("li",{parentName:"ul"},"how to use ",(0,l.kt)("inlineCode",{parentName:"li"},"Blocs")," in iOS"),(0,l.kt)("li",{parentName:"ul"},"how to use ",(0,l.kt)("inlineCode",{parentName:"li"},"Blocs")," with or without Android ViewModel"),(0,l.kt)("li",{parentName:"ul"},"how to use ",(0,l.kt)("a",{parentName:"li",href:"https://developer.android.com/topic/libraries/data-binding"},"data binding")," with ",(0,l.kt)("inlineCode",{parentName:"li"},"Blocs")," on Android"),(0,l.kt)("li",{parentName:"ul"},"how to use sealed classes or enums classes as ",(0,l.kt)("inlineCode",{parentName:"li"},"Action"))),(0,l.kt)("h3",{id:"calculator-1"},"Calculator 1"),(0,l.kt)("p",null,"The more conventional implementation uses a sealed class as Action:"),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-kotlin"},"sealed class CalculatorAction {\n    object Clear : CalculatorAction()\n    object Add : CalculatorAction()\n    data class Digit(val digit: Int) : CalculatorAction()\n   // more actions ...\n}\n")),(0,l.kt)("p",null,"which are processed using single-action reducers:"),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-kotlin"},"fun bloc(context: BlocContext) = bloc<CalculatorState, CalculatorAction>(context, CalculatorState()) {\n    reduce<Clear> { CalculatorState() }\n    reduce<Add> { state.resetErrors().apply(Operator.Add) }\n    reduce<Digit> { state.resetErrors().digit(action.digit) }\n    // more reducers...\n")),(0,l.kt)("h4",{id:"android"},"Android"),(0,l.kt)("p",null,"On Android ",(0,l.kt)("inlineCode",{parentName:"p"},"toLiveData()")," converts the ",(0,l.kt)("inlineCode",{parentName:"p"},"StateStream")," to ",(0,l.kt)("a",{parentName:"p",href:"https://developer.android.com/topic/libraries/architecture/livedata"},"LiveData")," so it can be bound to the view using data binding:"),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-kotlin"},"class CalculatorViewModel : ViewModel() {\n\n    private val bloc = bloc(blocContext())\n\n    val state = toLiveData(bloc)\n")),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-xml"},'<data>\n  <variable\n    name="viewmodel"\n    type="com.onegravity.bloc.calculator.CalculatorViewModel" />\n</data>\n\n\x3c!-- display State --\x3e\n<TextView\n  android:text="@{viewmodel.state.toString()}"/>\n\n\x3c!-- send an Action --\x3e\n<Button\n  android:onClick="@{(view) -> viewmodel.button(view.id)}"/>\n')),(0,l.kt)("h4",{id:"ios"},"iOS"),(0,l.kt)("p",null,"On iOS the standard ",(0,l.kt)("a",{parentName:"p",href:"/Kotlin-Bloc/docs/extensions/ios/ios_bloc_holder"},"BlocHolder"),"/ ",(0,l.kt)("a",{parentName:"p",href:"/Kotlin-Bloc/docs/extensions/ios/ios_bloc_observer"},"BlocObserver")," pattern is used:"),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-swift"},'struct CalculatorView: View {\n    private let holder = BlocHolder { CalculatorKt.bloc(context: $0) }\n    \n    @ObservedObject\n    private var model: BlocObserver<CalculatorState, CalculatorAction, KotlinUnit>\n\n    init() {\n        self.model = BlocObserver(self.holder.value)\n    }\n\n    var body: some View {\n\n      // ...\n        \n      // display State\n      Text("\\(model.value)")\n\n      // ...\n\n      Button(action: {\n        // send an Action\n        holder.value.send(value: CalculatorAction.Clear())\n      }) {\n        Text("Clear")\n      }\n')),(0,l.kt)("h3",{id:"calculator-2"},"Calculator 2"),(0,l.kt)("p",null,"The second calculator uses enum classes as ",(0,l.kt)("inlineCode",{parentName:"p"},"Action"),":"),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-kotlin"},"enum class ActionEnum(val digit: Int? = null) {\n    Clear,\n    Add,\n    Digit0(0),\n    Digit1(1),\n    // more actions...\n}\n")),(0,l.kt)("p",null,"which are also processed using single-action reducers but with a slightly different syntax:"),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-kotlin"},"fun blocEnum(context: BlocContext) = bloc<CalculatorState, ActionEnum>(context, CalculatorState()) {\n  reduce(ActionEnum.Clear) { CalculatorState() }\n  reduce(ActionEnum.Add) { state.resetErrors().apply(Operator.Add) }\n  reduce(ActionEnum.Subtract) { state.resetErrors().apply(Operator.Subtract) }\n// more reducers...\n")),(0,l.kt)("h4",{id:"android-1"},"Android"),(0,l.kt)("p",null,"On Android the ",(0,l.kt)("inlineCode",{parentName:"p"},"Bloc")," is created in the Activity itself, eliminating the ViewModel (at least on the surface):"),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-kotlin"},"class CalculatorActivity : AppCompatActivity() {\n    val bloc by getOrCreate { calculatorBloc(it) }\n\n    val state by lazy { toLiveData(bloc) }\n\n    override fun onCreate(savedInstanceState: Bundle?) {\n        super.onCreate(savedInstanceState)\n\n        bind<ActivityCalculatorNoVmBinding>(R.layout.activity_calculator_no_vm) { it.activity = this }\n    }\n\n    fun button(action: ActionEnum) {\n        bloc.send(action)\n    }\n}\n")),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-xml"},'<data>\n    <variable\n        name="activity"\n        type="com.onegravity.bloc.calculator.CalculatorActivity" />\n    <import type="com.onegravity.bloc.sample.calculator.ActionEnum" />\n</data>\n\n\x3c!-- display State --\x3e\n<TextView\n  android:text="@{activity.state.toString()}"/>\n\n\x3c!-- send an Action --\x3e\n<Button\n  android:onClick="@{() -> activity.button(ActionEnum.Clear)}"/>\n')),(0,l.kt)("h4",{id:"ios-1"},"iOS"),(0,l.kt)("p",null,"There's no implementation on iOS side but if there were, it would look very similar to the first implementation:"),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-swift"},'struct CalculatorView: View {\n    private let holder = BlocHolder { CalculatorEnumsKt.blocEnum(context: $0) }\n    \n    @ObservedObject\n    private var model: BlocObserver<CalculatorState, ActionEnum, KotlinUnit>\n\n    init() {\n        self.model = BlocObserver(self.holder.value)\n    }\n\n    var body: some View {\n      // ...\n        \n      // display State\n      Text("\\(model.value)")\n\n      // ...\n\n      Button(action: {\n        // send an Action\n        holder.value.send(value: ActionEnum.clear)\n      }) {\n        Text("Clear")\n      }\n')))}d.isMDXComponent=!0},2228:function(e,t,n){t.Z=n.p+"assets/images/calculator-99e011bffa988c0eb0b76711122135c6.png"}}]);