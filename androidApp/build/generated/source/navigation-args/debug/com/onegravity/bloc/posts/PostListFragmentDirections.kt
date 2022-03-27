package com.onegravity.bloc.posts

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavDirections
import com.onegravity.bloc.R
import com.onegravity.bloc.sample.posts.domain.repositories.PostOverview
import java.io.Serializable
import java.lang.UnsupportedOperationException
import kotlin.Int
import kotlin.Suppress

public class PostListFragmentDirections private constructor() {
  private data class ActionListFragmentToDetailFragment(
    public val overview: PostOverview
  ) : NavDirections {
    public override val actionId: Int = R.id.action_listFragment_to_detailFragment

    public override val arguments: Bundle
      @Suppress("CAST_NEVER_SUCCEEDS")
      get() {
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
  }

  public companion object {
    public fun actionListFragmentToDetailFragment(overview: PostOverview): NavDirections =
        ActionListFragmentToDetailFragment(overview)
  }
}
