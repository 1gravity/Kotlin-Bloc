package com.genaku.reduce.books

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.genaku.reduce.R
import kotlinx.coroutines.launch

class BooksActivity : AppCompatActivity() {

    private val viewModel: BooksViewModel by viewModels()

    lateinit var pageEmpty: View
    lateinit var pageLoading: View
    lateinit var pageContent: View
    lateinit var pageError: View
    lateinit var booksMessage: TextView
    lateinit var errorMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books)
        setupView()
        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is BooksState.Content -> showContent(state)
                    BooksState.Empty -> pageEmpty.show()
                    is BooksState.BooksError -> showError(state)
                    BooksState.Loading -> pageLoading.show()
                }
            }
        }
    }

    private fun showError(state: BooksState.BooksError) {
        pageError.show()
        errorMessage.text = state.message
    }

    private fun showContent(state: BooksState.Content) {
        pageContent.show()
        val text = state.books.joinToString(separator = "\n") {
            "${it.title} (${it.year})"
        }
        booksMessage.text = "Books:\n$text"
    }

    private fun setupView() {
        pageEmpty = findViewById(R.id.pageEmpty)
        pageLoading = findViewById(R.id.pageLoading)
        pageContent = findViewById(R.id.pageContent)
        pageError = findViewById(R.id.pageError)
        booksMessage = findViewById(R.id.booksMessage)
        errorMessage = findViewById(R.id.errorMessage)
        val loadListener = View.OnClickListener { viewModel.load() }
        findViewById<View>(R.id.tryAgainButton).setOnClickListener(loadListener)
        findViewById<View>(R.id.reloadButton).setOnClickListener(loadListener)
        findViewById<View>(R.id.loadButton).setOnClickListener(loadListener)

        val clearListener = View.OnClickListener { viewModel.clear() }
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