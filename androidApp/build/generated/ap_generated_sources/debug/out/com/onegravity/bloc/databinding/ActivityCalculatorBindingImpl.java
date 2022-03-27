package com.onegravity.bloc.databinding;
import com.onegravity.bloc.R;
import com.onegravity.bloc.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityCalculatorBindingImpl extends ActivityCalculatorBinding implements com.onegravity.bloc.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = null;
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    @Nullable
    private final android.view.View.OnClickListener mCallback39;
    @Nullable
    private final android.view.View.OnClickListener mCallback47;
    @Nullable
    private final android.view.View.OnClickListener mCallback37;
    @Nullable
    private final android.view.View.OnClickListener mCallback45;
    @Nullable
    private final android.view.View.OnClickListener mCallback35;
    @Nullable
    private final android.view.View.OnClickListener mCallback43;
    @Nullable
    private final android.view.View.OnClickListener mCallback33;
    @Nullable
    private final android.view.View.OnClickListener mCallback29;
    @Nullable
    private final android.view.View.OnClickListener mCallback30;
    @Nullable
    private final android.view.View.OnClickListener mCallback42;
    @Nullable
    private final android.view.View.OnClickListener mCallback40;
    @Nullable
    private final android.view.View.OnClickListener mCallback38;
    @Nullable
    private final android.view.View.OnClickListener mCallback36;
    @Nullable
    private final android.view.View.OnClickListener mCallback34;
    @Nullable
    private final android.view.View.OnClickListener mCallback46;
    @Nullable
    private final android.view.View.OnClickListener mCallback32;
    @Nullable
    private final android.view.View.OnClickListener mCallback44;
    @Nullable
    private final android.view.View.OnClickListener mCallback41;
    @Nullable
    private final android.view.View.OnClickListener mCallback31;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityCalculatorBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 21, sIncludes, sViewsWithIds));
    }
    private ActivityCalculatorBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 1
            , (android.widget.Button) bindings[18]
            , (android.widget.Button) bindings[14]
            , (android.widget.Button) bindings[15]
            , (android.widget.Button) bindings[16]
            , (android.widget.Button) bindings[10]
            , (android.widget.Button) bindings[11]
            , (android.widget.Button) bindings[12]
            , (android.widget.Button) bindings[6]
            , (android.widget.Button) bindings[7]
            , (android.widget.Button) bindings[8]
            , (android.widget.Button) bindings[17]
            , (android.widget.Button) bindings[2]
            , (android.widget.Button) bindings[5]
            , (android.widget.Button) bindings[20]
            , (android.widget.Button) bindings[9]
            , (android.widget.Button) bindings[4]
            , (android.widget.Button) bindings[19]
            , (android.widget.Button) bindings[3]
            , (android.widget.Button) bindings[13]
            , (android.widget.TextView) bindings[1]
            );
        this.button0.setTag(null);
        this.button1.setTag(null);
        this.button2.setTag(null);
        this.button3.setTag(null);
        this.button4.setTag(null);
        this.button5.setTag(null);
        this.button6.setTag(null);
        this.button7.setTag(null);
        this.button8.setTag(null);
        this.button9.setTag(null);
        this.buttonAdd.setTag(null);
        this.buttonClear.setTag(null);
        this.buttonDivide.setTag(null);
        this.buttonEquals.setTag(null);
        this.buttonMultiply.setTag(null);
        this.buttonPercentage.setTag(null);
        this.buttonPeriod.setTag(null);
        this.buttonPlusMinus.setTag(null);
        this.buttonSubtract.setTag(null);
        this.currentValue.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        setRootTag(root);
        // listeners
        mCallback39 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 11);
        mCallback47 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 19);
        mCallback37 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 9);
        mCallback45 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 17);
        mCallback35 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 7);
        mCallback43 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 15);
        mCallback33 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 5);
        mCallback29 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 1);
        mCallback30 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 2);
        mCallback42 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 14);
        mCallback40 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 12);
        mCallback38 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 10);
        mCallback36 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 8);
        mCallback34 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 6);
        mCallback46 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 18);
        mCallback32 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 4);
        mCallback44 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 16);
        mCallback41 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 13);
        mCallback31 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 3);
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.viewmodel == variableId) {
            setViewmodel((com.onegravity.bloc.calculator.CalculatorViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setViewmodel(@Nullable com.onegravity.bloc.calculator.CalculatorViewModel Viewmodel) {
        this.mViewmodel = Viewmodel;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.viewmodel);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeViewmodelState((androidx.lifecycle.LiveData<com.onegravity.bloc.sample.calculator.State>) object, fieldId);
        }
        return false;
    }
    private boolean onChangeViewmodelState(androidx.lifecycle.LiveData<com.onegravity.bloc.sample.calculator.State> ViewmodelState, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        java.lang.String viewmodelStateToString = null;
        com.onegravity.bloc.sample.calculator.State viewmodelStateGetValue = null;
        androidx.lifecycle.LiveData<com.onegravity.bloc.sample.calculator.State> viewmodelState = null;
        com.onegravity.bloc.calculator.CalculatorViewModel viewmodel = mViewmodel;

        if ((dirtyFlags & 0x7L) != 0) {



                if (viewmodel != null) {
                    // read viewmodel.state
                    viewmodelState = viewmodel.getState();
                }
                updateLiveDataRegistration(0, viewmodelState);


                if (viewmodelState != null) {
                    // read viewmodel.state.getValue()
                    viewmodelStateGetValue = viewmodelState.getValue();
                }


                if (viewmodelStateGetValue != null) {
                    // read viewmodel.state.getValue().toString()
                    viewmodelStateToString = viewmodelStateGetValue.toString();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x4L) != 0) {
            // api target 1

            this.button0.setOnClickListener(mCallback45);
            this.button1.setOnClickListener(mCallback41);
            this.button2.setOnClickListener(mCallback42);
            this.button3.setOnClickListener(mCallback43);
            this.button4.setOnClickListener(mCallback37);
            this.button5.setOnClickListener(mCallback38);
            this.button6.setOnClickListener(mCallback39);
            this.button7.setOnClickListener(mCallback33);
            this.button8.setOnClickListener(mCallback34);
            this.button9.setOnClickListener(mCallback35);
            this.buttonAdd.setOnClickListener(mCallback44);
            this.buttonClear.setOnClickListener(mCallback29);
            this.buttonDivide.setOnClickListener(mCallback32);
            this.buttonEquals.setOnClickListener(mCallback47);
            this.buttonMultiply.setOnClickListener(mCallback36);
            this.buttonPercentage.setOnClickListener(mCallback31);
            this.buttonPeriod.setOnClickListener(mCallback46);
            this.buttonPlusMinus.setOnClickListener(mCallback30);
            this.buttonSubtract.setOnClickListener(mCallback40);
        }
        if ((dirtyFlags & 0x7L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.currentValue, viewmodelStateToString);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        switch(sourceId) {
            case 11: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // view.id
                int callbackArg0Id = 0;
                // viewmodel
                com.onegravity.bloc.calculator.CalculatorViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {


                    if ((callbackArg_0) != (null)) {


                        callbackArg0Id = callbackArg_0.getId();

                        viewmodel.button(callbackArg0Id);
                    }
                }
                break;
            }
            case 19: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // view.id
                int callbackArg0Id = 0;
                // viewmodel
                com.onegravity.bloc.calculator.CalculatorViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {


                    if ((callbackArg_0) != (null)) {


                        callbackArg0Id = callbackArg_0.getId();

                        viewmodel.button(callbackArg0Id);
                    }
                }
                break;
            }
            case 9: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // view.id
                int callbackArg0Id = 0;
                // viewmodel
                com.onegravity.bloc.calculator.CalculatorViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {


                    if ((callbackArg_0) != (null)) {


                        callbackArg0Id = callbackArg_0.getId();

                        viewmodel.button(callbackArg0Id);
                    }
                }
                break;
            }
            case 17: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // view.id
                int callbackArg0Id = 0;
                // viewmodel
                com.onegravity.bloc.calculator.CalculatorViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {


                    if ((callbackArg_0) != (null)) {


                        callbackArg0Id = callbackArg_0.getId();

                        viewmodel.button(callbackArg0Id);
                    }
                }
                break;
            }
            case 7: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // view.id
                int callbackArg0Id = 0;
                // viewmodel
                com.onegravity.bloc.calculator.CalculatorViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {


                    if ((callbackArg_0) != (null)) {


                        callbackArg0Id = callbackArg_0.getId();

                        viewmodel.button(callbackArg0Id);
                    }
                }
                break;
            }
            case 15: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // view.id
                int callbackArg0Id = 0;
                // viewmodel
                com.onegravity.bloc.calculator.CalculatorViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {


                    if ((callbackArg_0) != (null)) {


                        callbackArg0Id = callbackArg_0.getId();

                        viewmodel.button(callbackArg0Id);
                    }
                }
                break;
            }
            case 5: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // view.id
                int callbackArg0Id = 0;
                // viewmodel
                com.onegravity.bloc.calculator.CalculatorViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {


                    if ((callbackArg_0) != (null)) {


                        callbackArg0Id = callbackArg_0.getId();

                        viewmodel.button(callbackArg0Id);
                    }
                }
                break;
            }
            case 1: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // view.id
                int callbackArg0Id = 0;
                // viewmodel
                com.onegravity.bloc.calculator.CalculatorViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {


                    if ((callbackArg_0) != (null)) {


                        callbackArg0Id = callbackArg_0.getId();

                        viewmodel.button(callbackArg0Id);
                    }
                }
                break;
            }
            case 2: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // view.id
                int callbackArg0Id = 0;
                // viewmodel
                com.onegravity.bloc.calculator.CalculatorViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {


                    if ((callbackArg_0) != (null)) {


                        callbackArg0Id = callbackArg_0.getId();

                        viewmodel.button(callbackArg0Id);
                    }
                }
                break;
            }
            case 14: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // view.id
                int callbackArg0Id = 0;
                // viewmodel
                com.onegravity.bloc.calculator.CalculatorViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {


                    if ((callbackArg_0) != (null)) {


                        callbackArg0Id = callbackArg_0.getId();

                        viewmodel.button(callbackArg0Id);
                    }
                }
                break;
            }
            case 12: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // view.id
                int callbackArg0Id = 0;
                // viewmodel
                com.onegravity.bloc.calculator.CalculatorViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {


                    if ((callbackArg_0) != (null)) {


                        callbackArg0Id = callbackArg_0.getId();

                        viewmodel.button(callbackArg0Id);
                    }
                }
                break;
            }
            case 10: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // view.id
                int callbackArg0Id = 0;
                // viewmodel
                com.onegravity.bloc.calculator.CalculatorViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {


                    if ((callbackArg_0) != (null)) {


                        callbackArg0Id = callbackArg_0.getId();

                        viewmodel.button(callbackArg0Id);
                    }
                }
                break;
            }
            case 8: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // view.id
                int callbackArg0Id = 0;
                // viewmodel
                com.onegravity.bloc.calculator.CalculatorViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {


                    if ((callbackArg_0) != (null)) {


                        callbackArg0Id = callbackArg_0.getId();

                        viewmodel.button(callbackArg0Id);
                    }
                }
                break;
            }
            case 6: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // view.id
                int callbackArg0Id = 0;
                // viewmodel
                com.onegravity.bloc.calculator.CalculatorViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {


                    if ((callbackArg_0) != (null)) {


                        callbackArg0Id = callbackArg_0.getId();

                        viewmodel.button(callbackArg0Id);
                    }
                }
                break;
            }
            case 18: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // view.id
                int callbackArg0Id = 0;
                // viewmodel
                com.onegravity.bloc.calculator.CalculatorViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {


                    if ((callbackArg_0) != (null)) {


                        callbackArg0Id = callbackArg_0.getId();

                        viewmodel.button(callbackArg0Id);
                    }
                }
                break;
            }
            case 4: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // view.id
                int callbackArg0Id = 0;
                // viewmodel
                com.onegravity.bloc.calculator.CalculatorViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {


                    if ((callbackArg_0) != (null)) {


                        callbackArg0Id = callbackArg_0.getId();

                        viewmodel.button(callbackArg0Id);
                    }
                }
                break;
            }
            case 16: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // view.id
                int callbackArg0Id = 0;
                // viewmodel
                com.onegravity.bloc.calculator.CalculatorViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {


                    if ((callbackArg_0) != (null)) {


                        callbackArg0Id = callbackArg_0.getId();

                        viewmodel.button(callbackArg0Id);
                    }
                }
                break;
            }
            case 13: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // view.id
                int callbackArg0Id = 0;
                // viewmodel
                com.onegravity.bloc.calculator.CalculatorViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {


                    if ((callbackArg_0) != (null)) {


                        callbackArg0Id = callbackArg_0.getId();

                        viewmodel.button(callbackArg0Id);
                    }
                }
                break;
            }
            case 3: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // view.id
                int callbackArg0Id = 0;
                // viewmodel
                com.onegravity.bloc.calculator.CalculatorViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {


                    if ((callbackArg_0) != (null)) {


                        callbackArg0Id = callbackArg_0.getId();

                        viewmodel.button(callbackArg0Id);
                    }
                }
                break;
            }
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): viewmodel.state
        flag 1 (0x2L): viewmodel
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}