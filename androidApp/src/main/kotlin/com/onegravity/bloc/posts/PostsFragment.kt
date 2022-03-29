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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.michaelbull.result.mapBoth
import com.google.android.material.snackbar.Snackbar
import com.onegravity.bloc.R
import com.onegravity.bloc.databinding.PostListFragmentBinding
import com.onegravity.bloc.sample.posts.bloc.PostList
import com.onegravity.bloc.utils.viewBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.onegravity.bloc.sample.posts.bloc.PostListState
import com.onegravity.bloc.subscribe

class PostsFragment : Fragment(R.layout.post_list_fragment) {

    private val viewModel: PostsViewModel by viewModels()

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
        viewModel.subscribe(this, ::render, ::sideEffect)
    }

    private fun render(state: PostListState) {
        lifecycleScope.launchWhenResumed {
            val (spinnerVisibility, listVisibility) = when (state.loading) {
                true -> Pair(View.VISIBLE, View.INVISIBLE)
                else -> Pair(View.GONE, View.VISIBLE)
            }
            binding.indeterminateBar.visibility = spinnerVisibility
            binding.content.visibility = listVisibility

            state.overviews.mapBoth(
                { posts ->
                    posts.sortedBy { it.title }
                        .map { PostsItem(it, viewModel) }
                        .also { adapter.update(it) }
                },
                { error ->
                    Snackbar
                        .make(binding.root, "Error: ${error.message}", Snackbar.LENGTH_LONG)
                        .show()
                }
            )
        }
    }

    private fun sideEffect(sideEffect: PostList.OpenPost) {
        findNavController().navigate(
            PostsFragmentDirections.actionListFragmentToDetailFragment(
                sideEffect.postOverview
            )
        )
    }
}
