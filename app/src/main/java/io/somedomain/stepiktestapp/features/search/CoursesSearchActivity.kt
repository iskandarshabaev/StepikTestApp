package io.somedomain.stepiktestapp.features.search

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import io.somedomain.stepiktestapp.R
import io.somedomain.stepiktestapp.features.CoursesFragment

class CoursesSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initToolbar()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, CoursesFragment.search())
                    .commit()
        }
    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.search_toolbar)
        setSupportActionBar(toolbar)
    }
}