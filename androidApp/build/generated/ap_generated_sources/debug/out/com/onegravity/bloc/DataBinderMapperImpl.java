package com.onegravity.bloc;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.onegravity.bloc.databinding.ActivityCalculatorBindingImpl;
import com.onegravity.bloc.databinding.ActivityCalculatorBindingLandImpl;
import com.onegravity.bloc.databinding.ActivityCounterReduxBindingImpl;
import com.onegravity.bloc.databinding.ActivityCounterSimpleBindingImpl;
import com.onegravity.bloc.databinding.ActivityMainBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_ACTIVITYCALCULATOR = 1;

  private static final int LAYOUT_ACTIVITYCOUNTERREDUX = 2;

  private static final int LAYOUT_ACTIVITYCOUNTERSIMPLE = 3;

  private static final int LAYOUT_ACTIVITYMAIN = 4;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(4);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.onegravity.bloc.R.layout.activity_calculator, LAYOUT_ACTIVITYCALCULATOR);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.onegravity.bloc.R.layout.activity_counter_redux, LAYOUT_ACTIVITYCOUNTERREDUX);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.onegravity.bloc.R.layout.activity_counter_simple, LAYOUT_ACTIVITYCOUNTERSIMPLE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.onegravity.bloc.R.layout.activity_main, LAYOUT_ACTIVITYMAIN);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_ACTIVITYCALCULATOR: {
          if ("layout-land/activity_calculator_0".equals(tag)) {
            return new ActivityCalculatorBindingLandImpl(component, view);
          }
          if ("layout/activity_calculator_0".equals(tag)) {
            return new ActivityCalculatorBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_calculator is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYCOUNTERREDUX: {
          if ("layout/activity_counter_redux_0".equals(tag)) {
            return new ActivityCounterReduxBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_counter_redux is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYCOUNTERSIMPLE: {
          if ("layout/activity_counter_simple_0".equals(tag)) {
            return new ActivityCounterSimpleBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_counter_simple is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYMAIN: {
          if ("layout/activity_main_0".equals(tag)) {
            return new ActivityMainBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_main is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(3);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    result.add(new com.onegravity.bloc.core.DataBinderMapperImpl());
    result.add(new com.onegravity.bloc.sample.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(2);

    static {
      sKeys.put(0, "_all");
      sKeys.put(1, "viewmodel");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(5);

    static {
      sKeys.put("layout-land/activity_calculator_0", com.onegravity.bloc.R.layout.activity_calculator);
      sKeys.put("layout/activity_calculator_0", com.onegravity.bloc.R.layout.activity_calculator);
      sKeys.put("layout/activity_counter_redux_0", com.onegravity.bloc.R.layout.activity_counter_redux);
      sKeys.put("layout/activity_counter_simple_0", com.onegravity.bloc.R.layout.activity_counter_simple);
      sKeys.put("layout/activity_main_0", com.onegravity.bloc.R.layout.activity_main);
    }
  }
}
