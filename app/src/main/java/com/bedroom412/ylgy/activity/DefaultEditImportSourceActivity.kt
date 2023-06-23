package com.bedroom412.ylgy.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.bedroom412.ylgy.R
import com.bedroom412.ylgy.databinding.DefaultEditImportSourceActivityBinding
import com.bedroom412.ylgy.model.ImportSource


class DefaultEditImportSourceActivity : AppCompatActivity() {


    private val importSource : ImportSource? = null

    private lateinit var binding: DefaultEditImportSourceActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DefaultEditImportSourceActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }

        setSupportActionBar(binding.settingsToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.import_source_menus, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.import_source_check -> {
                val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                val sourceName = sharedPreferences.getString("source_name", "")
                val sourceUrl = sharedPreferences.getString("source_url", "")
                val sourcePlaylist = sharedPreferences.getString("source_playlist", "")
                val sourceAutoCreatePlaylist =  sharedPreferences.getBoolean("source_auto_create_playlist", false)
                val sourceProxyOn = sharedPreferences.getBoolean("source_proxy_on", false)
                val sourceProxyUrl = sharedPreferences.getString("source_proxy_url", "")
//                Toast.makeText(this,spUsername, Toast.LENGTH_SHORT ).show()
                // todo
            }
        }


        return super.onOptionsItemSelected(item)
    }

    class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.default_import_source_preferences, rootKey)
            val listPreference = preferenceScreen.findPreference<ListPreference>("source_playlist")!!
            listPreference.entries = arrayOf("1")
            listPreference.entryValues = arrayOf("1")
            listPreference.onPreferenceChangeListener = this@SettingsFragment
        }

        override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
//            return super.onPreferenceChange(preference, newValue)
            return true
        }

    }
}