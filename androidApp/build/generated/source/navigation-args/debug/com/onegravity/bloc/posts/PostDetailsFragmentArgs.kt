package com.onegravity.bloc.posts

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import com.onegravity.bloc.sample.posts.domain.repositories.PostOverview
import java.io.Serializable
import java.lang.IllegalArgumentException
import java.lang.UnsupportedOperationException
import kotlin.Suppress
import kotlin.jvm.JvmStatic

public data class PostDetailsFragmentArgs(
  public val overview: PostOverview
) : NavArgs {
  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toBundle(): Bundle {
    val result = Bundle()
    if (Parcelable::class.java.isAssignableFrom(PostOverview::class.java)) {
      result.putParcelable("overview", this.overview as Parcelable)
    } else if (Serializable::class.java.isAssignableFrom(PostOverview::class.java)) {
      result.putSerializable("overview", this.overview as Serializable)
    } else {
      throw UnsupportedOperationException(PostOverview::class.java.name +
          " must implement Parcelable or Serializable or must be an Enum.")
    }
    return result
  }

  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    if (Parcelable::class.java.isAssignableFrom(PostOverview::class.java)) {
      result.set("overview", this.overview as Parcelable)
    } else if (Serializable::class.java.isAssignableFrom(PostOverview::class.java)) {
      result.set("overview", this.overview as Serializable)
    } else {
      throw UnsupportedOperationException(PostOverview::class.java.name +
          " must implement Parcelable or Serializable or must be an Enum.")
    }
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): PostDetailsFragmentArgs {
      bundle.setClassLoader(PostDetailsFragmentArgs::class.java.classLoader)
      val __overview : PostOverview?
      if (bundle.containsKey("overview")) {
        if (Parcelable::class.java.isAssignableFrom(PostOverview::class.java) ||
            Serializable::class.java.isAssignableFrom(PostOverview::class.java)) {
          __overview = bundle.get("overview") as PostOverview?
        } else {
          throw UnsupportedOperationException(PostOverview::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
        if (__overview == null) {
          throw IllegalArgumentException("Argument \"overview\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"overview\" is missing and does not have an android:defaultValue")
      }
      return PostDetailsFragmentArgs(__overview)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): PostDetailsFragmentArgs {
      val __overview : PostOverview?
      if (savedStateHandle.contains("overview")) {
        if (Parcelable::class.java.isAssignableFrom(PostOverview::class.java) ||
            Serializable::class.java.isAssignableFrom(PostOverview::class.java)) {
          __overview = savedStateHandle.get<PostOverview?>("overview")
        } else {
          throw UnsupportedOperationException(PostOverview::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
        if (__overview == null) {
          throw IllegalArgumentException("Argument \"overview\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"overview\" is missing and does not have an android:defaultValue")
      }
      return PostDetailsFragmentArgs(__overview)
    }
  }
}
