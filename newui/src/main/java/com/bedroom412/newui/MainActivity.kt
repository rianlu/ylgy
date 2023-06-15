package com.bedroom412.newui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bedroom412.newui.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var homeFragment: HomeFragment
    private lateinit var blankFragment: BlankFragment
    private lateinit var mineFragment: MineFragment
    private lateinit var fragments: Array<Fragment>
    private var lastFragmentIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFragment()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.page_1 -> {
                    switchFragment(0)
                    true
                }
                R.id.page_2 -> {
                    switchFragment(1)
                    true
                }
                R.id.page_3 -> {
                    switchFragment(2)
                    true
                }
                else -> false
            }
        }
    }

    private fun initFragment() {
        homeFragment = HomeFragment()
        blankFragment = BlankFragment()
        mineFragment = MineFragment()
        fragments = arrayOf(homeFragment, blankFragment, mineFragment)
        fragments.forEach {
            addFragment(it, it.tag.toString())
            hideFragment(it)
        }
        showFragment(fragments.first())
        lastFragmentIndex = 0
    }

    private fun switchFragment(index: Int) {
        if (lastFragmentIndex == -1 || lastFragmentIndex == index || fragments.isEmpty() || fragments.size <= index) return
        val lastFragment = fragments[lastFragmentIndex]
        val currentFragment = fragments[index]
        hideFragment(lastFragment)
        showFragment(currentFragment)
        lastFragmentIndex = index
    }

    private fun addFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().add(R.id.navContainer, fragment, tag)
        .commit()
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().show(fragment)
            .commit()
    }

    private fun hideFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().hide(fragment)
            .commit()
    }
}