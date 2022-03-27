package com.onegravity.bloc.databinding;
import com.onegravity.bloc.R;
import com.onegravity.bloc.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityCounterReduxBindingImpl extends ActivityCounterReduxBinding implements com.onegravity.bloc.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.guideline, 4);
    }
    // views
    // variables
    @Nullable
    private final android.view.View.OnClickListener mCallback7;
    @Nullable
    private final android.view.View.OnClickListener mCallback6;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityCounterReduxBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 5, sIncludes, sViewsWithIds));
    }
    private ActivityCounterReduxBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 1
            , (android.widget.TextView) bindings[1]
            , (android.widget.Button) bindings[3]
            , (androidx.constraintlayout.widget.Guideline) bindings[4]
            , (android.widget.Button) bindings[2]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[0]
            );
        this.counter.setTag(null);
        this.decrement.setTag(null);
        this.increment.setTag(null);
        this.root.setTag(null);
        setRootTag(root);
        // listeners
        mCallback7 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 2);
        mCallback6 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 1);
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
            setViewmodel((com.onegravity.bloc.counter.CounterReduxViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setViewmodel(@Nullable com.onegravity.bloc.counter.CounterReduxViewModel Viewmodel) {
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
                return onChangeViewmodelState((androidx.lifecycle.LiveData<java.lang.Integer>) object, fieldId);
        }
        return false;
    }
    private boolean onChangeViewmodelState(androidx.lifecycle.LiveData<java.lang.Integer> ViewmodelState, int fieldId) {
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
        androidx.lifecycle.LiveData<java.lang.Integer> viewmodelState = null;
        java.lang.String javaLangStringCounterViewmodelState = null;
        java.lang.Integer viewmodelStateGetValue = null;
        com.onegravity.bloc.counter.CounterReduxViewModel viewmodel = mViewmodel;

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


                // read ("Counter: ") + (viewmodel.state.getValue())
                javaLangStringCounterViewmodelState = ("Counter: ") + (viewmodelStateGetValue);
        }
        // batch finished
        if ((dirtyFlags & 0x7L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.counter, javaLangStringCounterViewmodelState);
        }
        if ((dirtyFlags & 0x4L) != 0) {
            // api target 1

            this.decrement.setOnClickListener(mCallback7);
            this.increment.setOnClickListener(mCallback6);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        switch(sourceId) {
            case 2: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // viewmodel
                com.onegravity.bloc.counter.CounterReduxViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {


                    viewmodel.decrement();
                }
                break;
            }
            case 1: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // viewmodel
                com.onegravity.bloc.counter.CounterReduxViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {


                    viewmodel.increment();
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