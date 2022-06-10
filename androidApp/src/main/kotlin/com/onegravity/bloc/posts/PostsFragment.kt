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

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.michaelbull.result.mapBoth
import com.google.android.material.snackbar.Snackbar
import com.onegravity.bloc.R
import com.onegravity.bloc.databinding.PostListFragmentBinding
import com.onegravity.bloc.getOrCreate
import com.onegravity.bloc.sample.posts.bloc.Posts
import com.onegravity.bloc.sample.posts.bloc.PostsState
import com.onegravity.bloc.subscribe
import com.onegravity.bloc.BlocOwner
import com.onegravity.bloc.utils.viewBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class PostsFragment :
    Fragment(R.layout.post_list_fragment),
    BlocOwner<PostsState, Posts.Action, Posts.OpenPost, PostsState> {

    override val bloc by getOrCreate("posts") { Posts.bloc(it) }

    private val binding by viewBinding<PostListFragmentBinding>()

    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.apply {
            setTitle(R.string.app_name)
        }

        binding.content.layoutManager = LinearLayoutManager(activity)

        binding.content.addItemDecoration(
            SeparatorDecoration(
                requireActivity(),
                R.dimen.separator_margin_start_icon,
                R.dimen.separator_margin_end
            )
        )

        binding.content.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        subscribe(this, ::render, ::sideEffect)
    }

    private fun render(state: PostsState) {
        lifecycleScope.launchWhenResumed {
            val (spinnerVisibility, listVisibility) = when (state.loading) {
                true -> Pair(View.VISIBLE, View.INVISIBLE)
                else -> Pair(View.GONE, View.VISIBLE)
            }
            binding.indeterminateBar.visibility = spinnerVisibility
            binding.content.visibility = listVisibility

            state.posts.mapBoth(
                { posts ->
                    val items = posts.map {
                        PostsItem(it) { post -> bloc.send(Posts.Action.Clicked(post)) }
                    }
                    adapter.update(items)
                },
                { error ->
                    Snackbar.make(binding.root, "Error: ${error.message}", Snackbar.LENGTH_LONG).show()
                }
            )
        }
    }

    private fun sideEffect(sideEffect: Posts.OpenPost) {
        findNavController().navigate(
            PostsFragmentDirections.actionListFragmentToDetailFragment(
                sideEffect.post
            )
        )
    }

}
