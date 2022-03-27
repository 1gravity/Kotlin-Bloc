package com.onegravity.bloc.databinding;
import com.onegravity.bloc.R;
import com.onegravity.bloc.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityCalculatorBindingLandImpl extends ActivityCalculatorBinding implements com.onegravity.bloc.generated.callback.OnClickListener.Listener {

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
    private final android.view.View.OnClickListener mCallback27;
    @Nullable
    private final android.view.View.OnClickListener mCallback17;
    @Nullable
    private final android.view.View.OnClickListener mCallback25;
    @Nullable
    private final android.view.View.OnClickListener mCallback15;
    @Nullable
    private final android.view.View.OnClickListener mCallback23;
    @Nullable
    private final android.view.View.OnClickListener mCallback13;
    @Nullable
    private final android.view.View.OnClickListener mCallback21;
    @Nullable
    private final android.view.View.OnClickListener mCallback11;
    @Nullable
    private final android.view.View.OnClickListener mCallback19;
    @Nullable
    private final android.view.View.OnClickListener mCallback20;
    @Nullable
    private final android.view.View.OnClickListener mCallback16;
    @Nullable
    private final android.view.View.OnClickListener mCallback28;
    @Nullable
    private final android.view.View.OnClickListener mCallback14;
    @Nullable
    private final android.view.View.OnClickListener mCallback26;
    @Nullable
    private final android.view.View.OnClickListener mCallback12;
    @Nullable
    private final android.view.View.OnClickListener mCallback24;
    @Nullable
    private final android.view.View.OnClickListener mCallback10;
    @Nullable
    private final android.view.View.OnClickListener mCallback22;
    @Nullable
    private final android.view.View.OnClickListener mCallback18;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityCalculatorBindingLandImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 21, sIncludes, sViewsWithIds));
    }
    private ActivityCalculatorBindingLandImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
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
        mCallback27 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 18);
        mCallback17 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 8);
        mCallback25 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 16);
        mCallback15 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 6);
        mCallback23 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 14);
        mCallback13 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 4);
        mCallback21 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 12);
        mCallback11 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 2);
        mCallback19 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 10);
        mCallback20 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 11);
        mCallback16 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 7);
        mCallback28 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 19);
        mCallback14 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 5);
        mCallback26 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 17);
        mCallback12 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 3);
        mCallback24 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 15);
        mCallback10 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 1);
        mCallback22 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 13);
        mCallback18 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 9);
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

            this.button0.setOnClickListener(mCallback26);
            this.button1.setOnClickListener(mCallback22);
            this.button2.setOnClickListener(mCallback23);
            this.button3.setOnClickListener(mCallback24);
            this.button4.setOnClickListener(mCallback18);
            this.button5.setOnClickListener(mCallback19);
            this.button6.setOnClickListener(mCallback20);
            this.button7.setOnClickListener(mCallback14);
            this.button8.setOnClickListener(mCallback15);
            this.button9.setOnClickListener(mCallback16);
            this.buttonAdd.setOnClickListener(mCallback25);
            this.buttonClear.setOnClickListener(mCallback10);
            this.buttonDivide.setOnClickListener(mCallback13);
            this.buttonEquals.setOnClickListener(mCallback28);
            this.buttonMultiply.setOnClickListener(mCallback17);
            this.buttonPercentage.setOnClickListener(mCallback12);
            this.buttonPeriod.setOnClickListener(mCallback27);
            this.buttonPlusMinus.setOnClickListener(mCallback11);
            this.buttonSubtract.setOnClickListener(mCallback21);
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