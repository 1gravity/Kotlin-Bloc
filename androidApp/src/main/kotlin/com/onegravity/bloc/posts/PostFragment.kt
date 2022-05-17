/*
 * Copyright 2021 Mikołaj Leszczyński & Appmattus Limited
 * Copyright 2020 Babylon Partners Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * File modified by Mikołaj Leszczyński & Appmattus Limited
 * See: https://github.com/orbit-mvi/orbit-mvi/compare/c5b8b3f2b83b5972ba2ad98f73f75086a89653d3...main
 */

package com.onegravity.bloc.posts

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.github.michaelbull.result.mapBoth
import com.google.android.material.snackbar.Snackbar
import com.onegravity.bloc.R
import com.onegravity.bloc.databinding.PostDetailsFragmentBinding
import com.onegravity.bloc.getOrCreate
import com.onegravity.bloc.sample.posts.bloc.PostState
import com.onegravity.bloc.sample.posts.bloc.Post
import com.onegravity.bloc.sample.posts.bloc.PostBloc
import com.onegravity.bloc.subscribe
import com.onegravity.bloc.sample.posts.domain.repositories.Post as PostData
import com.onegravity.bloc.utils.viewBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class PostFragment : Fragment(R.layout.post_details_fragment) {

    private val args: PostFragmentArgs by navArgs()
    private val post by lazy { args.post }

    private val bloc: PostBloc by getOrCreate("post") {
        Post.bloc(it).apply { send(Post.Action.Load(post.id)) }
    }

    private var initialised: Boolean = false
    private val adapter = GroupAdapter<GroupieViewHolder>()

    private val binding by viewBinding<PostDetailsFragmentBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(appCompatActivity, findNavController())

        binding.postCommentsList.layoutManager = LinearLayoutManager(activity)
        ViewCompat.setNestedScrollingEnabled(binding.postCommentsList, false)

        binding.postCommentsList.addItemDecoration(
            SeparatorDecoration(requireActivity(), R.dimen.separator_margin_start, R.dimen.separator_margin_end)
        )

        binding.postCommentsList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        bloc.sideEffects
        bloc.subscribe<PostState, Post.Action, Unit>(this, ::render)
    }

    private fun render(state: PostState) {
        val visibility = when (state.loading) {
            true -> View.VISIBLE
            else -> View.GONE
        }
        binding.indeterminateBar.visibility = visibility

        state.post?.mapBoth(
            { post ->
                if (!initialised) initialize(post)
                binding.postTitle.text = post.title
                binding.postBody.text = post.body
                val comments = post.comments.size
                binding.postCommentCount.text = context?.resources?.getQuantityString(
                    R.plurals.comments,
                    comments,
                    comments
                )
                adapter.update(post.comments.map(::PostCommentItem))
            },
            { error ->
                Snackbar.make(binding.root, "Error: ${error.message}", Snackbar.LENGTH_LONG).show()
            }
        )
    }

    private fun initialize(post: PostData) {
        initialised = true
        binding.toolbar.apply {
            title = post.username
            Glide.with(requireContext()).load(post.avatarUrl)
                .apply(RequestOptions.overrideOf(resources.getDimensionPixelSize(R.dimen.toolbar_logo_size)))
                .apply(RequestOptions.circleCropTransform()).into(
                    object : CustomTarget<Drawable>() {
                        override fun onLoadCleared(placeholder: Drawable?) {
                            placeholder?.let(::setLogo)
                        }

                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            val logo = LayerDrawable(arrayOf(resource)).apply {
                                setLayerInset(0, 0, 0, resources.getDimensionPixelSize(R.dimen.toolbar_logo_padding_end), 0)
                            }

                            setLogo(logo)
                        }
                    }
                )
        }
    }
}
