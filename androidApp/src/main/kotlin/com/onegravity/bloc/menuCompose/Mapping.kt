@file:Suppress("WildcardImport")

package com.onegravity.bloc.menuCompose

import com.onegravity.bloc.R
import com.onegravity.bloc.books.BooksActivity
import com.onegravity.bloc.calculator.CalculatorActivity
import com.onegravity.bloc.calculator.CalculatorActivityNoVM
import com.onegravity.bloc.counter.CounterActivity
import com.onegravity.bloc.counter.CounterActivityCompose
import com.onegravity.bloc.counter.CounterReduxActivity
import com.onegravity.bloc.menu.MainActivity
import com.onegravity.bloc.posts.PostsActivity
import com.onegravity.bloc.sample.MainMenuCompose.MenuEntry.*
import com.onegravity.bloc.todo.TodoActivity

val menuItem2Text = mapOf(
    MainMenu to R.string.main_menu,
    Counter to R.string.main_menu_counter,
    CounterCompose to R.string.main_menu_counter_compose,
    CounterRedux to R.string.main_menu_counter_redux,
    Books to R.string.main_menu_books,
    Calculator to R.string.main_menu_calculator,
    CalculatorNoViewModel to R.string.main_menu_calculator_no_vm,
    Posts to R.string.main_menu_posts,
    PostsCompose to R.string.main_menu_posts_compose,
    ToDo to R.string.main_menu_todo,
)

val menuItem2Activity = mapOf(
    MainMenu to MainActivity::class.java,
    Counter to CounterActivity::class.java,
    CounterCompose to CounterActivityCompose::class.java,
    CounterRedux to CounterReduxActivity::class.java,
    Books to BooksActivity::class.java,
    Calculator to CalculatorActivity::class.java,
    CalculatorNoViewModel to CalculatorActivityNoVM::class.java,
    Posts to PostsActivity::class.java,
    PostsCompose to com.onegravity.bloc.postsCompose.PostsActivity::class.java,
    ToDo to TodoActivity::class.java
)
