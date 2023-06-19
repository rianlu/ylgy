package com.bedroom412.newui

import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bedroom412.newui.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import eightbitlab.com.blurview.RenderScriptBlur

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

        initBlurNav()

        initTabLayout()
    }

    private fun initTabLayout() {
        binding.navTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab == null) return
                switchFragment(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun initBlurNav() {
        val radius = 20f
        val rootView = window.decorView.findViewById(android.R.id.content) as ViewGroup
        binding.navBlurView.setupWith(rootView, RenderScriptBlur(this))
            .setFrameClearDrawable(window.decorView.background)
            .setBlurRadius(radius)
        binding.navBlurView.outlineProvider = ViewOutlineProvider.BACKGROUND
        binding.navBlurView.clipToOutline = true
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