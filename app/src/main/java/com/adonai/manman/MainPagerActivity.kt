package com.adonai.manman

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import com.adonai.manman.preferences.PreferencesActivity
import com.google.android.material.tabs.TabLayout

/**
 * Main activity where everything takes place
 *
 * @author Kanedias
 */
class MainPagerActivity : AppCompatActivity() {

    private lateinit var mPager: ViewPager
    private lateinit var mActionBar: Toolbar
    private lateinit var mDonateHelper: DonateHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        // should set theme prior to instantiating compat actionbar etc.
        Utils.setupTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_pager)
        mActionBar = findViewById<View>(R.id.app_toolbar) as Toolbar
        mPager = findViewById<View>(R.id.page_holder) as ViewPager

        setSupportActionBar(mActionBar)
        mPager.adapter = ManFragmentPagerAdapter(supportFragmentManager)

        val tabs = findViewById<View>(R.id.tabs) as TabLayout
        tabs.setupWithViewPager(mPager)

        // setting up vending
        mDonateHelper = DonateHelper(this)

        // applying default tab
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val index = prefs.getString("app.default.tab", "0")
        mPager.currentItem = Integer.valueOf(index!!)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.global_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about_menu_item -> {
                showAbout()
                return true
            }
            R.id.donate_menu_item -> {
                mDonateHelper.donate()
                return true
            }
            R.id.settings_menu_item -> {
                startActivity(Intent(this, PreferencesActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private inner class ManFragmentPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(i: Int): Fragment {
            when (i) {
                0 -> return ManPageSearchFragment()
                1 -> return ManChaptersFragment()
                2 -> return ManCacheFragment()
                3 -> return ManLocalArchiveFragment()
            }
            throw IllegalArgumentException(String.format("No such fragment, index was %d", i))
        }

        override fun getCount(): Int {
            return 4
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> getString(R.string.search)
                1 -> getString(R.string.contents)
                2 -> getString(R.string.cached)
                3 -> getString(R.string.local_storage)
                else -> null
            }
        }
    }

    /**
     * Shows about dialog, with description, author and stuff
     */
    @SuppressLint("InflateParams")
    private fun showAbout() {
        // Inflate the about message contents
        val messageView = layoutInflater.inflate(R.layout.about_dialog, null, false)
        val builder = AlertDialog.Builder(this)
        builder.setIcon(R.drawable.ic_launcher_notification_icon)
        builder.setTitle(R.string.app_name)
        builder.setView(messageView)
        builder.create()
        builder.show()
    }

    override fun onBackPressed() {
        if (!LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(BACK_BUTTON_NOTIFY))) {
            super.onBackPressed()
        }
    }

    companion object {
        const val FOLDER_LIST_KEY = "folder.list"
        const val DB_CHANGE_NOTIFY = "database.updated"
        const val LOCAL_CHANGE_NOTIFY = "locals.updated"
        const val BACK_BUTTON_NOTIFY = "back.button.pressed"
    }
}