package com.bedroom412.ylgy.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.bedroom412.ylgy.R
import com.bedroom412.ylgy.YglyApplication
import com.bedroom412.ylgy.databinding.DefaultEditImportSourceActivityBinding
import com.bedroom412.ylgy.model.ImportSource
import com.bedroom412.ylgy.model.ProxyType
import com.bedroom412.ylgy.model.SourceType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DefaultEditImportSourceActivity : AppCompatActivity() {


    private var importSource: ImportSource? = null

    val proxyRegex = Regex("^(?<schema>\\w+)://(?<ip>[^:]+):(?<port>\\d+)$")

    private lateinit var binding: DefaultEditImportSourceActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        importSource = ImportSource(
            id = null,
            url = "",
            type = null,
            name = null,
            proxyOn = null,
            lastSyncTs = null,
            playlistId = null,
            isNeedSync = 0,
            proxyType = null,
            proxyAddress = null
        );

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
        menuInflater.inflate(R.menu.import_source_edit_menus, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.import_source_check -> {

//                Toast.makeText(this,spUsername, Toast.LENGTH_SHORT ).show()
                // todo
                createOrUpdateImportSource()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun createOrUpdateImportSource() {

        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        var sourceName = sharedPreferences.getString("source_name", "")!!
        val sourceUrl = sharedPreferences.getString("source_url", "")!!
        val sourcePlaylist = sharedPreferences.getString("source_playlist", "")!!
        val sourceAutoCreatePlaylist =
            sharedPreferences.getBoolean("source_auto_create_playlist", false)!!
        val sourceProxyOn = sharedPreferences.getBoolean("source_proxy_on", false)!!
        val sourceProxyUrl = sharedPreferences.getString("source_proxy_url", "")!!

        importSource?.apply {
            name = sourceName
            url = sourceUrl
            type = SourceType.GOINDEX_THEME_ACROU.typeVal

            if (sourcePlaylist.isNotBlank()) {
                playlistId = sourcePlaylist.toInt()
            } else {
                if (name == null) {

                } else {
                    if (sourceAutoCreatePlaylist) {
                        // todo createPlayList
                    } else {

                    }
                }
            }

            proxyOn = if (sourceProxyOn) 1 else 0

            proxyRegex.matchEntire(sourceProxyUrl)?.let { matchResult ->
                val schema = matchResult.groups["schema"]?.value
                val ip = matchResult.groups["ip"]?.value
                val port = matchResult.groups["port"]?.value

                schema?.let {
                    when (it) {
                        "http" -> {
                            proxyType = ProxyType.HTTP.typeVal
                        }

                        "https" -> {
                            proxyType = ProxyType.HTTP.typeVal
                        }

                        "socks5" -> {
                            proxyType = ProxyType.SOCKS5.typeVal
                        }
                    }
                }

                proxyAddress = "$ip:$port"
            }
        }?.let {
            saveOrUpdateImportSource()
        }
    }

    private fun saveOrUpdateImportSource() {

        importSource?.let { importSource ->
            lifecycleScope.launch(Dispatchers.IO) {
                if (importSource.id == null) {
                    val id = YglyApplication.instance.db.importSourceDao().insert(importSource)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@DefaultEditImportSourceActivity,
                            "Insert Import Source(id = ${id}) Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                } else {
                    YglyApplication.instance.db.importSourceDao().update(importSource)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@DefaultEditImportSourceActivity,
                            "Update Import Source(id = ${importSource.id}) Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }


    }

    class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.default_import_source_preferences, rootKey)

            val listPreference =
                preferenceScreen.findPreference<ListPreference>("source_playlist")!!

            lifecycleScope.launch {
                withContext(Dispatchers.IO){
                    YglyApplication.instance.db.playListDao().getAll()?.let {

                        val idList = it.map {
                            "$it.id"
                        }
                        listPreference.entries = idList.toTypedArray()

                        it
                    }!!.let {
                        var valList = it.map {
                            "${it.name}"
                        }

                        listPreference.entryValues = valList.toTypedArray()
                    }
                }
            }
            listPreference.onPreferenceChangeListener = this@SettingsFragment
        }

        override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
//            return super.onPreferenceChange(preference, newValue)
            return true
        }

    }
}