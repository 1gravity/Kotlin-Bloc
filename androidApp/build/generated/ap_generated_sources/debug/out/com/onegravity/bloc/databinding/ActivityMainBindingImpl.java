package com.onegravity.bloc.databinding;
import com.onegravity.bloc.R;
import com.onegravity.bloc.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityMainBindingImpl extends ActivityMainBinding implements com.onegravity.bloc.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.counter, 6);
        sViewsWithIds.put(R.id.guideline, 7);
    }
    // views
    // variables
    @Nullable
    private final android.view.View.OnClickListener mCallback5;
    @Nullable
    private final android.view.View.OnClickListener mCallback3;
    @Nullable
    private final android.view.View.OnClickListener mCallback4;
    @Nullable
    private final android.view.View.OnClickListener mCallback1;
    @Nullable
    private final android.view.View.OnClickListener mCallback2;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityMainBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 8, sIncludes, sViewsWithIds));
    }
    private ActivityMainBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.Button) bindings[3]
            , (android.widget.Button) bindings[4]
            , (android.widget.TextView) bindings[6]
            , (android.widget.Button) bindings[1]
            , (android.widget.Button) bindings[2]
            , (androidx.constraintlayout.widget.Guideline) bindings[7]
            , (android.widget.Button) bindings[5]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[0]
            );
        this.books.setTag(null);
        this.calculator.setTag(null);
        this.counter1.setTag(null);
        this.counter2.setTag(null);
        this.posts.setTag(null);
        this.root.setTag(null);
        setRootTag(root);
        // listeners
        mCallback5 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 5);
        mCallback3 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 3);
        mCallback4 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 4);
        mCallback1 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 1);
        mCallback2 = new com.onegravity.bloc.generated.callback.OnClickListener(this, 2);
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
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
            setViewmodel((com.onegravity.bloc.MainViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setViewmodel(@Nullable com.onegravity.bloc.MainViewModel Viewmodel) {
        this.mViewmodel = Viewmodel;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.viewmodel);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
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
        com.onegravity.bloc.MainViewModel viewmodel = mViewmodel;
        // batch finished
        if ((dirtyFlags & 0x2L) != 0) {
            // api target 1

            this.books.setOnClickListener(mCallback3);
            this.calculator.setOnClickListener(mCallback4);
            this.counter1.setOnClickListener(mCallback1);
            this.counter2.setOnClickListener(mCallback2);
            this.posts.setOnClickListener(mCallback5);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        switch(sourceId) {
            case 5: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // viewmodel
                com.onegravity.bloc.MainViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {




                    viewmodel.onClick(com.onegravity.bloc.sample.MainMenu.ActionState.Posts);
                }
                break;
            }
            case 3: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // viewmodel
                com.onegravity.bloc.MainViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {




                    viewmodel.onClick(com.onegravity.bloc.sample.MainMenu.ActionState.Books);
                }
                break;
            }
            case 4: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // viewmodel
                com.onegravity.bloc.MainViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {




                    viewmodel.onClick(com.onegravity.bloc.sample.MainMenu.ActionState.Calculator);
                }
                break;
            }
            case 1: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // viewmodel
                com.onegravity.bloc.MainViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {




                    viewmodel.onClick(com.onegravity.bloc.sample.MainMenu.ActionState.Counter1);
                }
                break;
            }
            case 2: {
                // localize variables for thread safety
                // viewmodel != null
                boolean viewmodelJavaLangObjectNull = false;
                // viewmodel
                com.onegravity.bloc.MainViewModel viewmodel = mViewmodel;



                viewmodelJavaLangObjectNull = (viewmodel) != (null);
                if (viewmodelJavaLangObjectNull) {




                    viewmodel.onClick(com.onegravity.bloc.sample.MainMenu.ActionState.Counter2);
                }
                break;
            }
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): viewmodel
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}