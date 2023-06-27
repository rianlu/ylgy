package com.bedroom412.ylgy.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bedroom412.ylgy.R
import com.bedroom412.ylgy.SourceViewModel
import com.bedroom412.ylgy.databinding.ActivitySourceBinding
import com.bedroom412.ylgy.recycleview.SourceRecycleViewAdapter


class SourceActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySourceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySourceBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.sourceToolbar)

        val sourceViewModel =
            ViewModelProvider(this).get(SourceViewModel::class.java)
        val adapter = SourceRecycleViewAdapter(sourceViewModel)
        binding.sourceRecyclerView.adapter = adapter
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_goindex_theme_acrou -> {
                val intent = Intent(this, DefaultEditImportSourceActivity::class.java)
                startActivity(intent)
            }
            else -> {

                if (!item.hasSubMenu()) {
                    Toast.makeText(this, "Source not support yet", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.import_source_menus, menu)
        return true

    }
}