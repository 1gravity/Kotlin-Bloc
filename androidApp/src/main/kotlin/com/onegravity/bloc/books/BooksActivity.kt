package com.onegravity.bloc.books

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.onegravity.bloc.R
import com.onegravity.bloc.getOrCreate
import com.onegravity.bloc.sample.books.BookState
import com.onegravity.bloc.sample.books.BooksRepositoryImpl
import com.onegravity.bloc.sample.books.BooksUseCase
import com.onegravity.bloc.sample.books.BooksUseCaseImpl
import com.onegravity.bloc.sample.books.BooksUseCaseImplRedux
import com.onegravity.bloc.subscribe

class BooksActivity : AppCompatActivity() {

    private val useCase by getOrCreate { BooksUseCaseImpl(it, BooksRepositoryImpl()) }
//    private val useCase by getOrCreate { BooksUseCaseImplSimple(it, BooksRepositoryImpl()) }
//    private val useCase by getOrCreate { BooksUseCaseImplRedux(it, BooksRepositoryImpl()) }

    lateinit var pageEmpty: View
    lateinit var pageLoading: View
    lateinit var pageContent: View
    lateinit var pageError: View
    lateinit var booksMessage: TextView
    lateinit var errorMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books)
        setupView(useCase)
    }

    override fun onResume() {
        super.onResume()
        useCase.subscribe(this, state = ::observeState)
    }

    private fun observeState(state: BookState) {
        when (state) {
            is BookState.Loaded -> showContent(state)
            BookState.Empty -> pageEmpty.show()
            is BookState.Failure -> showError(state)
            BookState.Loading -> pageLoading.show()
        }
    }

    private fun showError(state: BookState.Failure) {
        pageError.show()
        errorMessage.text = state.message
    }

    @SuppressLint("SetTextI18n")
    private fun showContent(state: BookState.Loaded) {
        pageContent.show()
        val text = state.books.joinToString(separator = "\n") {
            "${it.title} (${it.year})"
        }
        booksMessage.text = "Books:\n$text"
    }

    private fun setupView(useCase: BooksUseCase) {
        pageEmpty = findViewById(R.id.pageEmpty)
        pageLoading = findViewById(R.id.pageLoading)
        pageContent = findViewById(R.id.pageContent)
        pageError = findViewById(R.id.pageError)
        booksMessage = findViewById(R.id.booksMessage)
        errorMessage = findViewById(R.id.errorMessage)
        val loadListener = View.OnClickListener { useCase.load() }
        findViewById<View>(R.id.tryAgainButton).setOnClickListener(loadListener)
        findViewById<View>(R.id.reloadButton).setOnClickListener(loadListener)
        findViewById<View>(R.id.loadButton).setOnClickListener(loadListener)

        val clearListener = View.OnClickListener { useCase.clear() }
        findViewById<View>(R.id.clearButton).setOnClickListener(clearListener)
    }

    private fun View.show() {
        if (visibility != View.VISIBLE) {
            visibility = View.VISIBLE
        }
        if (this != pageContent) pageContent.hide()
        if (this != pageLoading) pageLoading.hide()
        if (this != pageError) pageError.hide()
        if (this != pageEmpty) pageEmpty.hide()
    }

    private fun View.hide() {
        if (visibility != View.INVISIBLE) {
            visibility = View.INVISIBLE
        }
    }
}