// Generated by data binding compiler. Do not edit!
package com.onegravity.bloc.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.onegravity.bloc.R;
import com.onegravity.bloc.counter.CounterSimpleViewModel;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityCounterSimpleBinding extends ViewDataBinding {
  @NonNull
  public final TextView counter;

  @NonNull
  public final Button decrement;

  @NonNull
  public final Guideline guideline;

  @NonNull
  public final Button increment;

  @NonNull
  public final ConstraintLayout root;

  @Bindable
  protected CounterSimpleViewModel mViewmodel;

  protected ActivityCounterSimpleBinding(Object _bindingComponent, View _root, int _localFieldCount,
      TextView counter, Button decrement, Guideline guideline, Button increment,
      ConstraintLayout root) {
    super(_bindingComponent, _root, _localFieldCount);
    this.counter = counter;
    this.decrement = decrement;
    this.guideline = guideline;
    this.increment = increment;
    this.root = root;
  }

  public abstract void setViewmodel(@Nullable CounterSimpleViewModel viewmodel);

  @Nullable
  public CounterSimpleViewModel getViewmodel() {
    return mViewmodel;
  }

  @NonNull
  public static ActivityCounterSimpleBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_counter_simple, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityCounterSimpleBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityCounterSimpleBinding>inflateInternal(inflater, R.layout.activity_counter_simple, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityCounterSimpleBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_counter_simple, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityCounterSimpleBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityCounterSimpleBinding>inflateInternal(inflater, R.layout.activity_counter_simple, null, false, component);
  }

  public static ActivityCounterSimpleBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static ActivityCounterSimpleBinding bind(@NonNull View view, @Nullable Object component) {
    return (ActivityCounterSimpleBinding)bind(component, view, R.layout.activity_counter_simple);
  }
}
