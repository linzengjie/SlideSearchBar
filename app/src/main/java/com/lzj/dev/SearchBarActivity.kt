package com.lzj.dev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.lzj.slidebar.QuickSearchView
import kotlinx.android.synthetic.main.activity_search_bar.*

class SearchBarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_bar)

        searchView.setOnLetterChangedListener(object : QuickSearchView.OnLetterChangedListener{
            override fun onTouchStop() {
                tvLetter.visibility = View.GONE
            }

            override fun onLetterChanged(letter: String?, y: Float) {
                tvLetter.visibility = View.VISIBLE
                tvLetter.text = letter
            }
        })
    }
}