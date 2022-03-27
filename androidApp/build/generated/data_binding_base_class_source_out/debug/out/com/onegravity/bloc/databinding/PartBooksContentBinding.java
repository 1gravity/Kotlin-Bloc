// Generated by view binder compiler. Do not edit!
package com.onegravity.bloc.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.onegravity.bloc.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class PartBooksContentBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView booksMessage;

  @NonNull
  public final Button clearButton;

  @NonNull
  public final Button reloadButton;

  private PartBooksContentBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextView booksMessage, @NonNull Button clearButton, @NonNull Button reloadButton) {
    this.rootView = rootView;
    this.booksMessage = booksMessage;
    this.clearButton = clearButton;
    this.reloadButton = reloadButton;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static PartBooksContentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static PartBooksContentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.part_books_content, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static PartBooksContentBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.booksMessage;
      TextView booksMessage = ViewBindings.findChildViewById(rootView, id);
      if (booksMessage == null) {
        break missingId;
      }

      id = R.id.clearButton;
      Button clearButton = ViewBindings.findChildViewById(rootView, id);
      if (clearButton == null) {
        break missingId;
      }

      id = R.id.reloadButton;
      Button reloadButton = ViewBindings.findChildViewById(rootView, id);
      if (reloadButton == null) {
        break missingId;
      }

      return new PartBooksContentBinding((ConstraintLayout) rootView, booksMessage, clearButton,
          reloadButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
